package com.hardware.SystemUsic.controllers;

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

import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.service.ICargoService;
import com.hardware.SystemUsic.models.service.IPersonaService;
import com.hardware.SystemUsic.models.service.IUnidadService;

@Controller
@RequestMapping("/hardware-servicio")
public class PersonaController {
    
    @Autowired
    private IUnidadService unidadService;

    @Autowired
    private ICargoService cargoService;

    @Autowired
    private IPersonaService personaService;

    @RequestMapping("/add_Persona")
    public String add_Persona_Service(Model model, @RequestParam(name = "validado", required = false) String validado,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }
            model.addAttribute("persona", new Persona());
            model.addAttribute("unidades", unidadService.findAll());
            model.addAttribute("cargos", cargoService.findAll());

            return "add_Persona";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping(value = "/add_persona", method = RequestMethod.POST)
    public String add_Persona(Model model, @Validated Persona persona,
            @RequestParam(name = "id_unidad", required = false) Long id_unidad,
            @RequestParam(name = "id_cargo", required = false) Long id_cargo, RedirectAttributes flash,
            HttpServletRequest request) {
        
        if (request.getSession().getAttribute("persona") != null) {

            if (personaService.getCIpersona(persona.getCi()) == null) {
                // Si no existe, continúa con el proceso de guardar
                persona.setCargo(cargoService.findOne(id_cargo));
                persona.setUnidad(unidadService.findOne(id_unidad));
                personaService.save(persona);
                flash.addAttribute("validado", "Persona Agregada Con Éxito!");
            } else {
                flash.addAttribute("succes", "Ya existe una Persona con el mismo CI.");
            }
            return "redirect:/hardware-servicio/lista_personas";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/lista_personas")
    public String lista_Personas(Model model, @RequestParam(name = "validado", required = false) String validado,
        @RequestParam(name = "succes", required = false) String succes,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            if (validado != null) {
                model.addAttribute("validado", validado);
            }
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("personas", personaService.findAll());
            return "lista_Persona";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/editar-persona/{ip_persona}")
    public String editar_Persona(Model model, @PathVariable("ip_persona") Long ip_persona, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Persona persona = personaService.findOne(ip_persona);

            model.addAttribute("persona", persona);
            model.addAttribute("cargos", cargoService.findAll());
            model.addAttribute("unidades", unidadService.findAll());
            return "Edit_Persona";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @RequestMapping("/eliminar-persona/{ip_persona}")
    public String eliminar_Persona(Model model, @PathVariable("ip_persona") Long ip_persona, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            Persona persona = personaService.findOne(ip_persona);

            persona.setEstado("X");
            personaService.save(persona);
            flash.addAttribute("validado", "Persona Eliminada Con Exito!!");
            return "redirect:/hardware-servicio/lista_personas";
        } else {
            return "redirect:/hardware/login";
        }
    }
}
