package com.example.skystayback.services;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.UserRol;
import com.example.skystayback.exceptions.ApiException;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.UserRepository;
import com.example.skystayback.security.JwtService;
import com.example.skystayback.services.email.EmailService;
import com.example.skystayback.services.email.EmailTemplateType;
import com.example.skystayback.utils.ErrorUtils;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findTopByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public ResponseVO<AuthenticationVO> register(UserRegisterVO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            ErrorUtils.throwEmailExistsError();
        }
        if (userRepository.existsByNif(userDTO.getNif())) {
            ErrorUtils.throwApiError("NIF ya registrado","El NIF introducido ya está en uso.", "NIF_EXISTS");
        }
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            ErrorUtils.throwApiError("Número de teléfono registrado","El número de teléfono introducido ya está en uso.", "PHONE_EXISTS");
        }

        User user = new User();
        user.setUserCode(generateUniqueUserCode());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBirthDate(userDTO.getBirthDate());
        user.setNif(userDTO.getNif());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setImg(userDTO.getImg());
        user.setValidation(false);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRol(UserRol.ROLE_CLIENT);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user, user.getUserCode(), user.getRol().name());

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());

        try {
            int verificationCode = generateVerificationCode();
            variables.put("verificationCode", verificationCode);

            emailService.sendEmail(user.getEmail(), "Validation code", EmailTemplateType.REGISTRATION, variables);

            user.setCode(verificationCode);
            user.setValidation_date(LocalDateTime.now());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage(), e);
        }


        return ResponseVO.<AuthenticationVO>builder()
                .response(new DataVO<>(AuthenticationVO.builder().token(jwtToken).build()))
                .messages(new MessageResponseVO("Registro exitoso", 200, LocalDateTime.now()))
                .build();
    }

    public Integer generateVerificationCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return code;
    }

    public String generateUniqueUserCode() {
        final int MAX_ATTEMPTS = 10;
        String uniqueCode;
        int attempts = 0;

        do {
            uniqueCode = generateShortUuid();
            attempts++;

            if (attempts >= MAX_ATTEMPTS) {
                throw new RuntimeException("No se pudo generar un código de usuario único después de " + MAX_ATTEMPTS + " intentos");
            }
        } while (userRepository.existsByUserCode(uniqueCode));

        return uniqueCode;
    }

    public String generateShortUuid() {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = toBytes(uuid);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes).substring(0, 16);
    }

    public byte[] toBytes(UUID uuid) {
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];
        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (mostSigBits >>> (8 * (7 - i)));
            buffer[i + 8] = (byte) (leastSigBits >>> (8 * (7 - i)));
        }
        return buffer;
    }




    public ResponseVO<AuthenticationVO> login(UserLoginVO userLoginDTO) {
        User user = userRepository.findTopByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new ApiException(
                        "Credenciales inválidas",
                        "El correo electrónico o la contraseña son incorrectos",
                        "INVALID_CREDENTIALS"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new ApiException(
                    "Credenciales inválidas",
                    "El correo electrónico o la contraseña son incorrectos",
                    "INVALID_CREDENTIALS");
        }

        String jwtToken = jwtService.generateToken(user, user.getUserCode(), user.getRol().name());
        return ResponseVO.<AuthenticationVO>builder()
                .response(new DataVO<>(AuthenticationVO.builder().token(jwtToken).build()))
                .messages(new MessageResponseVO("Inicio de sesión exitoso", 200, LocalDateTime.now()))
                .build();
    }

    public ResponseVO<MessageResponseVO> code_check(Integer code, String email){
        // Buscar al usuario con el código proporcionado
        User user = userRepository.findTopByEmailAndCode(email, code)
                .orElseThrow(() -> new ApiException(
                        "Código inválido",
                        "El código de verificación no es válido para este usuario.",
                        "INVALID_CODE"));

        // Verificar si el código ha expirado (10 minutos de validez)
        LocalDateTime now = LocalDateTime.now();
        if (user.getValidation_date() == null || user.getValidation_date().plusMinutes(10).isBefore(now)) {
            throw new ApiException(
                    "Código expirado",
                    "El código de verificación ha expirado. Solicita uno nuevo.",
                    "EXPIRED_CODE");
        }

        // Si el código es válido, actualizar el estado de validación del usuario
        user.setValidation(true);
        user.setCode(null); // Limpiar el código después de la validación
        user.setValidation_date(null); // Limpiar la fecha de validación
        userRepository.save(user);

        // Devolver una respuesta exitosa
        return ResponseVO.<MessageResponseVO>builder()
                .response(new DataVO<>(new MessageResponseVO("Código validado correctamente", 200, LocalDateTime.now())))
                .build();
    }


    public ResponseVO<TokenDecodeVO> decodeToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseVO<>(null, new MessageResponseVO("Token no proporcionado o inválido", 401, LocalDateTime.now()));
        }
        try{
            String tokenWithoutBearer = token.replace("Bearer ", "");

            String email = jwtService.extractUsername(tokenWithoutBearer);

            User user = userRepository.findTopByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String name = user.getName();
            String rol = user.getRol().toString().split("_")[1];

            new TokenDecodeVO();
            TokenDecodeVO tokenDecodeVO = TokenDecodeVO.builder().name(name).role(rol).build();

            return ResponseVO.<TokenDecodeVO>builder()
                    .response(new DataVO<>(tokenDecodeVO))
                    .messages(new MessageResponseVO("Token decodificado", 200, LocalDateTime.now()))
                    .build();
        }catch (UsernameNotFoundException e){
            return new ResponseVO<>(null, new MessageResponseVO("Token inválido", 401, LocalDateTime.now()));
        }catch (Exception e){
            return new ResponseVO<>(null, new MessageResponseVO("Error al intentar descodificar el token:", 400, LocalDateTime.now()));
        }
    }

    public ResponseVO<MessageResponseVO> resendCode(String email) {
        User user = userRepository.findTopByEmail(email)
                .orElseThrow(() -> new ApiException(
                        "Usuario no encontrado",
                        "No se encontró un usuario con el correo electrónico proporcionado.",
                        "USER_NOT_FOUND"));

        if (user.getValidation() && user.getCode() == null) {
            return ResponseVO.<MessageResponseVO>builder()
                    .response(new DataVO<>(new MessageResponseVO("El usuario ya está validado", 400, LocalDateTime.now())))
                    .build();
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());

        try {
            int verificationCode = generateVerificationCode();
            variables.put("verificationCode", verificationCode);

            emailService.sendEmail(user.getEmail(), "Validation code", EmailTemplateType.REGISTRATION, variables);

            user.setCode(verificationCode);
            user.setValidation_date(LocalDateTime.now());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage(), e);
        }

        return ResponseVO.<MessageResponseVO>builder()
                .response(new DataVO<>(new MessageResponseVO("Código de verificación reenviado", 200, LocalDateTime.now())))
                .build();
    }
}
