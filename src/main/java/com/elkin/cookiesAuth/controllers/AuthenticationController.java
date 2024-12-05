package com.elkin.cookiesAuth.controllers;

import com.elkin.cookiesAuth.config.CookieAuthenticationFilter;
import com.elkin.cookiesAuth.dto.SignUpDto;
import com.elkin.cookiesAuth.dto.UserDto;
import com.elkin.cookiesAuth.services.AuthenticationService;
import com.elkin.cookiesAuth.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService,
                                    AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    //con @AuthenticationPrincipal inyectara al objeto usuario en mi controlador
    //si llego a este controlador y ninguna excepcion es capturada por el EntryPoint
    //significa que mi usuaro esta correctamente autenticado, asi que se crea la cookie
    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user,
                                          HttpServletResponse servletResponse) {

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, authenticationService.createToken(user));
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");

        servletResponse.addCookie(authCookie);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.signUp(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId() + "/profile")).body(createdUser);
    }

    //el ultimo paso, el cierre de sesion, debo limpiar el contexto de seguridad y eliminar la cookie
    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
