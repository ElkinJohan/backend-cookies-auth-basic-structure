package com.elkin.cookiesAuth.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
* En esta config se realiza un controlador de excepciones para que retorne un bonito Json en lugar de un stacktrace
* Config los filtros HTTP para manejar autenticacion basada en cookies y basada en username y pass
* Los enpoints protegidos y alguna confg basica
*/
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    public SecurityConfiguration(UserAuthenticationEntryPoint userAuthenticationEntryPoint) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //este es para capturar excepciones y devolver JSON bonito
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                //agregamos las autenticaciones por usuario y basada en cookies dentro de los fiiltros HTTP
                .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthenticationFilter(), UsernamePasswordAuthFilter.class)
                .csrf().disable()
                //no se quiere crear una sesion, ya que se quiere es una aplicacion sin stado STATELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //cuando hacemos logout elimino la cookie
                .and().logout().deleteCookies(CookieAuthenticationFilter.COOKIE_NAME)
                .and()
                .authorizeRequests()
                //enpoints deprotegidos:
                .antMatchers(HttpMethod.POST, "/v1/signIn", "/v1/signUp").permitAll()
                //los demas son con autenticacion
                .anyRequest().authenticated();
    }
}
