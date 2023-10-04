package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.DetalleBaja;

public interface IDetalleBajaService {
    public List<DetalleBaja> findAll();

	public void save(DetalleBaja detalleBaja);

	public DetalleBaja findOne(Long id);

	public void delete(Long id);
}
