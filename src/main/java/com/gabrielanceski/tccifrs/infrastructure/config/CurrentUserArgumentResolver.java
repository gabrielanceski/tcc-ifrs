package com.gabrielanceski.tccifrs.infrastructure.config;

import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserDetailsService userDetailsService;

    public CurrentUserArgumentResolver(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null && parameter.getParameterType().equals(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String username = jwt.getClaimAsString("sub");

            return userDetailsService.loadUserByUsername(username);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
