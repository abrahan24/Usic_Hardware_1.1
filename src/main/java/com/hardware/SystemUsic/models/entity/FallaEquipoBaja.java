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
@Table(name = "fallaEquipoBaja")
@Setter
@Getter
public class FallaEquipoBaja implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_fallaEquipoBaja;
    private String estado_fallaEquipoBaja;

    //Tabla Tipo Equipo
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipoequipo")
    private TipoEquipo tipoEquipo ;

    //Tabla Falla Baja
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fallaBaja")
    private FallasBaja fallaBaja ;
}
