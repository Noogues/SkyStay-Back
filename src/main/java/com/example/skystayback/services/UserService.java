package com.example.skystayback.services;

import com.example.skystayback.dtos.*;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.UserRol;
import com.example.skystayback.exceptions.ApiException;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.UserRepository;
import com.example.skystayback.security.JwtService;
import com.example.skystayback.utils.ErrorUtils;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRol(UserRol.ROLE_CLIENT);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user, user.getUserCode(), user.getRol().name());

        return ResponseVO.<AuthenticationVO>builder()
                .response(new DataVO<>(AuthenticationVO.builder().token(jwtToken).build()))
                .messages(new MessageResponseVO("Registro exitoso", 200, LocalDateTime.now()))
                .build();
    }

    private String generateUniqueUserCode() {
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

    private String generateShortUuid() {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = toBytes(uuid);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes).substring(0, 16);
    }

    private byte[] toBytes(UUID uuid) {
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

    public ResponseVO<UserInfoVO> getUserInfoByCode(String userCode){
        try{
            User user = userRepository.getUserByUserCode(userCode);
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setName(user.getName() + " " + user.getLastName());
            // Quitar ROLE_ y ademas dejar solo en mayuscula la primera letra.
            String rol = user.getRol().toString().toLowerCase().split("_")[1];
            rol = Character.toUpperCase(rol.charAt(0)) + rol.substring(1);
            userInfoVO.setRole(rol);
            return ResponseVO.<UserInfoVO>builder()
                    .response(new DataVO<>(userInfoVO))
                    .messages(new MessageResponseVO("Información del usuario obtenida", 200, LocalDateTime.now()))
                    .build();
        }catch (Exception e){
            return new ResponseVO<>(null, new MessageResponseVO("Error al intentar obtener la información del usuario", 400, LocalDateTime.now()));
        }
    }
}
