package org.example.msvcauth.authentication;

import org.example.msvcauth.services.UserDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderManager implements  AuthenticationManager,UserDetailsService{
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final UserDetailsServiceInterface userService;

    @Autowired
    public AuthenticationProviderManager(UserDetailsServiceInterface  userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        this.daoAuthenticationProvider.setUserDetailsService(userService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return daoAuthenticationProvider.authenticate(authentication);
    }

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }

}
