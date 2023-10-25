package com.hardware.SystemUsic.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.DetalleSolucion;
import com.hardware.SystemUsic.models.entity.Servicio;
import com.hardware.SystemUsic.models.service.IBajaService;
import com.hardware.SystemUsic.models.service.IDetalleSolucionService;
import com.hardware.SystemUsic.models.service.IServicioService;
import com.hardware.SystemUsic.models.service.ISolucionService;
import com.hardware.SystemUsic.models.service.ITipoEquipoService;

@Controller
@RequestMapping("/hardware-servicio")
public class InformeController {

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ITipoEquipoService tipoEquipoService;

    @Autowired
    private IBajaService bajaService;

    @Autowired
    private ISolucionService solucionService;

    @Autowired
    private IDetalleSolucionService detalleSolucionService;
    
    @RequestMapping("/informe_soporte_tec/{id_servicio}")
    public String informe_Soporte_Tec(Model model, @PathVariable("id_servicio") Long id_servicio,
            RedirectAttributes flash, HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            model.addAttribute("tipoequipo", tipoEquipoService.findOne(servicio.getTipoEquipo().getId_tipoequipo()));
            model.addAttribute("servicio", servicio);

            return "informe_soporte_tec";
        } else {
            return "redirect:/hardware/login";
        }
    }
    
    @RequestMapping("/informe_preventivo/{id_servicio}")
    public String informe_Preventivo(Model model, @PathVariable("id_servicio") Long id_servicio,
            RedirectAttributes flash, HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            model.addAttribute("tipoequipo", tipoEquipoService.findOne(servicio.getTipoEquipo().getId_tipoequipo()));
            model.addAttribute("servicio", servicio);

            return "informe_preventivo";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/informe_tecnico/{id_servicio}")
    public String informeTecnico(Model model, @PathVariable("id_servicio") Long id_servicio, RedirectAttributes flash,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            model.addAttribute("tipoequipo", tipoEquipoService.findOne(servicio.getTipoEquipo().getId_tipoequipo()));
            model.addAttribute("servicio", servicio);

            return "informe_tecnico";
        } else {
            return "redirect:/hardware/login";
        }
    }

    
    @RequestMapping("/editar-informe_tecnico/{id_servicio}")
    public String Editar_informe_Tecnico(Model model, @PathVariable("id_servicio") Long id_servicio,
            RedirectAttributes flash, HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            /* model.addAttribute("detalleSolucion", new DetalleSolucion()); */
            model.addAttribute("tipoequipo", tipoEquipoService.findOne(servicio.getTipoEquipo().getId_tipoequipo()));
            model.addAttribute("servicio", servicio);

            if (servicio.getTiposervicio() != null && servicio.getTiposervicio().getId_TipoServicio() == 1) {
                return "informe_soporte_tec";
            } else {
                if (servicio.getTiposervicio() != null && servicio.getTiposervicio().getId_TipoServicio() == 2) {
                    return "informe_preventivo";
                }else{
                    return "informe_tecnico";
                }
            }
        } else {
            return "redirect:/hardware/login";
        }
    }


    @RequestMapping(value = "/add_informe_soporte_tec", method = RequestMethod.POST)
    public String informeTecnico_Soporte_Tec(Model model,
            @RequestParam(name = "observacion", required = false) String observacion,
            @RequestParam(name = "id_servicio", required = false) Long id_servicio,
            @RequestParam(name = "id_solucion", required = false) Long[] id_solucion,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            if (observacion != null && observacion.isEmpty()) {
                observacion = null;
            }

            if (observacion != null) {
                servicio.setObservacion(observacion);
            }

            if (servicio.getEstado() == "T") {
                servicio.setEstado("T");
            } else {
                if (servicio.getEstado() == "B") {
                    servicio.setEstado("B");
                }

            } // B = Estado En Proceso
            servicioService.save(servicio);

            if (servicio.getDetalleSolucions().size() > 0) {
                for (DetalleSolucion detalleSolucion : servicio.getDetalleSolucions()) {
                    detalleSolucion.setEstado("X");
                    detalleSolucionService.save(detalleSolucion);
                }
            }

            if (id_solucion != null) {
                for (int i = 0; i < id_solucion.length; i++) {
                    DetalleSolucion detalleSolucion = new DetalleSolucion();
                    detalleSolucion.setSolucion(solucionService.findOne(id_solucion[i]));
                    detalleSolucion.setServicio(servicioService.findOne(id_servicio));
                    detalleSolucion.setEstado("A");
                    detalleSolucionService.save(detalleSolucion);
                }
            }

            flash.addAttribute("validado", "Informe de Soporte Técnico N°"+servicio.getId_servicio()+" Realizado Con Exito!");

            return "redirect:/hardware-servicio/";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_informe_preventivo", method = RequestMethod.POST)
    public String informeTecnico_Preventivo(Model model, @RequestParam("observacion") String observacion,
            @RequestParam(name = "id_servicio", required = false) Long id_servicio,
            @RequestParam(name = "id_solucion", required = false) Long[] id_solucion,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);

            if (observacion != null && observacion.isEmpty()) {
                observacion = null;
            }

            if (observacion != null) {
                servicio.setObservacion(observacion);
            }

            if (servicio.getEstado() == "T") {
                servicio.setEstado("T");
            } else {
                if (servicio.getEstado() == "B") {
                    servicio.setEstado("B");
                }

            } // B = Estado En Proceso
            servicioService.save(servicio);

            if (servicio.getDetalleSolucions().size() > 0) {
                for (DetalleSolucion detalleSolucion : servicio.getDetalleSolucions()) {
                    detalleSolucion.setEstado("X");
                    detalleSolucionService.save(detalleSolucion);
                }
            }

            if (id_solucion != null) {
                for (int i = 0; i < id_solucion.length; i++) {
                    DetalleSolucion detalleSolucion = new DetalleSolucion();
                    detalleSolucion.setSolucion(solucionService.findOne(id_solucion[i]));
                    detalleSolucion.setServicio(servicioService.findOne(id_servicio));
                    detalleSolucion.setEstado("A");
                    detalleSolucionService.save(detalleSolucion);
                }
            }

            flash.addAttribute("validado", "Informe De Mantenimiento Preventivo N°"+servicio.getId_servicio()+" Realizado Con Exito!");

            return "redirect:/hardware-servicio/";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_informe", method = RequestMethod.POST)
    public String informeTecnicoServicio(Model model, @RequestParam("conclucion") String conclucion,
            @RequestParam("recomendacion") String recomendacion, @RequestParam("observacion") String observacion,
            @RequestParam(name = "id_servicio", required = false) Long id_servicio,
            @RequestParam(name = "id_solucion", required = false) Long[] id_solucion,
            @RequestParam(name = "id_detalleSolucion", required = false) Long id_detalleSolucion,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            Servicio servicio = servicioService.findOne(id_servicio);
            servicio.setConclucion(conclucion);
            servicio.setRecomendacion(recomendacion);
            servicio.setObservacion(observacion);
            if (servicio.getEstado() == "T") {
                servicio.setEstado("T");
            } else {
                if (servicio.getEstado() == "B") {
                    servicio.setEstado("B");
                }

            }
            servicioService.save(servicio);

            if (servicio.getDetalleSolucions().size() > 0) {
                for (DetalleSolucion detalleSolucion : servicio.getDetalleSolucions()) {
                    detalleSolucion.setEstado("X");
                    detalleSolucionService.save(detalleSolucion);
                }
            }

            if (id_solucion != null) {
                for (int i = 0; i < id_solucion.length; i++) {
                    DetalleSolucion detalleSolucion = new DetalleSolucion();
                    detalleSolucion.setSolucion(solucionService.findOne(id_solucion[i]));
                    detalleSolucion.setServicio(servicioService.findOne(id_servicio));
                    detalleSolucion.setEstado("A");
                    detalleSolucionService.save(detalleSolucion);
                }
            }
            if (servicio != null ) {
                flash.addAttribute("validado", "Informe de Mantenimiento Correctivo N°"+servicio.getId_servicio()+" Realizado Con Exito!");
            }

            return "redirect:/hardware-servicio/";
        } else {
            return "redirect:/hardware/login";
        }
    }
}
