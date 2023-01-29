package com.phosservice.Phosservice.Security;

import com.phosservice.Phosservice.Exceptions.CustomException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = jwtTokenProvider.resolveToken(request);

        if(!token.isEmpty()){
            if(!jwtTokenProvider.isTokenExistsInDb(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
                throw new CustomException("Invalid JWT Token", HttpStatus.UNAUTHORIZED);
            }
            try {
                jwtTokenProvider.validateToken(token);
            } catch (JwtException | IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid Jwt Token");
                throw new CustomException("Invalid JWT Token", HttpStatus.UNAUTHORIZED);
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }
}
