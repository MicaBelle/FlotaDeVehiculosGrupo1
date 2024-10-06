package com.gifa_api.service;

import com.gifa_api.dto.mantenimiento.*;
import com.gifa_api.model.ItemDeInventario;
import com.gifa_api.model.Mantenimiento;

import java.util.List;

public interface IMantenimientoService {

    void crearMantenimiento(RegistrarMantenimientoDTO registrarMantenimientoDTO);
    MantenimientosResponseDTO verMantenimientosPorVehiculo(Integer id);

    MantenimientosPendientesResponseDTO verMantenimientosPendientes();

    MantenimientosResponseDTO verMantenimientos();

    void asignarMantenimiento(Integer mantenimientoId, AsignarMantenimientoRequestDTO asignarMantenimientoRequestDTO);

    void finalizarMantenimiento(Integer mantenimientoId);

    MantenimientosResponseDTO obtenerMantenimientosPorOperador(Integer idOperador);
}
