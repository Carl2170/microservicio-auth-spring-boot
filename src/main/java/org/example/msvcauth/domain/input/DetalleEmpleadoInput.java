package org.example.msvcauth.domain.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleEmpleadoInput {
    private Double salario;
    private String cargo;
}
