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
@Table(name = "usuario")
@Setter
@Getter
public class Usuario implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    private String usuario;
    private String contrasena;
    private Character estado;
//Tabla Persona
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona")    
    private Persona persona;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<Previo> previos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<Baja> bajas;

}
