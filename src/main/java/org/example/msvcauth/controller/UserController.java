package org.example.msvcauth.controller;
import lombok.Cleanup;
import org.example.msvcauth.authentication.JwtUtils;
import org.example.msvcauth.domain.AuthResponse;
import org.example.msvcauth.domain.ClienteResponse;
import org.example.msvcauth.domain.EmpleadoResponse;
import org.example.msvcauth.domain.input.ClienteInput;
import org.example.msvcauth.domain.input.EmpleadoInput;
import org.example.msvcauth.domain.input.UserInput;
import org.example.msvcauth.exceptions.MessageError;
import org.example.msvcauth.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.example.msvcauth.domain.User;
import lombok.extern.java.Log;

import java.util.List;

@Log
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public User getUser(@Argument String id) {
        return userService.getUserById(id);
    }

    @QueryMapping
   // @PreAuthorize("hasRole('ROLE_USER')")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @QueryMapping
    public List<User> obtenerClientes(){
        return userService.getAllClientes();
    }
    @MutationMapping
    public String eliminarCliente(@Argument String id) {
        try {
            userService.eliminarCliente(id);
            return "Cliente con ID " + id + " ha sido eliminado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
    @MutationMapping
    public User registerCliente(
                                 @Argument String nombre,
                                 @Argument String apellidos,
                                 @Argument String email,
                                 @Argument String telefono,
                                 @Argument String direccion,
                                 @Argument String password,
                                 @Argument String tipoUsuario) {
        if (userService.findByEmail(email) != null) {
            throw new MessageError("El usuario ya está registrado");
        }

        UserInput input = new UserInput(nombre, apellidos, email,telefono, direccion, password, "CLIENTE");
        User user = userService.register(input);

        return user;
    }

    @MutationMapping
    public ClienteResponse modificarCliente(@Argument String id, @Argument ClienteInput clienteInput) {
        System.out.println("clienteInput "+clienteInput);
        return userService.modificarCliente(id, clienteInput);
    }

}
