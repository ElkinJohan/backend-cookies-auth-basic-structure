package com.elkin.cookiesAuth.config;

import com.elkin.cookiesAuth.dto.CredentialsDto;
import com.elkin.cookiesAuth.dto.UserDto;
import com.elkin.cookiesAuth.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    //primero unyectamos el authenticacinService para validar informacion de autenticacion
    private final AuthenticationService authenticationService;

    public UserAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDto userDto = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            // authentication by username and password
            userDto = authenticationService.authenticate(
                    new CredentialsDto((String) authentication.getPrincipal(), (char[]) authentication.getCredentials()));
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            // authentication by cookie
            userDto = authenticationService.findByToken((String) authentication.getPrincipal());
        }

        if (userDto == null) {
            return null;
        }
        //si obtengo un usuaroi de lo metodos anteriores significa que la info entrante
        //esta asignando correctamente a un usuario existente, lo cual la request es correctamente autenticada
        //y se puede devolver el objto de autenticaciocn
        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
