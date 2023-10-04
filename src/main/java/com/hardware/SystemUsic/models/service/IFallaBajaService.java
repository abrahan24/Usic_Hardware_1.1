package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.FallasBaja;

public interface IFallaBajaService {
    
     public List<FallasBaja> findAll();

	public void save(FallasBaja fallasBaja);

	public FallasBaja findOne(Long id);

	public void delete(Long id);
}
