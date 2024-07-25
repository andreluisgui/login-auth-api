package com.example.login_auth_api.infra.security;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.exceptions.UserNotAuthenticatedException;
import com.example.login_auth_api.exceptions.UserNotFoundException;
import com.example.login_auth_api.repositories.UserRepository;
import com.example.login_auth_api.utils.requests.RestErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;
    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/register");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (EXCLUDED_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var token = this.recoverToken(request);
            var login = tokenService.validateToken(token);

            if (login != null) {
                User user = userRepository.findByEmail(login).orElseThrow(() -> new UserNotFoundException("User Not Found"));
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UserNotAuthenticatedException("User not authenticated");
            }
            filterChain.doFilter(request, response);
        } catch (UserNotAuthenticatedException | UserNotFoundException ex) {
            handleException(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (Exception ex) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private void handleException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        RestErrorMessage restErrorMessage = new RestErrorMessage(status, message);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(restErrorMessage));
    }
}