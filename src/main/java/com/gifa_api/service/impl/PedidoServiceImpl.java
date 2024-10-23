package com.gifa_api.service.impl;

import com.gifa_api.dto.proveedoresYPedidos.CrearPedidoDTO;
import com.gifa_api.dto.proveedoresYPedidos.PedidoResponseDTO;
import com.gifa_api.exception.NotFoundException;
import com.gifa_api.model.*;
import com.gifa_api.repository.IPedidoRepository;
import com.gifa_api.repository.ItemDeInventarioRepository;
import com.gifa_api.service.IGestorOperacionalService;
import com.gifa_api.service.IPedidoService;
import com.gifa_api.service.IProveedorDeItemService;
import com.gifa_api.utils.enums.EstadoPedido;
import com.gifa_api.utils.mappers.PedidosMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {
    private final IPedidoRepository pedidoRepository;
    private final ItemDeInventarioRepository itemDeInventarioRepository;
    private final IGestorOperacionalService gestorOperacionalService;
    private final IProveedorDeItemService proveedorDeItemService;
    private final PedidosMapper pedidosMapper;

    @Override
    public void createPedido(CrearPedidoDTO pedidoManualDTO) {
        ItemDeInventario item = itemDeInventarioRepository.findById(pedidoManualDTO.getIdItem())
                .orElseThrow(() -> new NotFoundException("No se encontró el item con id: " + pedidoManualDTO.getIdItem()));
        Pedido pedido = Pedido
                .builder()
                .estadoPedido(EstadoPedido.PENDIENTE)
                .item(item)
                .cantidad(pedidoManualDTO.getCantidad())
                .fecha(LocalDate.now())
                .motivo(pedidoManualDTO.getMotivo())
                .build();
        pedidoRepository.save(pedido);

    }


    @Override
    public void hacerPedidos(Integer idItem) {
        Optional<ItemDeInventario> itemOptional = itemDeInventarioRepository.findById(idItem);
        ItemDeInventario item = itemOptional.get();
        GestorOperacional gestorOperacional = gestorOperacionalService.getGestorOperacional();
        int cantidad = item.getCantCompraAutomatica() + item.getUmbral();
        ProveedorDeItem proveerDeItemMasEconomico = proveedorDeItemService.proveedorMasEconomico(item.getId());

        if (item.getUmbral() > item.getStock()) {
            if ((proveerDeItemMasEconomico.getPrecio() * cantidad) < gestorOperacional.getPresupuesto()) {
                CrearPedidoDTO pedidoManualDTO = CrearPedidoDTO
                        .builder()
                        .idItem(item.getId())
                        .cantidad(cantidad)
                        .motivo("Solcitud de stock automatica")
                        .build();
                createPedido(pedidoManualDTO);

            }
        }
    }

    @Override
    public List<PedidoResponseDTO> obtenerPedidos() {
        return pedidosMapper.mapToPedidoDTO(pedidoRepository.findAll());
    }
}



