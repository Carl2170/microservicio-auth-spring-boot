package org.example.msvcauth.controller;


import org.example.msvcauth.authentication.JwtUtils;
import org.example.msvcauth.domain.AuthResponse;
import org.example.msvcauth.domain.User;
import org.example.msvcauth.domain.input.UserInput;
import org.example.msvcauth.exceptions.MessageError;
import org.example.msvcauth.exceptions.UserNotRegisteredException;
import org.example.msvcauth.services.AuthService;
import org.example.msvcauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @MutationMapping
    public AuthResponse login(@Argument String email, @Argument String password) {

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UserNotRegisteredException("El usuario no está registrado");
        }

        String token = authService.login(email, password);
        return new AuthResponse(user, token);
    }

    @MutationMapping
    public AuthResponse register(@Argument String nombre,
                                 @Argument String apellidos,
                                 @Argument String email,
                                 @Argument String telefono,
                                 @Argument String direccion,
                                 @Argument String password,
                                 @Argument String tipoUsuario) {
        if (userService.findByEmail(email) != null) {
            throw new MessageError("El usuario ya está registrado");
        }

        UserInput input = new UserInput(nombre, apellidos, email,telefono, direccion, password, tipoUsuario);
        User user = userService.register(input);
        String token = JwtUtils.generateToken(user.getEmail());
        return new AuthResponse(user, token);
    }
}
