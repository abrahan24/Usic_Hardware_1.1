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
@Table(name = "detalleBaja")
@Setter
@Getter
public class DetalleBaja implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalleBaja;
    private String estado_detalleBaja;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_baja")
    private Baja baja;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_almacen")
    private Almacen almacen;

     //Tabla Falla Baja
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_fallaBaja")
     private FallasBaja fallaBaja ;
}
