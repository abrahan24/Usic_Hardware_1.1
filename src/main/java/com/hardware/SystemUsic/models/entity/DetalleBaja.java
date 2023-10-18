package com.hardware.SystemUsic.models.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "detalleBaja")
@Setter
@Getter
public class DetalleBaja implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalleBaja;
    private String estado_detalleBaja;
    private Date fecha_registro;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_baja")
    private Baja baja;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleBaja", fetch = FetchType.LAZY)
	private List<DetalleAlmacenFallaBaja> detalleAlmacenFallaBajas;

     
}
