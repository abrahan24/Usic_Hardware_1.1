package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.GradoAcademico;

public interface IGradoAcademicoService {
    public List<GradoAcademico> findAll();

	public void save(GradoAcademico gradoAcademico);

	public GradoAcademico findOne(Long id);

	public void delete(Long id);
}
