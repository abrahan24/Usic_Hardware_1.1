package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.FallaEquipoBaja;

public interface IFallaEquipoBajaService {
    
    public List<FallaEquipoBaja> findAll();

	public void save(FallaEquipoBaja fallaEquipoBaja);

	public FallaEquipoBaja findOne(Long id);

	public void delete(Long id);
}
