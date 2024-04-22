package com.hardware.SystemUsic.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.Almacen;
import com.hardware.SystemUsic.models.entity.Cargo;
import com.hardware.SystemUsic.models.entity.Colaborador;
import com.hardware.SystemUsic.models.entity.DetalleFalla;
import com.hardware.SystemUsic.models.entity.Falla;
import com.hardware.SystemUsic.models.entity.FallaEquipo;
import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.entity.Servicio;
import com.hardware.SystemUsic.models.entity.Solucion;
import com.hardware.SystemUsic.models.entity.SolucionEquipo;
import com.hardware.SystemUsic.models.entity.TipoServicio;
import com.hardware.SystemUsic.models.entity.Unidad;
import com.hardware.SystemUsic.models.entity.Usuario;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.ICargoService;
import com.hardware.SystemUsic.models.service.IColaboradorService;
import com.hardware.SystemUsic.models.service.IDetalleFallaService;
import com.hardware.SystemUsic.models.service.IFallaEquipoService;
import com.hardware.SystemUsic.models.service.IFallaService;
import com.hardware.SystemUsic.models.service.IPersonaService;
import com.hardware.SystemUsic.models.service.IServicioService;
import com.hardware.SystemUsic.models.service.ISolucionEquipoService;
import com.hardware.SystemUsic.models.service.ISolucionService;
import com.hardware.SystemUsic.models.service.ITipoEquipoService;
import com.hardware.SystemUsic.models.service.ITipoServicioService;
import com.hardware.SystemUsic.models.service.IUnidadService;
import com.hardware.SystemUsic.models.service.IUsuarioService;

@Controller
@RequestMapping("/hardware-servicio")
public class servicioController {

    @Autowired
    private IUnidadService unidadService;
    @Autowired
    private ICargoService cargoService;
    @Autowired
    private ITipoEquipoService tipoEquipoService;
    @Autowired
    private IAlmacenService almacenService;
    @Autowired
    private IColaboradorService colaboradorService;
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private IServicioService servicioService;
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private ISolucionService solucionService;
    @Autowired
    private IFallaEquipoService  fallaEquipoService;
    @Autowired
    private IFallaService fallaService;
    @Autowired
    private ISolucionEquipoService solucionEquipoService;
    @Autowired
    private ITipoServicioService tipoServicioService;
    @Autowired
    private IDetalleFallaService detalleFallaService;

    @RequestMapping("/")
    public String servicios(Model model,RedirectAttributes flash, HttpServletRequest request , @RequestParam(name = "validado",required = false)String validado){

        if (request.getSession().getAttribute("persona") != null) {
			Persona persona = (Persona) request.getSession().getAttribute("persona");
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            
            persona = personaService.findOne(persona.getId_persona()); 
            usuario = usuarioService.findOne(usuario.getId_usuario());

            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
        
            // for (Previo previo : usuario.getPrevios()) {
            //     model.addAttribute("servicios" + previo.getId_previo() , servicioService.getAllServicioUsuario(previo.getId_previo()));
            //     System.out.println(servicioService.getAllServicioUsuario(previo.getId_previo()));
            // }
            model.addAttribute("previos", usuario.getPrevios());
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("tipoServicios", tipoServicioService.findAll());
            return "panel";
		} else {
			return "redirect:/hardware/login";
		}
	}

    @RequestMapping("/eliminar_servicio/{id_servicio}")
    public String eliminarServicios(Model model, @PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
			
            Servicio servicio = servicioService.findOne(id_servicio);
            servicio.setEstado(null);
            servicio.setEstado_servicio(null);
            servicioService.save(servicio);
            flash.addAttribute("validado", "Regisro Eliminado Con Exito!");
            
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/terminar_servicio/{id_servicio}")
    public String terminarServicios(Model model, @PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
			
            Servicio servicio = servicioService.findOne(id_servicio);
            servicio.setEstado("T");
            servicio.setEstado_servicio("T");
            servicio.setFecha_entrega(new Date());
            servicioService.save(servicio);
            flash.addAttribute("validado", "Servicio N°"+servicio.getId_servicio() +" Terminado Con Exito!");
            
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/aceptar_servicio/{id_servicio}")
    public String aceptarServicio(Model model,  @PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
			Persona persona = (Persona) request.getSession().getAttribute("persona");
            persona = personaService.findOne(persona.getId_persona());
            Servicio servicio = servicioService.findOne(id_servicio);
            servicio.setEstado("A");
            servicio.setEstado_servicio("A");

            servicioService.save(servicio);
            Colaborador colaborador = new Colaborador();
            colaborador.setPersona(persona);
            colaborador.setServicio(servicio);
            colaborador.setEstado("A");
            colaborador.setFecha_recepcion(new Date());
            colaboradorService.save(colaborador);

            flash.addAttribute("validado", "Recepcion Realizada Con Exito!");
            
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/ficha_tecnica/{id_servicio}")
    public String fichaTecnicaServicio(Model model, @PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
			
            flash.addAttribute("validado", "Recepcion Realizada Con Exito!");
            
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/add_equipo")
    public String add_Equipo(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null) {
                model.addAttribute("validado", validado);
            }
            model.addAttribute("almacen", new Almacen());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            
            return "add_Equipo";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/add_Equipo",method = RequestMethod.POST)
    public String add_Equipo_Almacen(Model model,@Validated Almacen almacen,@RequestParam("id_tipoequipo")Long id_tipoequipo,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            almacen.setEstado("A");
            almacen.setTipoEquipo(tipoEquipoService.findOne(id_tipoequipo));
            almacenService.save(almacen);
            flash.addAttribute("validado", "Equipo Agregado Con Exito!!");
            return "redirect:/hardware-servicio/add_equipo";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/lista_almacen")
    public String lista_Equipo(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }
    
            model.addAttribute("almacenes", almacenService.findAll());
            return "lista_Almacen";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-almacen/{id_almacen}")
    public String editar_Almacen_Equipo(Model model,@PathVariable("id_almacen")Long id_almacen, RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            Almacen almacen = almacenService.findOne(id_almacen);

            model.addAttribute("almacen", almacen);
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            return "add_Equipo";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/eliminar-almacen/{id_almacen}")
    public String eliminar_Almacen_Equipo(Model model,@PathVariable("id_almacen")Long id_almacen, RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            Almacen almacen = almacenService.findOne(id_almacen);
            
            almacen.setEstado("X");
            almacenService.save(almacen);
            flash.addAttribute("validado", "Equipo Eliminado Con Exito!!");

            return "redirect:/hardware-servicio/lista_almacen";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/add_Cargo_Unidad")
    public String add_Cargo_Unidad_Service(Model model,@RequestParam(name = "validado",required = false)String validado,@RequestParam(name = "validado_",required = false)String validado_, RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null || validado_ != null) {
                model.addAttribute("validado", validado);
                model.addAttribute("validado_", validado_);
            }
            
            model.addAttribute("cargo", new Cargo());
            model.addAttribute("unidad", new Unidad());
            return "add_Cargo_Unidad";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/add_soluciones")
    public String add_Soluciones(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
            
            model.addAttribute("solucion", new Solucion());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("solucionEquipo", new SolucionEquipo());
            return "add_Soluciones";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/add_solucion_equipo",method = RequestMethod.POST)
    public String add_Solucion(Model model,@RequestParam(name = "id_solucionEquipo",required = false)Long id_solucionEquipo,@RequestParam(name = "id_solucion",required = false)Long id_solucion,@RequestParam(name = "id_tipoequipo",required = false)Long id_tipoequipo,@RequestParam(name = "solucion",required = false)String solucion_nom,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            Solucion solucion = new Solucion();
            if (id_solucion != null) {
                solucion = solucionService.findOne(id_solucion);
            }
            solucion.setSolucion(solucion_nom);
            solucionService.save(solucion);
            
            SolucionEquipo solucionEquipo = new SolucionEquipo();
            if (id_solucionEquipo != null) {
                solucionEquipo = solucionEquipoService.findOne(id_solucionEquipo);
            }
            solucionEquipo.setEstado("A");
            solucionEquipo.setSolucion(solucionService.findOne(solucion.getId_solucion()));
            solucionEquipo.setTipoEquipo(tipoEquipoService.findOne(id_tipoequipo));
            solucionEquipoService.save(solucionEquipo);
            flash.addAttribute("validado", "Se ah Agregado Una Nueva Solución Con Exito!!");
            return "redirect:/hardware-servicio/add_soluciones";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-solucion/{id_solucionEquipo}")
    public String editar_Soluciones(Model model,@PathVariable("id_solucionEquipo")Long id_solucionEquipo,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            SolucionEquipo solucionEquipo = solucionEquipoService.findOne(id_solucionEquipo);
          
            model.addAttribute("solucion", solucionEquipo.getSolucion());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("solucionEquipo", solucionEquipo);
            return "add_Soluciones";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/eliminar-solucion/{id_solucionEquipo}")
    public String eliminar_Soluciones(Model model,@PathVariable("id_solucionEquipo")Long id_solucionEquipo,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            SolucionEquipo solucionEquipo = solucionEquipoService.findOne(id_solucionEquipo);
            solucionEquipo.setEstado("X");
            solucionEquipoService.save(solucionEquipo);
            flash.addAttribute("validado", "Se ah Eliminado Una Solución Con Exito!!");
            return "redirect:/hardware-servicio/lista-soluciones-equipos";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/lista-soluciones-equipos")
    public String lista_Soluciones(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
            
            model.addAttribute("solucionEquipo", solucionEquipoService.findAll());
            return "lista_Soluciones_Equipos";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/add_fallas")
    public String add_Fallas(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
            
            model.addAttribute("falla", new Falla());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("fallaequipo", new FallaEquipo());
            return "add_Fallas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-falla/{id_fallaequipo}")
    public String editar_Fallas(Model model, @PathVariable("id_fallaequipo") Long id_fallaequipo,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            FallaEquipo fallaEquipo = fallaEquipoService.findOne(id_fallaequipo);

            model.addAttribute("falla", fallaEquipo.getFalla());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("fallaequipo", fallaEquipo);
            return "add_Fallas";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/eliminar-falla/{id_fallaequipo}")
    public String eliminar_Fallas(Model model, @PathVariable("id_fallaequipo") Long id_fallaequipo,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            FallaEquipo fallaEquipo = fallaEquipoService.findOne(id_fallaequipo);

            fallaEquipo.setEstado("X");
            fallaEquipoService.save(fallaEquipo);
            flash.addAttribute("validado", "Se ah Eliminado Una Falla Con Exito!!");
            return "redirect:/hardware-servicio/lista-fallas-equipos";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_falla_equipo",method = RequestMethod.POST)
    public String add_Falla(Model model,@RequestParam(name = "id_falla",required = false)Long id_falla,@RequestParam("falla")String falla_nom,@RequestParam("id_tipoequipo")Long id_tipoequipo,@RequestParam(name = "id_fallaequipo",required = false)Long id_fallaequipo,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            Falla falla = new Falla();
            if (id_falla != null) {
                falla = fallaService.findOne(id_falla);
            }
            falla.setFalla(falla_nom);
            fallaService.save(falla);
            
            FallaEquipo fallaEquipo = new FallaEquipo();
            if (id_fallaequipo != null) {
                fallaEquipo = fallaEquipoService.findOne(id_fallaequipo);
            }
            fallaEquipo.setFalla(fallaService.findOne(falla.getId_falla()));
            fallaEquipo.setTipoEquipo(tipoEquipoService.findOne(id_tipoequipo));
            fallaEquipo.setEstado("A");
            fallaEquipoService.save(fallaEquipo);
            flash.addAttribute("validado", "Se ah Agregado Una Nueva Falla Con Exito!!");

            return "redirect:/hardware-servicio/add_fallas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/lista-fallas-equipos")
    public String lista_Fallas(Model model, @RequestParam(name = "validado", required = false) String validado,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }

            model.addAttribute("fallaEquipo", fallaEquipoService.findAll());
            return "lista_Fallas_Equipos";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/lista_cargos")
    public String lista_Cargo(Model model, @RequestParam(name = "validado", required = false) String validado,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }
            model.addAttribute("cargos", cargoService.findAll());
            return "lista_Cargo";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/lista_unidades")
    public String lista_Unidades(Model model, @RequestParam(name = "validado", required = false) String validado,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }
            model.addAttribute("unidades", unidadService.findAll());
            return "lista_Unidad_Funcional";
        } else {
            return "redirect:/hardware/login";
        }
    }

    

    @RequestMapping("/editar-unidad/{id_unidad}")
    public String editar_Unidad(Model model, @PathVariable("id_unidad") Long id_unidad, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Unidad unidad = unidadService.findOne(id_unidad);

            model.addAttribute("cargo", new Cargo());
            model.addAttribute("unidad", unidad);
            return "add_Cargo_Unidad";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/eliminar-unidad/{id_unidad}")
    public String eliminar_Unidad(Model model, @PathVariable("id_unidad") Long id_unidad, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Unidad unidad = unidadService.findOne(id_unidad);
            unidad.setEstado("X");
            unidadService.save(unidad);
            flash.addAttribute("validado", "Unidad Eliminado Con Exito!!");
            return "redirect:/hardware-servicio/lista_unidades";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/editar-cargo/{id_cargo}")
    public String editar_Cargo(Model model, @PathVariable("id_cargo") Long id_cargo, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Cargo cargo = cargoService.findOne(id_cargo);

            model.addAttribute("cargo", cargo);
            model.addAttribute("unidad", new Unidad());
            return "add_Cargo_Unidad";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/eliminar-cargo/{id_cargo}")
    public String eliminar_Cargo(Model model, @PathVariable("id_cargo") Long id_cargo, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Cargo cargo = cargoService.findOne(id_cargo);

            cargo.setEstado("X");
            cargoService.save(cargo);
            flash.addAttribute("validado", "Cargo Eliminado Con Exito!!");
            return "redirect:/hardware-servicio/lista_cargos";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_cargo", method = RequestMethod.POST)
    public String add_Cargo(Model model, @RequestParam(name = "id_cargo", required = false) Long id_cargo,
            @RequestParam("cargo") String cargo_nom, RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Cargo cargo = new Cargo();
            if (id_cargo != null) {
                cargo = cargoService.findOne(id_cargo);
            }
            cargo.setCargo(cargo_nom);
            cargo.setEstado("A");
            cargoService.save(cargo);
            flash.addAttribute("validado", "Cargo Agregado Con Exito!");

            return "redirect:/hardware-servicio/add_Cargo_Unidad";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_unidad", method = RequestMethod.POST)
    public String add_Unidad(Model model, @RequestParam(name = "unidad", required = false) String unidad_,
            @RequestParam(name = "id_unidad", required = false) Long id_unidad, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Unidad unidad = new Unidad();
            if (id_unidad != null) {
                unidad = unidadService.findOne(id_unidad);
            }
            unidad.setUnidad(unidad_);
            unidadService.save(unidad);
            flash.addAttribute("validado_", "Unidad Agregada Con Exito!");

            return "redirect:/hardware-servicio/add_Cargo_Unidad";
        } else {
            return "redirect:/hardware/login";
        }
    }

    

    @RequestMapping(value = "/add_colaborador", method = RequestMethod.POST)
    public String addColaborador(RedirectAttributes flash, HttpServletRequest request, @RequestParam("aux") Integer aux,
            @RequestParam("id_persona") Long id_persona) {

        if (request.getSession().getAttribute("persona") != null) {
            Long id_servicio = 0L;
            for (int i = 0; i < aux; i++) {
                if (request.getParameter("id_servicio" + i) != null) {
                    id_servicio = Long.parseLong(request.getParameter("id_servicio" + i));
                    System.out.println(id_servicio);
                }
            }

            Colaborador colaborador = new Colaborador();
            colaborador.setPersona(personaService.findOne(id_persona));
            colaborador.setServicio(servicioService.findOne(id_servicio));
            colaborador.setFecha_recepcion(new Date());
            colaborador.setEstado("B");
            colaboradorService.save(colaborador);

            flash.addAttribute("validado", "Colaborador Añadido Con Exito!");

            return "redirect:/hardware-servicio/";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/ficha-tecnica/{id_servicio}")
    public String fichatecnica(Model model , @PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request ){
        
         if (request.getSession().getAttribute("persona") != null) {
			model.addAttribute("servicio", servicioService.findOne(id_servicio));
            model.addAttribute("usuarios", usuarioService.findAll());
            return "ficha_tecnica";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-servicio/{id_servicio}")
    public String editarServicio(Model model,@PathVariable("id_servicio")Long id_servicio, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            Servicio servicio = servicioService.findOne(id_servicio);
            
			model.addAttribute("servicio", servicio);
            model.addAttribute("almacenes", almacenService.getAllAlmacenTipoEquipo(servicio.getTipoEquipo().getId_tipoequipo()));
            model.addAttribute("tipoequipo", tipoEquipoService.findOne(servicio.getTipoEquipo().getId_tipoequipo()));
           
            return "editar_servicio";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/edit_servicio",method = RequestMethod.POST)
    public String editar_Servicio(Model model,@RequestParam(name = "id_servicio",required = false)Long id_servicio ,
    @RequestParam(name = "id_falla",required = false)Long [] id_falla, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            
            Servicio servicio = servicioService.findOne(id_servicio);
            
            if (servicio.getDetalleFallas().size() > 0 && servicio.getDetalleFallas() != null) {
                for (DetalleFalla detalleFalla : servicio.getDetalleFallas()) {
                    detalleFalla.setEstado_detalleFalla("X");
                    detalleFallaService.save(detalleFalla);
                }
            }
            servicioService.save(servicio);
            if (id_falla != null) {
                
                for (int i = 0; i < id_falla.length; i++) {
                    DetalleFalla detalleFalla = new DetalleFalla();
                    detalleFalla.setServicio(servicio);
                    detalleFalla.setFalla(fallaService.findOne(id_falla[i]));
                    detalleFalla.setEstado_detalleFalla("A");
                    detalleFalla.setFecha_registro(new Date());
                    detalleFallaService.save(detalleFalla);
                }
            }
			
            flash.addAttribute("validado", "Se ah Editado Con Exito el Servicio N°"+servicio.getId_servicio());
           
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-perfil-usuario")
    public String editar_perfil_usuario(Model model,@RequestParam(name = "validado",required = false)String validado, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }

            return "editar_Perfil_Usuario";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/editar_perfil",method = RequestMethod.POST)
    public String editar_perfil(Model model,@RequestParam("id_persona")Long id_persona,@RequestParam("nombre")String nombre,@RequestParam("ap_paterno")String ap_paterno,@RequestParam("ap_materno")String ap_materno,@RequestParam("ci")String ci,@RequestParam("celular")Integer celular, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            Persona persona = personaService.findOne(id_persona);
            persona.setNombre(nombre);
            persona.setAp_paterno(ap_paterno);
            persona.setAp_materno(ap_materno);
            persona.setCi(ci);
            persona.setCelular(celular);
            personaService.save(persona);

            flash.addAttribute("validado", "Se actualizo los datos con exito!!, Porfavor volver a iniciar sesion...");
            return "redirect:/hardware-servicio/editar-perfil-usuario";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/editar_usuario",method = RequestMethod.POST)
    public String editar_usuario(Model model,@RequestParam("id_usuario")Long id_usuario,@RequestParam("usuario")String usuario,@RequestParam("contrasena")String contrasena, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            Usuario usuario2 = usuarioService.findOne(id_usuario);
            usuario2.setUsuario(usuario);
            usuario2.setContrasena(contrasena);
            usuarioService.save(usuario2);

            HttpSession session = request.getSession(false);
			session.invalidate();
			flash.addAttribute("validado", "Se actualizo los datos con exito!");
		
            return "redirect:/hardware/login";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/historial_servicio/{id_almacen}")
    public String historialServicio(Model model,@PathVariable("id_almacen")Long id_almacen, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            Almacen almacen = almacenService.findOne(id_almacen);
           
            model.addAttribute("servicios", almacen.getServicios());
            model.addAttribute("almacen", almacen);
            
            return "historial_servicio";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/add_TipoServicio",method = RequestMethod.POST)
    public String Seleccion_TipoServicio(RedirectAttributes flash, HttpServletRequest request, @RequestParam("aux")Integer aux, @RequestParam(name = "id_TipoServicio",required = false)Long id_TipoServicio){

        if (request.getSession().getAttribute("persona") != null) {
			Long id_servicio = 0L;
            for (int i = 0; i < aux; i++) {
                if (request.getParameter("id_ser" + i) != null) {
                    id_servicio=Long.parseLong(request.getParameter("id_ser" + i));
                    System.out.println(id_servicio);
                }
            }
            
            Persona persona = (Persona) request.getSession().getAttribute("persona");
            persona = personaService.findOne(persona.getId_persona());
            Servicio servicio = servicioService.findOne(id_servicio);


             if (id_TipoServicio.intValue() == 1) {
                servicio.setEstado_servicio("AS"); // AS = Activo Soporte Tecnico
            }if (id_TipoServicio.intValue() == 2) {
                servicio.setEstado_servicio("AP"); // AP = Activo Mantenimiento Preventivo
            }if (id_TipoServicio.intValue() == 3) {
                servicio.setEstado_servicio("A"); 
            }
            
            System.out.println(id_TipoServicio);

            servicio.setTiposervicio(tipoServicioService.findOne(id_TipoServicio));
            TipoServicio tipoServicio = tipoServicioService.findOne(id_TipoServicio);
            servicioService.save(servicio);
            Colaborador colaborador = new Colaborador();
            colaborador.setPersona(persona);
            colaborador.setServicio(servicio);
            colaborador.setEstado("A");
            colaborador.setFecha_recepcion(new Date());
            colaboradorService.save(colaborador);

            flash.addAttribute("validado", "Solicitud de Tipo: "+ tipoServicio.getNom_TipoServicio() + " Aceptado Con Exito!");
            
            return "redirect:/hardware-servicio/";
		} else {
			return "redirect:/hardware/login";
		} 
    }

    @RequestMapping("/cerrar_sesion")
    public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash){
        HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/hardware/login";
    }
    
}

