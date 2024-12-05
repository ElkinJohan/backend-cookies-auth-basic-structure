package com.elkin.cookiesAuth.services.impl;

import com.elkin.cookiesAuth.dto.CredentialsDto;
import com.elkin.cookiesAuth.dto.UserDto;
import com.elkin.cookiesAuth.services.AuthenticationService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    //esta clave secreta se usa para crear la firma
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //este metodo verifica el nombre de usuario y la validez del pass,
    //solo se verifica con una contrasena codificada
    //lo correcto es compararla con un pass almacenado por un usuario
    @Override
    public UserDto authenticate(CredentialsDto credentialsDto) {
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), encodedMasterPassword)) {
            return new UserDto(1L, "Elkin", "Imbachi", "login", "token");
        }
        throw new RuntimeException("Invalid password");
    }

    @Override
    public UserDto findByLogin(String login) {
        if ("login".equals(login)) {
            return new UserDto(1L, "Elkin", "Imbachi", "login", "token");
        }
        throw new RuntimeException("Invalid login");
    }

    //creando token con la concatenacion del id del usuario, el login y la firma
    //se usara Hmac, que es codigo de autenticacion de mensajes en clave-hash
    @Override
    public String createToken(UserDto user) {
        return user.getId() + "&" + user.getLogin() + "&" + calculateHmac(user);
    }

    //para encontrar un usuario de un token, primero se divide el token en las 3 partes para
    //obtener el id, login o name
    @Override
    public UserDto findByToken(String token) {
        String[] parts = token.split("&");

        Long userId = Long.valueOf(parts[0]);
        String login = parts[1];
        String hmac = parts[2];

        UserDto userDto = findByLogin(login);

        //validar la firma, de lo contrario significa que el token no corresponde a este usuario
        if (!hmac.equals(calculateHmac(userDto)) || userId != userDto.getId()) {
            throw new RuntimeException("Invalid Cookie value");
        }

        return userDto;
    }

    //calculando el Hmac
    @Override
    public String calculateHmac(UserDto user) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(user.getId() + "&" + user.getLogin()).getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
