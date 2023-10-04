package com.hardware.SystemUsic.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.Almacen;
import com.hardware.SystemUsic.models.entity.Baja;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.IBajaService;
import com.hardware.SystemUsic.models.service.IDetalleBajaService;
import com.hardware.SystemUsic.models.service.IPersonaService;

@Controller
@RequestMapping("/hardware-servicio")
public class BajaController {
    
    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IAlmacenService almacenService;

    @Autowired
    private IDetalleBajaService detalleBajaService;

    @Autowired
    private IBajaService bajaService;

    @RequestMapping("/informe_baja")
    public String Informe_Baja(Model model, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            
            model.addAttribute("baja", new Baja());
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("activos", almacenService.findAll());
            
            return "informe_baja";
		} else {
			return "redirect:/hardware/login";
		}
    }
}