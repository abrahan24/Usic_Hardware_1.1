package com.hardware.SystemUsic.controllers;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Arrays;

import org.checkerframework.checker.units.qual.m;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    // @RequestMapping(value = "/activos/{id_almacen}")
	// public String getContent1(@PathVariable(value = "id_almacen")Long [] id_almacen, Model model, HttpServletRequest request){
	
    //     System.out.println(id_almacen);
    //     List<Long> idsSeleccionados; 

    //     for (int i = 0; i < id_almacen.length; i++) {
    //         idsSeleccionados = Arrays.asList(id_almacen[i]);
    //         System.out.println(idsSeleccionados.get(i));
    //         model.addAttribute("activos", almacenService.Lista_Activos_Por_Id(idsSeleccionados));
    //         System.out.println(almacenService.Lista_Activos_Por_Id(idsSeleccionados));

    //     }
       
	// 	return "content :: content2";

	// }

    @RequestMapping(value = "/activos")
    public String getContent1(@RequestParam(value = "selectedValues", required = false) String selectedValues, Model model, HttpServletRequest request) {
        if (selectedValues != null) {
            // Dividir la cadena de valores separados por comas en un array de Long
            String[] selectedValuesArray = selectedValues.split(",");
            Long[] id_almacen = new Long[selectedValuesArray.length];

            for (int i = 0; i < selectedValuesArray.length; i++) {
                id_almacen[i] = Long.parseLong(selectedValuesArray[i]);
            }

            List<Long> idsSeleccionados = Arrays.asList(id_almacen);
            model.addAttribute("activos", almacenService.Lista_Activos_Por_Id(idsSeleccionados));
        }

        return "content :: content2";
    }
}