package org.example.msvcauth.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
//    @Autowired
//    public JwtAuthorizationFilter(UserDetailsServiceInterface userService) {
//        this.userService = userService;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtUtils.HEADER_STRING);

        if (header == null || !header.startsWith(JwtUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtUtils.HEADER_STRING);
        if (token != null) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtils.key)
                    .build()
                    .parseClaimsJws(token.replace(JwtUtils.TOKEN_PREFIX, ""))
                    .getBody();

            String username = claims.getSubject();

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null);
            }
        }
        return null;
    }

}
