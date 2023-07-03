package ru.clevertec.userservice.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import ru.clevertec.userservice.service.JwtUtils;
import ru.clevertec.userservice.service.JwtProvider;
import org.springframework.web.filter.GenericFilterBean;
import ru.clevertec.userservice.dto.JwtAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * <p> User authentication class. </p>
 *
 * @author Artur Malashkov
 * @see JwtProvider
 * @see JwtUtils
 * @since 17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    /**
     * Validates the request header and creates an authentication object in the SecurityContext.
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            Claims claims = jwtProvider.getAccessClaims(token);
            JwtAuthentication jwtAuthentication = JwtUtils.generate(claims);
            jwtAuthentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * This method receives the token from the authorization header as a String.
     *
     * @param httpServletRequest -  http Servlet Request
     * @return token as a String
     */
    private String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
