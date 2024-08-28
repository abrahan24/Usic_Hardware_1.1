package com.hardware.SystemUsic.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hardware.SystemUsic.models.entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long>{

    @Query("select p from Persona p where p.ci=?1")
    public Persona getCIpersona(String ci);

    @Query("SELECT p FROM Persona p WHERE p.nombre LIKE %?1% OR p.ci LIKE %?1% OR p.ap_paterno LIKE %?1% OR p.ap_materno LIKE %?1%")
    public List<Persona>getPersonas_Nombre_Or_Ci(String dato);

    @Query("SELECT p FROM Persona p WHERE p.ci LIKE CONCAT(:ci, '%')")
    public List<Persona> validarPersonaPorCI(String ci);
}