package com.hardware.SystemUsic.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "previo")
@Setter
@Getter
public class Previo implements Serializable {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_previo;
    private String estado_previo;
    private Integer correlativo;

//Tabla Unidad
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad")
    private Unidad unidad;
//Tabla Procedencia
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_procedencia")
    private Procedencia procedencia;
//Tabla Usuario
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
    private Usuario usuario;


}
