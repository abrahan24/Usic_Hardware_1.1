package com.hardware.SystemUsic.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.Baja;
import com.hardware.SystemUsic.models.entity.DetalleAlmacenFallaBaja;
import com.hardware.SystemUsic.models.entity.DetalleBaja;
import com.hardware.SystemUsic.models.entity.FallasBaja;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.IBajaService;
import com.hardware.SystemUsic.models.service.IDetalleAlmacenFallaBajaService;
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

    @Autowired
    private IDetalleAlmacenFallaBajaService detalleAlmacenFallaBajaService;


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


    @RequestMapping(value = "/add_informe_baja", method = RequestMethod.POST)
    public String Form_Informe_Baja(Model model, @Validated Baja baja,
            @RequestParam(name = "id_persona", required = false) Long id_persona,
            @RequestParam(name = "id_almacen", required = false) Long[] id_almacen,
            @RequestParam MultiValueMap<String, String> params, // Recibir todos los parámetros
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            baja.setEstado_baja("A");
            baja.setPersona(personaService.findOne(id_persona));
            baja.setFecha_baja(new Date());
            baja.setObservacion_baja("ads");
            bajaService.save(baja);

            if (id_almacen != null) {
                for (Long id : id_almacen) {
                    // Obtener los valores de los checkboxes asociados a este id_almacen
                    Long[] id_fallaBaja = params.get("id_fallaBaja_" + id)
                            .stream()
                            .map(Long::valueOf)
                            .toArray(Long[]::new);

                    DetalleBaja detalleBaja = new DetalleBaja();
                    detalleBaja.setAlmacen(almacenService.findOne(id));
                    detalleBaja.setEstado_detalleBaja("A");
                    detalleBaja.setBaja(baja);
                    detalleBaja.setFecha_registro(new Date());
                    detalleBajaService.save(detalleBaja);

                    if (id_fallaBaja != null) {
                        for (Long idFallaBaja : id_fallaBaja) {
                            DetalleAlmacenFallaBaja detalleAlmacenFallaBaja = new DetalleAlmacenFallaBaja();
                            detalleAlmacenFallaBaja.setEstado_detalleAlmacenFallaBaja("A");
                            detalleAlmacenFallaBaja.setFecha_registroDetAlmacenBaja(new Date());
                            detalleAlmacenFallaBaja.setDetalleBaja(detalleBaja);
                            detalleAlmacenFallaBaja.setFallaBaja(fallaBajaService.findOne(idFallaBaja));
                            detalleAlmacenFallaBajaService.save(detalleAlmacenFallaBaja);
                        }
                    }
                }
            }

            return "redirect:/hardware-servicio/informe_baja";
        } else {
            return "redirect:/hardware/login";
        }
    }
    

}

