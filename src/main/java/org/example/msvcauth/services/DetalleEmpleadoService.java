package org.example.msvcauth.services;

import org.example.msvcauth.domain.input.DetalleEmpleadoInput;
import org.example.msvcauth.repositories.DetalleEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.msvcauth.domain.DetalleEmpleado;



@Service
public class DetalleEmpleadoService {
    private final DetalleEmpleadoRepository detalleEmpleadoRepository;

    @Autowired
    public DetalleEmpleadoService( DetalleEmpleadoRepository detalleEmpleadoRepository){
        this.detalleEmpleadoRepository = detalleEmpleadoRepository;
    }

    public DetalleEmpleado crearDetalleEmpleado(DetalleEmpleadoInput detalleEmpleadoInput){
        DetalleEmpleado  detalleEmpleado= new DetalleEmpleado();
        detalleEmpleado.setSalario(detalleEmpleadoInput.getSalario());
        detalleEmpleado.setCargo(detalleEmpleadoInput.getCargo());
        return detalleEmpleadoRepository.save(detalleEmpleado);
    }


}
