package org.example.msvcauth.domain.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoInput {
    String nombre;
    String apellidos;
    String email;
    String telefono;
    String direccion;
    String password;
    Double salario;
    String cargo;
}
