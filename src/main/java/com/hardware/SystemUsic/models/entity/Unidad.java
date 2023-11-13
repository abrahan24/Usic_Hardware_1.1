package com.hardware.SystemUsic.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unidad")
@Setter
@Getter
public class Unidad implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_unidad;
    private String unidad;
    private String estado;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidad", fetch = FetchType.LAZY)
	private List<Persona> personas;

   

    public Long getId_unidad() {
        return id_unidad;
    }


    public void setId_unidad(Long id_unidad) {
        this.id_unidad = id_unidad;
    }


    public String getUnidad() {
        return unidad;
    }


    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }


    public List<Persona> getPersonas() {
        return personas;
    }


    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }

}
