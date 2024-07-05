package com.hardware.SystemUsic.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.entity.Servicio;
import com.hardware.SystemUsic.models.entity.Usuario;
import com.hardware.SystemUsic.models.service.IServicioService;
import com.hardware.SystemUsic.models.service.IUtilidadesService.IUtilidadesServices;

import net.sf.jasperreports.engine.JRException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping(value = "/hardware-servicio")
public class ReportesController {

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IUtilidadesServices utilidadesServices;

    @GetMapping("/reporte")
    public String reporte(Model model,HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            model.addAttribute("gestiones", servicioService.findAll());
        List<Servicio> servicios = servicioService.findAll();

        List<Integer> años = servicios.stream()
                .map(gestion -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(gestion.getFecha_recepcion());
                    return calendar.get(Calendar.YEAR);
                })
                .distinct() // Si solo quieres años únicos
                .collect(Collectors.toList());

        model.addAttribute("anos", años);

            return "REPORTES/reporte";
        }else{
            return "redirect:/hardware/login";
        }

    }

    @GetMapping("/reporte_personalizado")
    public String reporte_personalizado(Model model) {

        return "REPORTES/reporte";
    }


    @PostMapping(value ="/report_general")
    public ResponseEntity<ByteArrayResource> report_fechas(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_General_SATH.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte General (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }
    

}
