package com.hardware.SystemUsic.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.FallaEquipoBaja;
import com.hardware.SystemUsic.models.entity.FallasBaja;
import com.hardware.SystemUsic.models.service.IFallaBajaService;
import com.hardware.SystemUsic.models.service.IFallaEquipoBajaService;
import com.hardware.SystemUsic.models.service.ITipoEquipoService;

@Controller
@RequestMapping("/hardware-servicio")
public class FallaBajaController {

    @Autowired
    private IFallaBajaService fallaBajaService;

    @Autowired
    private IFallaEquipoBajaService fallaEquipoBajaService;

    @Autowired
    private ITipoEquipoService tipoEquipoService;
    
    @RequestMapping(value = "/add_fallas_baja", method = RequestMethod.GET)
    public String Vista_Agregar_Fallas_Bajas(Model model,@RequestParam(name = "validado",required = false)String validado ,RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
            
            model.addAttribute("falla_baja", new FallasBaja());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("falla_equipo_baja", new FallaEquipoBaja());
            
            return "add_Fallas_Bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/add_falla_equipo_baja", method = RequestMethod.POST)
    public String Form_Agregar_Fallas_Bajas(Model model,@Validated FallaEquipoBaja fallaEquipoBaja ,
    @Validated FallasBaja fallasBaja,@RequestParam(name = "id_tipoequipo",required = false)Long id_tipoequipo,RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            
            fallasBaja.setEstado_fallaBaja("A");
            fallasBaja.setFecha_registroFallaBaja(new Date());
            fallaBajaService.save(fallasBaja);

            fallaEquipoBaja.setFallaBaja(fallaBajaService.findOne(fallasBaja.getId_fallaBaja()));
            fallaEquipoBaja.setEstado_fallaEquipoBaja("A");
            fallaEquipoBaja.setTipoEquipo(tipoEquipoService.findOne(id_tipoequipo));
            fallaEquipoBajaService.save(fallaEquipoBaja);
            
            if (fallasBaja.getId_fallaBaja() == null && fallaEquipoBaja.getId_fallaEquipoBaja() == null) {
                flash.addAttribute("validado", "Se ah Agregado Una Nueva Falla Para Baja Con Exito!");
            }else{
                flash.addAttribute("validado", "Se ah Editado la Falla Para Baja Con Exito!");
            }
            
            return "redirect:/hardware-servicio/lista-fallas-bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/lista-fallas-bajas")
    public String lista_Fallas_Bajas(Model model,@RequestParam(name = "validado",required = false)String validado,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
        
            if (validado != null ) {
                model.addAttribute("validado", validado);
            }
            
            model.addAttribute("fallaEquipoBajas", fallaEquipoBajaService.findAll());
            return "lista_Fallas_Bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar-falla-baja/{id_fallaEquipoBaja}")
    public String editar_Fallas_Baja(Model model,@PathVariable("id_fallaEquipoBaja")Long id_fallaEquipoBaja,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            FallaEquipoBaja fallaEquipoBaja = fallaEquipoBajaService.findOne(id_fallaEquipoBaja);
          
            model.addAttribute("falla_baja", fallaEquipoBaja.getFallaBaja());
            model.addAttribute("tipoequipos", tipoEquipoService.findAll());
            model.addAttribute("falla_equipo_baja", fallaEquipoBaja);

            return "add_Fallas_Bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/eliminar-falla-baja/{id_fallaEquipoBaja}")
    public String eliminar_Fallas_Bajas(Model model,@PathVariable("id_fallaEquipoBaja")Long id_fallaEquipoBaja,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            FallaEquipoBaja fallaEquipoBaja = fallaEquipoBajaService.findOne(id_fallaEquipoBaja);

            fallaEquipoBaja.setEstado_fallaEquipoBaja("X");
            fallaEquipoBajaService.save(fallaEquipoBaja);

            flash.addAttribute("validado", "Se ah Eliminado Una Falla para Baja Con Exito!!");

            return "redirect:/hardware-servicio/lista-fallas-bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    
}
