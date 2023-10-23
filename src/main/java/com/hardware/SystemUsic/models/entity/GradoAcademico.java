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
@Table(name = "gradoAcademico")
@Setter
@Getter
public class GradoAcademico implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_gradoAcademico;
    private String nom_gradoAcademico;
    private String sigla_gradoAcademico;
    private String estado_gradoAcademico;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradoAcademico", fetch = FetchType.LAZY)
	private List<Persona> personas;
}
