package com.phosservice.Phosservice.Security;

import com.phosservice.Phosservice.Repository.JwtTokenDao;
import com.phosservice.Phosservice.Security.pojo.RequestScopeContext;
import com.phosservice.Phosservice.Security.pojo.SqlUserDetails;
import com.phosservice.Phosservice.Tables.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@Primary
public class JwtTokenProvider {

    private static final String AUTH = "auth";
    private static final String FullName = "fullname";
    private static final String AUTHORIZATION = "Authorization";
    private String secretKey = "qewrqwer-9yupoqsiyd983409234";

    private static final long validityInMilliseconds = 3600000 * 24 * 7;

    @Autowired
    private JwtTokenDao jwtTokenDao;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createJwtToken(String userName, String fullName) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(AUTH, userName);
        claims.put(FullName, fullName);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        jwtTokenDao.save(new JwtToken(token));
        return token;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    public boolean validateToken(String token) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token);
        return true;
    }

    public boolean isTokenExistsInDb(String token) {
        return jwtTokenDao.existsById(token);
    }

    public String getUserName(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJwt(token).getBody().getSubject();
    }

    public String getFullName(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJwt(token).getBody().get(FullName).toString();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public RequestScopeContext getRequestScopeContext(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return new RequestScopeContext(userDetails.getUsername());
    }

    public String getAuthMail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token)
                .getBody().get(AUTH).toString();
    }

    private UserDetails getUserDetails(String token) {
        String userName = getUserName(token);
        String authMail = getAuthMail(token);
        String fullName = getFullName(token);

        return new SqlUserDetails(userName, authMail, fullName);
    }

    public String createNewTokenFromExistingToken(String token) {
        String userName = getUserName(token);
        String fullName = getFullName(token);
        return createJwtToken(userName, fullName);
    }


}
