package org.example.msvcauth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoResponse {
   private String id;
   private String  nombre;
   private String  apellidos;
   private String  email;
   private String  telefono;
   private String  direccion;
   private Double  salario;
   private String  cargo;

}
