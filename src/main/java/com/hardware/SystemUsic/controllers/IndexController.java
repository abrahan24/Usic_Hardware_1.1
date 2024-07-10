package com.hardware.SystemUsic.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.Metodos;
import com.hardware.SystemUsic.models.entity.DetalleFalla;
import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.entity.Servicio;
import com.hardware.SystemUsic.models.entity.TipoEquipo;
import com.hardware.SystemUsic.models.entity.Usuario;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.IDetalleFallaService;
import com.hardware.SystemUsic.models.service.IFallaService;
import com.hardware.SystemUsic.models.service.IPersonaService;
import com.hardware.SystemUsic.models.service.IProcedenciaService;
import com.hardware.SystemUsic.models.service.IServicioService;
import com.hardware.SystemUsic.models.service.ITipoEquipoService;
import com.hardware.SystemUsic.models.service.IUsuarioService;

@Controller
@RequestMapping("/hardware")
public class IndexController {

    @Autowired
    private IServicioService servicioService;
    @Autowired
    private IProcedenciaService procedenciaService;
    @Autowired
    private IDetalleFallaService detalleFallaService;
    @Autowired
    private IFallaService fallaService;
    @Autowired
    private IAlmacenService almacenService;
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private ITipoEquipoService tipoEquipoService;
    @Autowired
    private IUsuarioService usuarioService;
    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(Model model, @RequestParam String usuario,
            @RequestParam String contrasena, RedirectAttributes flash, HttpServletRequest request) {
        Usuario u = usuarioService.getUsuario(usuario, contrasena);

        // model.addAttribute("usuario", u);
        if (u != null && !u.getEstado().equals("X")) {
            HttpSession session = request.getSession(true);

            session.setAttribute("persona", u.getPersona());
            session.setAttribute("usuario", u);
            session.setAttribute("unidad", u.getPersona().getUnidad().getUnidad());
            // Hibernate.initialize(u.getPersona().getCargo());
            return "redirect:/hardware-servicio/inicio";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/")
    public String inicio(Model model,  @RequestParam(required = false) String validado){
        
        if (validado != null) {
            model.addAttribute("validado", validado);
        }
        model.addAttribute("tipoequipos", tipoEquipoService.findAll());

        return "inicio";
    }
    
    @RequestMapping(value = "/formCliente")
    public String formCliente(Model model){

        return "formCliente";
    }

    @RequestMapping(value = "/verificar/{id}")
    public String verificar_id(Model model , @PathVariable Long id,RedirectAttributes flash,@RequestParam(required = false) String succes){

        if (succes != null) {
            model.addAttribute("succes", succes);
        }
        model.addAttribute("tipoequipo", tipoEquipoService.findOne(id));
        return "verificar";
    }
    
    @PostMapping("/verificar")
    public String verificar(Model model, @RequestParam Long id_persona, @RequestParam Long id_tipoequipo,RedirectAttributes flash){
        Persona persona= personaService.findOne(id_persona);

        // Map<String, Object> requests = new HashMap<String, Object>();

        // requests.put("usuario", dato);

        // String url = "https://digital.uap.edu.bo/api/londra/api/londraPost/v1/personaLondra/obtenerDatos";

        // HttpHeaders headers = new HttpHeaders();

        // headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity<HashMap> req = new HttpEntity(requests, headers);

        // RestTemplate restTemplate = new RestTemplate();

        // ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

        if (persona!=null) {
            model.addAttribute("persona", persona);
        }else{
            model.addAttribute("aux", true);
            flash.addAttribute("succes", "CI Ingresado No Registrado!");
            return "redirect:/hardware/verificar/"+id_tipoequipo;
        }
        model.addAttribute("tipoequipo", tipoEquipoService.findOne(id_tipoequipo));
        model.addAttribute("almacenes", almacenService.getAllAlmacenTipoEquipo(id_tipoequipo));
        model.addAttribute("procedencias", procedenciaService.findAll());
        return "formCliente";
    }

    @PostMapping("/servicio")
    public String registrarServicio(Model model, @RequestParam(required = false) Long id_servicio,
            @Validated Persona persona, @RequestParam(required = false) String accesorio, @RequestParam Long id_almacen,
            @RequestParam(required = false) Long[] id_falla, @RequestParam Long id_procedencia,
            @RequestParam(name = "fecha_servicio",required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_servicio,
            RedirectAttributes flash) throws FileNotFoundException, IOException {

        if (persona.getId_persona() != null) {
            persona = personaService.findOne(persona.getId_persona());
        } else {
            personaService.save(persona);
        }
        Metodos metodos = new Metodos();

        Path rootPath = Paths.get("uploads/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();

        Servicio servicio;
        if (id_servicio != null) {
            servicio = servicioService.findOne(id_servicio);
        } else {
            servicio = new Servicio();
        }
        servicio.setPersona(persona);
        servicio.setFecha_recepcion(fecha_servicio);
        servicio.setEstado_servicio("P");
        servicio.setProcedencia(procedenciaService.findOne(id_procedencia));
        servicio.setAlmacen(almacenService.findOne(id_almacen));
        servicio.setTipoEquipo(almacenService.findOne(id_almacen).getTipoEquipo());
        servicio.setTiposervicio(null);

        if (accesorio != null) {
            servicio.setAccesorio("1");
        } else {
            servicio.setAccesorio("0");
        }
        servicioService.save(servicio);
        servicio.setQr("QR_" + servicio.getId_servicio() + ".png");
        metodos.QR("http://virtual.uap.edu.bo:9998/hardware/seguimiento/" + servicio.getId_servicio(),
                rootAbsolutPath.toString() + "//" + "QR_" + servicio.getId_servicio() + ".png");
        servicioService.save(servicio);

        if (id_falla != null) {
            for (int i = 0; i < id_falla.length; i++) {
                DetalleFalla detalleFalla = new DetalleFalla();
                detalleFalla.setFalla(fallaService.findOne(id_falla[i]));
                detalleFalla.setFecha_registro(new Date());
                detalleFalla.setServicio(servicio);
                detalleFallaService.save(detalleFalla);
            }
        }
        flash.addAttribute("validado",
                "Registro Enviado Con Exito!, Puede proceder a dejar el Equipo al Campus Universitario en la oficina USIC");
        if (id_servicio != null) {
            return "redirect:/hardware-servicio/";
        } else {
            return "redirect:/hardware/";
        }
    }

    @RequestMapping(value = "/almacen/{id_almacen}")
	public String getContent1(@PathVariable Long id_almacen, Model model, HttpServletRequest request){
	
		model.addAttribute("almacen", almacenService.findOne(id_almacen));
       
		return "content :: content1";
	}

    @RequestMapping(value = "/personas/{dato}/{id_tipoequipo}")
	public String getContent3(@PathVariable String dato,@PathVariable Long id_tipoequipo, Model model, HttpServletRequest request){
	
        TipoEquipo tipoEquipo = tipoEquipoService.findOne(id_tipoequipo);

        model.addAttribute("tipoEquipo", tipoEquipo);
		model.addAttribute("personas", personaService.getPersonas_Nombre_Or_Ci(dato));
       
		return "content :: content3";
	}

    @RequestMapping(value = "/seguimiento/{id_servicio}")
	public String seguimiento(@PathVariable Long id_servicio, Model model, HttpServletRequest request){
	
		model.addAttribute("servicio", servicioService.findOne(id_servicio));
       
		return "seguimiento";
	}

}

