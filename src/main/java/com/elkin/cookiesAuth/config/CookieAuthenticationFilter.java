package com.elkin.cookiesAuth.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthenticationFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "auth_by_cookie";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //se necesita nombrar la cookie y luego buscarla
        Optional<Cookie> cookieAuth = Stream.of(Optional.ofNullable(httpServletRequest.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        if (cookieAuth.isPresent()) {
            //si esta presente, almaceno el valor en el security context que se leera mas tarde
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(cookieAuth.get().getValue(), null));
        }
        //llamar a los filtros restantes al final, muy importante
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
