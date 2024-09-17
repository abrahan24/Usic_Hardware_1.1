package com.hardware.SystemUsic.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.hardware.SystemUsic.models.service.ITipoEquipoService;
import com.hardware.SystemUsic.models.service.ITipoServicioService;
import com.hardware.SystemUsic.models.service.IUnidadService;
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

    @Autowired
    private IUnidadService unidadService;

    @Autowired
    private ITipoServicioService tipoServicioService;

    @Autowired
    private ITipoEquipoService tipoEquipoService;

    @GetMapping("/reporte")
    public String reporte(Model model, HttpServletRequest request) {

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
        } else {
            return "redirect:/hardware/login";
        }
    }

    @GetMapping("/reporte_estadistico")
    public String reporte_estadistico(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

            return "REPORTES/reporte_estadistico_usuario";
        } else {
            return "redirect:/hardware/login";
        }
    }

    @GetMapping("/reporte_personalizado")
    public String reporte_personalizado(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {

           model.addAttribute("unidades", unidadService.findAll());
           model.addAttribute("tipoServicios", tipoServicioService.findAll());
           model.addAttribute("tipoEquipos", tipoEquipoService.findAll());

            return "REPORTES/reporte_personalizado";
        } else {
            return "redirect:/hardware/login";
        }
    }


    @PostMapping(value ="/report_general")
    public ResponseEntity<ByteArrayResource> report_fechas(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_General_SATH.jrxml";

        Path projectPath = Paths.get("").toAbsolutePath();

        Path logoUAPPath = Paths.get(projectPath.toString(), "src", "main", "resources", "static", "images", "LOGO _UAP.png");
        String logoUAP = logoUAPPath.toString();

        Path logoUSICPath = Paths.get(projectPath.toString(), "src", "main", "resources", "static", "images", "usic2.png");
        String logoUSIC = logoUSICPath.toString();

        
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);
        parametros.put("logoUAP", logoUAP);
        parametros.put("logoUSIC", logoUSIC);

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
    
    @PostMapping(value ="/report_personalizado")
    public ResponseEntity<ByteArrayResource> report_personalizado(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,
    @RequestParam(name = "id_tipo_servicio",required = false)Long id_tipo_servicio,
    @RequestParam(name = "id_tipoequipo", required = false)Long id_tipoequipo,
    @RequestParam(name = "id_unidad",required = false)Long id_unidad,HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_Personalizado_SATH.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("id_tipo_servicio", id_tipo_servicio);
        parametros.put("id_tipoequipo", id_tipoequipo);
        parametros.put("id_unidad", id_unidad);
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte Personalizado (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @PostMapping(value ="/report_personalizado_2")
    public ResponseEntity<ByteArrayResource> report_personalizado_2(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,
    @RequestParam(name = "id_tipo_servicio",required = false)Long id_tipo_servicio,
    @RequestParam(name = "id_tipoequipo", required = false)Long id_tipoequipo,
    HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_Personalizado_SATH_2.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("id_tipo_servicio", id_tipo_servicio);
        parametros.put("id_tipoequipo", id_tipoequipo);
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte Personalizado (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @PostMapping(value ="/report_personalizado_3")
    public ResponseEntity<ByteArrayResource> report_personalizado_3(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,
    @RequestParam(name = "id_tipo_servicio",required = false)Long id_tipo_servicio,
    HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_Personalizado_SATH_3.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("id_tipo_servicio", id_tipo_servicio);
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte Personalizado (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @PostMapping(value ="/report_personalizado_4")
    public ResponseEntity<ByteArrayResource> report_personalizado_4(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,
    @RequestParam(name = "id_tipoequipo",required = false)Long id_tipoequipo,
    HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_Personalizado_SATH_4.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("id_tipoequipo", id_tipoequipo);
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte Personalizado (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }


    @PostMapping(value ="/report_estadistico")
    public ResponseEntity<ByteArrayResource> report_estadistico(@RequestParam(name = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_inicio,
    @RequestParam(name = "fecha_final")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fecha_final,
    @RequestParam(name = "id_usuario" ,required = false)Long id_usuario,HttpServletRequest request) throws IOException, JRException, SQLException {
       
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
       
        String nombreArchivo = "Reporte_Servicios_Usuario.jrxml";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id_usuario", usuario.getId_usuario());
        parametros.put("fecha_inicial", fecha_inicio);
        parametros.put("fecha_final", fecha_final);

        

        ByteArrayOutputStream stream = utilidadesServices.compilarAndExportarReporte(nombreArchivo,parametros);

        byte[] bytes = stream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(bytes);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline;filename=" + "Reporte Estadistico Usuario: "+usuario.getPersona().getNombre()+ " "+usuario.getPersona().getAp_paterno() +" (SATH)"
                                + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }
}
