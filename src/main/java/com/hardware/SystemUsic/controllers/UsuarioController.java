package com.hardware.SystemUsic.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.entity.Previo;
import com.hardware.SystemUsic.models.entity.Procedencia;
import com.hardware.SystemUsic.models.entity.Usuario;
import com.hardware.SystemUsic.models.service.IPersonaService;
import com.hardware.SystemUsic.models.service.IPrevioService;
import com.hardware.SystemUsic.models.service.IProcedenciaService;
import com.hardware.SystemUsic.models.service.IUnidadService;
import com.hardware.SystemUsic.models.service.IUsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/hardware-servicio")
public class UsuarioController {
    
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPrevioService previoService;

    @Autowired
    private IProcedenciaService procedenciaService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IUnidadService unidadService;

    @GetMapping("/usuario")
    public String usuario(Model model ,HttpServletRequest request) {

        if (request.getSession().getAttribute("persona") != null) {
            return "USUARIOS/vista_usuario";

        }else{
            return "redirect:/hardware/login";
        }

    }
    
    @PostMapping("/listaUsuarios")
    public String listaUsuarios(Model model) {
        
        model.addAttribute("usuarios", usuarioService.findAll());
        
        return "USUARIOS/lista_usuarios";
    }

    @PostMapping("/add_usuario")
    public String add_usuario(Model model) {
        
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("persona", new Persona());
        model.addAttribute("procedencias", procedenciaService.findAll());
        return "USUARIOS/formulario";
    }

    @GetMapping("/validarCI/{ci}")
    public ResponseEntity<?> validarCI(Model model, @PathVariable(name = "ci")String ci) {

        List<Persona> persona = personaService.validarPersonaPorCI(ci);

        if (persona.size() > 0) {
            return ResponseEntity.ok("ok");
        }else{
            return ResponseEntity.ok("error");
        }

    }
    
    
    @PostMapping("/usuario_form")
    public ResponseEntity<String> usuario_form(
            @RequestParam(name = "usuario") String usuari,
            @RequestParam(name = "contrasena")String contrasena,
            @RequestParam(name = "ci") String ci,
            @RequestParam(name = "previos") Long previos[],
            @RequestParam(name = "estado")Character estado) {

        try {

            if (usuarioService.validarExisteUsuarioPorCI(ci) != null) {
                return ResponseEntity.ok("error");
            }

            Usuario usuario = new Usuario();

            Persona persona = personaService.getCIpersona(ci);
            usuario.setUsuario(usuari);
            usuario.setContrasena(contrasena);
            usuario.setEstado(estado);
            if (persona != null) {
                usuario.setPersona(persona);
                usuarioService.save(usuario);
            } else {
                return ResponseEntity.ok("2");
            }

            usuarioService.save(usuario);

            for (int i = 0; i < previos.length; i++) {
                Previo previo = new Previo();
                previo.setEstado_previo("A");
                previo.setProcedencia(procedenciaService.findOne(previos[i]));
                previo.setUsuario(usuario);
                previo.setUnidad(unidadService.findOne(1L));
                previoService.save(previo);
            }

            return ResponseEntity.ok("1");
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }

    }
    
    @PostMapping("/editarUsuario/{id_usuario}")
    public String editarUsuario(Model model,@PathVariable(name = "id_usuario")Long id_usuario) {

        Usuario usuario = usuarioService.findOne(id_usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("persona", usuario.getPersona());

        List<Procedencia> procedencias = new ArrayList<>();

        for (Previo previo : usuario.getPrevios()) {
            // System.out.println(previo.getProcedencia().getProcedencia());
            procedencias.add(previo.getProcedencia());
        }
    
        model.addAttribute("procedencias", procedenciaService.findAll());
        model.addAttribute("edit", "true");
        // model.addAttribute("previos", procedencias);
        
        return "USUARIOS/formulario";
    }
    

    @PostMapping("/edit_usuario_form")
    public ResponseEntity<String> edit_usuario_form(@RequestParam(name = "id_usuario")Long id_usuario,
    @RequestParam(name = "usuario")String usuario,
    @RequestParam(name = "contrasena")String contrasena,
    @RequestParam(name = "ci")String ci,
    @RequestParam(name = "previos")Long previos [],
    @RequestParam(name = "estado")Character estado) {
        
        Usuario u = usuarioService.findOne(id_usuario);

        u.setUsuario(usuario);
        u.setContrasena(contrasena);

        Persona p = personaService.getCIpersona(ci);
        u.setPersona(p);
        u.setEstado(estado);

        for (Previo pre : u.getPrevios()) {
            pre.setEstado_previo("X");
            previoService.save(pre);
        }
        usuarioService.save(u);

        for (int i = 0; i < previos.length; i++) {
            Previo prev = new Previo();
            prev.setEstado_previo("A");
            prev.setProcedencia(procedenciaService.findOne(previos[i]));
            prev.setUsuario(u);
            prev.setUnidad(unidadService.findOne(1L));
            previoService.save(prev);
        }

        return ResponseEntity.ok("3");
    }
    
   
    @PostMapping("/eliminarUsuario/{id_usuario}")
    @ResponseBody
    public void eliminarUsuario(@PathVariable(name = "id_usuario")Long id_usuario) {

        Usuario usuario = usuarioService.findOne(id_usuario);
        usuario.setEstado('X');
        usuarioService.save(usuario);

        for (Previo previo : usuario.getPrevios()) {
            previo.setEstado_previo("X");
            previoService.save(previo);
        }
    }
    
}
