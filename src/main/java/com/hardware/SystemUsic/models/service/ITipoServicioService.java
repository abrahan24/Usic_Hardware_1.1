package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.TipoServicio;

public interface ITipoServicioService {
    
    public List<TipoServicio> findAll();

	public void save(TipoServicio tipoServicio);

	public TipoServicio findOne(Long id);

	public void delete(Long id);
}
