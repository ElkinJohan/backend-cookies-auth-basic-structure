package com.elkin.cookiesAuth.services;

import com.elkin.cookiesAuth.dto.CredentialsDto;
import com.elkin.cookiesAuth.dto.UserDto;

public interface AuthenticationService {
    UserDto authenticate(CredentialsDto credentialsDto);

    UserDto findByLogin(String login);

    String createToken(UserDto user);

    UserDto findByToken(String token);

    String calculateHmac(UserDto user);
}
