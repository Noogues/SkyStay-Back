package com.example.skystayback.services;

import com.example.skystayback.dtos.AuthenticationDTO;
import com.example.skystayback.dtos.UserLoginDTO;
import com.example.skystayback.dtos.UserRegisterDTO;
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

    public AuthenticationDTO register(UserRegisterDTO userDTO) {

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
        user.setRol(UserRol.CLIENT);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user, user.getId(), user.getRol().name());
        return AuthenticationDTO.builder().token(jwtToken).build();
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

    public AuthenticationDTO login(UserLoginDTO userLoginDTO) {
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

        String jwtToken = jwtService.generateToken(user, user.getId(), user.getRol().name());
        return AuthenticationDTO.builder().token(jwtToken).build();
    }
}
