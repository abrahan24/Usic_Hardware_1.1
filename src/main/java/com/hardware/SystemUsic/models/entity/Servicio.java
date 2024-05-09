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
@Table(name = "servicio")
@Setter
@Getter
public class Servicio implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_servicio;
    private Date fecha_recepcion;
    private String estado;
    private String estado_servicio;
    private String accesorio;
    private String observacion;
    private String recomendacion;
    private String conclucion;
    private String qr;
    private String qr_servicio;
    private Date fecha_entrega;

//Tabla Persona   
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona")
    private Persona persona;
//Tabla Tipo Servicio   
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_TipoServicio")
    private TipoServicio tiposervicio;
//Tabla Tipo Equipo
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipoequipo")
    private TipoEquipo tipoEquipo;
//Tabla Satisfaccion
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_satisfaccion")
    private Satisfaccion satisfaccion;
//Tabla Procedencia
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_procedencia")
    private Procedencia procedencia;
//Tabla Almacen
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_almacen")
    private Almacen almacen;
;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicio", fetch = FetchType.LAZY)
	private List<Colaborador> colaboradores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicio", fetch = FetchType.LAZY)
	private List<DetalleFalla> detalleFallas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicio", fetch = FetchType.LAZY)
	private List<DetalleSolucion> detalleSolucions;

   
}
