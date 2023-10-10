package com.hardware.SystemUsic.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.Baja;
import com.hardware.SystemUsic.models.entity.DetalleBaja;
import com.hardware.SystemUsic.models.entity.FallasBaja;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.IBajaService;
import com.hardware.SystemUsic.models.service.IDetalleBajaService;
import com.hardware.SystemUsic.models.service.IFallaBajaService;
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

    @Autowired
    private IFallaBajaService fallaBajaService;


    @RequestMapping(value = "/informe_baja",method = RequestMethod.GET)
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
            model.addAttribute("activos_selec", almacenService.Lista_Activos_Por_Id(idsSeleccionados));
        }

        return "content :: content2";
    }


    // @RequestMapping(value = "/add_informe_baja",method = RequestMethod.POST)
    // public String Form_Informe_Baja(Model model,@Validated Baja baja,
    // @RequestParam(name = "id_persona",required = false)Long id_persona,
    // @RequestParam(name = "id_almacen",required = false)Long [] id_almacen,
    // @RequestParam(name = "id_fallaBaja",required = false)Long [] id_fallaBaja,
    // RedirectAttributes flash, HttpServletRequest request ){

    //     if (request.getSession().getAttribute("persona") != null) {
            
    //         baja.setEstado_baja("A");
    //         baja.setPersona(personaService.findOne(id_persona));
    //         baja.setFecha_baja(new Date());
    //         baja.setObservacion_baja("ads");
    //         bajaService.save(baja);

    //         if (id_almacen != null) {
    //             for (int i = 0; i < id_almacen.length; i++) {
                
    //             if (id_fallaBaja != null) {
    //                     for (int j = 0; j < id_fallaBaja.length; j++) {
    //                         DetalleBaja detalleBaja = new DetalleBaja();
    //                         detalleBaja.setAlmacen(almacenService.findOne(id_almacen[i]));
    //                         detalleBaja.setBaja(bajaService.findOne(baja.getId_baja()));
    //                         detalleBaja.setEstado_detalleBaja("A");
    //                         detalleBaja.setFallaBaja(fallaBajaService.findOne(id_fallaBaja[i]));
    //                         System.out.println(id_fallaBaja[j]);
    //                         detalleBajaService.save(detalleBaja);
    //                 }
    //             }
    //         }
    //         }
            
            
    //         return "redirect:/hardware-servicio/informe_baja";
	// 	} else {
	// 		return "redirect:/hardware/login";
	// 	} 
    // }

   
    // @RequestMapping(value = "/add_informe_baja", method = RequestMethod.POST)
    // public String Form_Informe_Baja(
    //         Model model,
    //         @Validated Baja baja,
    //         @RequestParam(name = "id_persona", required = false) Long id_persona,
    //         @RequestParam(name = "id_almacen", required = false) Long[] id_almacen,
    //         @RequestParam(name = "id_fallaBaja", required = false) Long [] id_fallaBaja,
    //         RedirectAttributes flash,
    //         HttpServletRequest request
    // ) {
    //     if (request.getSession().getAttribute("persona") != null) {
    //         baja.setEstado_baja("A");
    //         baja.setPersona(personaService.findOne(id_persona));
    //         baja.setFecha_baja(new Date());
    //         baja.setObservacion_baja("ads");
    //         bajaService.save(baja);

    //         if (id_almacen != null ) {
    //             for (int i = 0; i < id_almacen.length; i++) {
    //             DetalleBaja detalleBaja = new DetalleBaja();
    //             detalleBaja.setAlmacen(almacenService.findOne(id_almacen[i]));
    //             detalleBaja.setBaja(bajaService.findOne(baja.getId_baja()));
    //             detalleBaja.setEstado_detalleBaja("A");
    //             detalleBaja.setFallaBaja(null);
    //             detalleBajaService.save(detalleBaja);
    //             }
    //         }
            

    //         return "redirect:/hardware-servicio/informe_baja";
    //     } else {
    //         return "redirect:/hardware/login";
    //     }
    // }

    
}

