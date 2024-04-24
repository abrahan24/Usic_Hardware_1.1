package com.hardware.SystemUsic.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "persona")
@Setter
@Getter
public class Persona implements Serializable{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_persona;
    private String estado;
    private String ci;
    private String nombre;
    private String ap_paterno;
    private String ap_materno;
    private Integer celular;
    private String cod_funcionario;

//Tabla Cargo
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cargo")
    private Cargo cargo;
//Tabla Cargo
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gradoAcademico")
    private GradoAcademico gradoAcademico;
//Tabla Unidad
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", fetch = FetchType.LAZY)
	private List<Colaborador> colaboradors;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", fetch = FetchType.EAGER)
	private List<Usuario> usuarios;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", fetch = FetchType.LAZY)
	private List<Baja> bajas;

}
