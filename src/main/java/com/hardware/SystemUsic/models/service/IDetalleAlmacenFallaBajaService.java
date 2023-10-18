package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.DetalleAlmacenFallaBaja;

public interface IDetalleAlmacenFallaBajaService {
    
    public List<DetalleAlmacenFallaBaja> findAll();

	public void save(DetalleAlmacenFallaBaja detalleAlmacenFallaBaja);

	public DetalleAlmacenFallaBaja findOne(Long id);

	public void delete(Long id);
}
