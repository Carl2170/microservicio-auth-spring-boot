package org.example.msvcauth.controller;

import org.example.msvcauth.authentication.JwtUtils;
import org.example.msvcauth.domain.AuthResponse;
import org.example.msvcauth.domain.DetalleEmpleado;
import org.example.msvcauth.domain.EmpleadoResponse;
import org.example.msvcauth.domain.User;
import org.example.msvcauth.domain.input.DetalleEmpleadoInput;
import org.example.msvcauth.domain.input.EmpleadoInput;
import org.example.msvcauth.domain.input.UserInput;
import org.example.msvcauth.repositories.DetalleEmpleadoRepository;
import org.example.msvcauth.repositories.EmpleadoRepository;
import org.example.msvcauth.repositories.UserRepository;
import org.example.msvcauth.services.DetalleEmpleadoService;
import org.example.msvcauth.services.EmpleadoService;
import org.example.msvcauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private UserService userService;
    @Autowired
    private DetalleEmpleadoService detalleEmpleadoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @MutationMapping
    public User registrarEmpleado(@Argument String nombre,
                                      @Argument String apellidos,
                                      @Argument String email,
                                      @Argument String telefono,
                                      @Argument String direccion,
                                      @Argument String password,
                                      @Argument String tipoUsuario,
                                      @Argument Double salario,
                                      @Argument String cargo){
        if (userService.findByEmail(email) != null) {
            throw new IllegalArgumentException("El empleado ya está registrado");
        }

        UserInput input = new UserInput(nombre, apellidos,email,telefono, direccion, password, tipoUsuario);
        DetalleEmpleadoInput detalleEmpleadoInput= new DetalleEmpleadoInput(salario,cargo);
        User user = userService.register(input);
        DetalleEmpleado detalleEmpleado = detalleEmpleadoService.crearDetalleEmpleado(detalleEmpleadoInput);
        User empleado = empleadoService.crearEmpleado(user,detalleEmpleado);
        return empleado;
    }

    @QueryMapping
    public List<User> obtenerEmpleados(){
        return empleadoService.findAllEmpleados();
    }
    @MutationMapping
    public EmpleadoResponse modificarEmpleado(@Argument String id, @Argument EmpleadoInput empleadoInput) {
        return empleadoService.modificarEmpleado(id, empleadoInput);
    }

    @MutationMapping
    public String eliminarEmpleado(@Argument String id) {
        try {
            empleadoService.eliminarEmpleado(id);
            return "Empleado con ID " + id + " ha sido eliminado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}
