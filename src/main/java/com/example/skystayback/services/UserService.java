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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        validateUserRegisterVO(userDTO);

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

    private void validateUserRegisterVO(UserRegisterVO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            ErrorUtils.throwApiError("Email requerido", "El email no puede estar vacío.", "EMAIL_REQUIRED");
        }
        if (!userDTO.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            ErrorUtils.throwApiError("Email inválido", "El formato del email es incorrecto.", "EMAIL_INVALID");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            ErrorUtils.throwApiError("Contraseña requerida", "La contraseña no puede estar vacía.", "PASSWORD_REQUIRED");
        }
        if (!userDTO.getPassword().matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]).{6,}$")) {
            ErrorUtils.throwApiError("Contraseña inválida", "La contraseña no cumple los requisitos.", "PASSWORD_INVALID");
        }
        if (userDTO.getName() == null || userDTO.getName().isBlank()) {
            ErrorUtils.throwApiError("Nombre requerido", "El nombre no puede estar vacío.", "NAME_REQUIRED");
        }
        if (userDTO.getLastName() == null || userDTO.getLastName().isBlank()) {
            ErrorUtils.throwApiError("Apellido requerido", "El apellido no puede estar vacío.", "LASTNAME_REQUIRED");
        }
        if (userDTO.getNif() == null || userDTO.getNif().isBlank()) {
            ErrorUtils.throwApiError("NIF requerido", "El NIF no puede estar vacío.", "NIF_REQUIRED");
        }
        if (!userDTO.getNif().matches("^[0-9]{8}[A-Z]$")) {
            ErrorUtils.throwApiError("NIF inválido", "El formato del NIF es incorrecto.", "NIF_INVALID");
        } else {
            String letters = "TRWAGMYFPDXBNJZSQVHLCKE";
            int number = Integer.parseInt(userDTO.getNif().substring(0, 8));
            char providedLetter = userDTO.getNif().charAt(8);
            char correctLetter = letters.charAt(number % 23);
            if (providedLetter != correctLetter) {
                ErrorUtils.throwApiError("NIF inválido", "La letra del NIF no es correcta.", "NIF_INVALID_DATA");
            }
        }
        if (userDTO.getPhone() == null || userDTO.getPhone().isBlank()) {
            ErrorUtils.throwApiError("Teléfono requerido", "El teléfono no puede estar vacío.", "PHONE_REQUIRED");
        }
        if (!userDTO.getPhone().matches("^\\d{9,15}$")) {
            ErrorUtils.throwApiError("Teléfono inválido", "El formato del teléfono es incorrecto.", "PHONE_INVALID");
        }
        if (userDTO.getBirthDate() == null) {
            ErrorUtils.throwApiError("Fecha de nacimiento requerida", "La fecha de nacimiento no puede estar vacía.", "BIRTHDATE_REQUIRED");
        } else {
            LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
            LocalDate birthDate = userDTO.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (birthDate.isAfter(eighteenYearsAgo)) {
                ErrorUtils.throwApiError("Edad mínima", "Debes ser mayor de 18 años.", "MIN_AGE");
            }
        }
        if (userDTO.getGender() == null) {
            ErrorUtils.throwApiError("Género requerido", "El género no puede estar vacío.", "GENDER_REQUIRED");
        }
    }

    public Integer generateVerificationCode() {
        return (int) (Math.random() * 900000) + 100000;
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

        if (!user.getValidation()) {
            throw new ApiException(
                    "Usuario no validado",
                    "El usuario no ha sido validado. Por favor, verifica tu correo electrónico.",
                    "USER_NOT_VALIDATED");
        }

        String jwtToken = jwtService.generateToken(user, user.getUserCode(), user.getRol().name());
        return ResponseVO.<AuthenticationVO>builder()
                .response(new DataVO<>(AuthenticationVO.builder().token(jwtToken).build()))
                .messages(new MessageResponseVO("Inicio de sesión exitoso", 200, LocalDateTime.now()))
                .build();
    }

    public ResponseVO<MessageResponseVO> code_check(Integer code, String email){
        User user = userRepository.findTopByEmailAndCode(email, code)
                .orElseThrow(() -> new ApiException(
                        "Código inválido",
                        "El código de verificación no es válido para este usuario.",
                        "INVALID_CODE"));

        LocalDateTime now = LocalDateTime.now();
        if (user.getValidation_date() == null || user.getValidation_date().plusMinutes(10).isBefore(now)) {
            throw new ApiException(
                    "Código expirado",
                    "El código de verificación ha expirado. Solicita uno nuevo.",
                    "EXPIRED_CODE");
        }

        user.setValidation(true);
        user.setCode(null);
        user.setValidation_date(null);
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
