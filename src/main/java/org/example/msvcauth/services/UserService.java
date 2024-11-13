package org.example.msvcauth.services;

import org.example.msvcauth.domain.*;
import org.example.msvcauth.domain.input.ClienteInput;
import org.example.msvcauth.domain.input.DetalleEmpleadoInput;
import org.example.msvcauth.domain.input.EmpleadoInput;
import org.example.msvcauth.domain.input.UserInput;
import org.example.msvcauth.exceptions.UserServiceException;
import org.example.msvcauth.repositories.ClienteRepository;
import org.example.msvcauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class UserService  implements UserDetailsServiceInterface {
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ClienteRepository clienteRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }


    public User register(UserInput userInput) {
        User user = new User();
        user.setNombre(userInput.getNombre());
        user.setApellidos(userInput.getApellidos());
        user.setEmail(userInput.getEmail());
        user.setTelefono(userInput.getTelefono());
        user.setDireccion(userInput.getDireccion());
        user.setImageUrl("https://res.cloudinary.com/dnkvrqfus/image/upload/v1700017356/user_zmcosz.jpg");
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));

        if(userInput.getTipoUsuario().equals("CLIENTE") ){
                user.setRoles(Arrays.asList(Role.ROLE_CLIENTE));
        }else if(userInput.getTipoUsuario().equals("EMPLEADO")) {
            user.setRoles(Arrays.asList(Role.ROLE_EMPLEADO));
        }else{
            user.setRoles(Arrays.asList(Role.ROLE_ADMIN));
        }
      //  user.setRoles(Arrays.asList(Role.ROLE_USER));
        return userRepository.save(user);
    }

    // Método para obtener un usuario por username
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException("Usuario no encontrado. id: " + id));
    }

    public List<User> getAllClientes(){
        List<User> clientes = clienteRepository.getAllClientes("ROLE_CLIENTE");
        return clientes;
    }

    public void eliminarCliente(String id) {
        Optional<User> clienteOpt = clienteRepository.findCliente(id);

        if (clienteOpt.isPresent()) {
            User cliente = clienteOpt.get();
            userRepository.delete(cliente);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado o no cumple con los requisitos para ser eliminado.");
        }
    }

    public ClienteResponse modificarCliente(String id, ClienteInput clienteInput) {
        System.out.println("cliente. "+ clienteInput);
        Optional<User> optionalUser = clienteRepository.findCliente(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado o el detalleEmpleado está vacío");
        }
        User user = optionalUser.get();

        // Aquí actualizamos los datos del cliente
        user.setNombre(clienteInput.getNombre());
        user.setApellidos(clienteInput.getApellidos());
        user.setEmail(clienteInput.getEmail());
        user.setTelefono(clienteInput.getTelefono());
        user.setDireccion(clienteInput.getDireccion());
        user.setPassword(clienteInput.getPassword());
        // Guardamos los cambios en el repositorio
        userRepository.save(user);

        // Devolvemos la respuesta con los datos actualizados
        return new ClienteResponse(
                user.getId(),
                user.getNombre(),
                user.getApellidos(),
                user.getEmail(),
                user.getTelefono(),
                user.getDireccion());
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
