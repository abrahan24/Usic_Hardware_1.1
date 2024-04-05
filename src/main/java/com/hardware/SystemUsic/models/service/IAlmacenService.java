package com.hardware.SystemUsic.models.service;

import java.util.List;

import com.hardware.SystemUsic.models.entity.Almacen;

public interface IAlmacenService {
    public List<Almacen> findAll();

	public void save(Almacen almacen);

	public Almacen findOne(Long id);

	public void delete(Long id);

	public List<Almacen>getAllAlmacenTipoEquipo(Long id_tipoEquipo);

	public List<Almacen>Lista_Activos_Por_Id(List<Long> id_almacenes);

	public List<Almacen>Lista_Activos_Cod_Equipo(String cod_equipo);

	public List<Almacen>Lista_Activos_Por_Codigo_Equipo(List<String> cod_equipo);


}
