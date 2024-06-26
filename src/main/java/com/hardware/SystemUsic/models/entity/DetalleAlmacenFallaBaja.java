package com.hardware.SystemUsic.models.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "detalleAlmacenFallaBaja")
@Setter
@Getter
public class DetalleAlmacenFallaBaja implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalleAlmacenFallaBaja;
    private String estado_detalleAlmacenFallaBaja;
    private Date fecha_registroDetAlmacenBaja;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_detalleBaja")
    private DetalleBaja detalleBaja;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fallaBaja")
    private FallasBaja fallaBaja;


}
