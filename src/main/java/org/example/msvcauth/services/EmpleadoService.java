package org.example.msvcauth.services;

import org.example.msvcauth.domain.DetalleEmpleado;
import org.example.msvcauth.domain.EmpleadoResponse;
import org.example.msvcauth.domain.User;
import org.example.msvcauth.domain.input.EmpleadoInput;
import org.example.msvcauth.repositories.DetalleEmpleadoRepository;
import org.example.msvcauth.repositories.EmpleadoRepository;
import org.example.msvcauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmpleadoService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmpleadoRepository empleadoRepository;
    private final DetalleEmpleadoRepository detalleEmpleadoRepository;

    @Autowired
    public EmpleadoService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           EmpleadoRepository empleadoRepository,
                           DetalleEmpleadoRepository detalleEmpleadoRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.empleadoRepository = empleadoRepository;
        this.detalleEmpleadoRepository = detalleEmpleadoRepository;
    }
    public List<User> findAllEmpleados() {
        List<User> empleados = empleadoRepository.findAllByRole("ROLE_EMPLEADO");
        for (User empleado : empleados) {
            if (empleado.getDetalleEmpleado()!= null) {
                DetalleEmpleado detalle = detalleEmpleadoRepository
                        .findById(empleado.getDetalleEmpleado().getId())
                        .orElse(null);
                empleado.setDetalleEmpleado(detalle);
            }
        }
        return empleados;
    }

    public User crearEmpleado(User user, DetalleEmpleado detalleEmpleado) {
        detalleEmpleadoRepository.save(detalleEmpleado);
        user.setDetalleEmpleado(detalleEmpleado);
        return userRepository.save(user);
    }

    public EmpleadoResponse modificarEmpleado(String id, EmpleadoInput empleadoInput) {
        // Buscar al empleado por ID y rol "empleado", además que tenga detalleEmpleado no nulo
        Optional<User> optionalUser = empleadoRepository.findEmpleado(id);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Empleado no encontrado o el detalleEmpleado está vacío");
        }

        User user = optionalUser.get();

        // Aquí actualizamos los datos del empleado
        user.setNombre(empleadoInput.getNombre());
        user.setApellidos(empleadoInput.getApellidos());
        user.setEmail(empleadoInput.getEmail());
        user.setTelefono(empleadoInput.getTelefono());
        user.setDireccion(empleadoInput.getDireccion());
        user.getDetalleEmpleado().setSalario(empleadoInput.getSalario());
        user.getDetalleEmpleado().setCargo(empleadoInput.getCargo());

        // Guardamos los cambios en el repositorio
        userRepository.save(user);

        // Devolvemos la respuesta con los datos actualizados
        return new EmpleadoResponse(
                user.getId(),
                user.getNombre(),
                user.getApellidos(),
                user.getEmail(),
                user.getTelefono(),
                user.getDireccion(),
                user.getDetalleEmpleado().getSalario(),
                user.getDetalleEmpleado().getCargo()
        );
    }

    public void eliminarEmpleado(String id) {
        Optional<User> empleadoOpt = empleadoRepository.findEmpleado(id);

        if (empleadoOpt.isPresent()) {
            User empleado = empleadoOpt.get();
            empleadoRepository.delete(empleado);
        } else {
            throw new IllegalArgumentException("Empleado no encontrado o no cumple con los requisitos para ser eliminado.");
        }
    }
}


