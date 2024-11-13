package org.example.msvcauth.domain.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
    String nombre;
    String apellidos;
    String email;
    String telefono;
    String direccion;
    String password;
    String tipoUsuario;

}
