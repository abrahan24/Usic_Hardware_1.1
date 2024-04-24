package com.hardware.SystemUsic.controllers;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.service.ICargoService;
import com.hardware.SystemUsic.models.service.IGradoAcademicoService;
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

    @Autowired
    private IGradoAcademicoService gradoAcademicoService;

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
                persona.setEstado("A");
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

    @RequestMapping(value = "/add_Persona", method = RequestMethod.POST)
    public String add_Persona_edit(Model model, @Validated Persona persona,
            @RequestParam(name = "id_unidad", required = false) Long id_unidad,
            @RequestParam(name = "id_cargo", required = false) Long id_cargo,
            @RequestParam("id_gradoAcademico")Long id_gradoAcademico, RedirectAttributes flash,
            HttpServletRequest request) {
        
        if (request.getSession().getAttribute("persona") != null) {

            persona.setEstado("A");
            persona.setCargo(cargoService.findOne(id_cargo));
            persona.setUnidad(unidadService.findOne(id_unidad));
            persona.setGradoAcademico(gradoAcademicoService.findOne(id_gradoAcademico));
            personaService.save(persona);

            flash.addAttribute("validado", "Persona Con CI:"+persona.getCi()+" Editada Con Éxito!");

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
            model.addAttribute("grados", gradoAcademicoService.findAll());
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

   
    // @RequestMapping("/persona")
    // public String Api_Persona2(Model model, @RequestParam("cod_persona")String cod_persona,RedirectAttributes flash,
    // HttpServletRequest request)throws ParseException {

    //     if (request.getSession().getAttribute("persona") != null) {
    //         System.out.println(cod_persona);

    //         // Consumir la URL y manejar los datos
    //         // String apiUrl = "http://virtual.uap.edu.bo:7174/api/londraPost/v1/personaLondra/obtenerDatos2";
    //         String apiUrl = "http://virtual.uap.edu.bo:7174/api/londraPost/v1/obtenerDatos";
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.APPLICATION_JSON);
    //         RestTemplate restTemplate = new RestTemplate();
    //         // Crear un objeto JSON con el parámetro cod_persona
    //         String requestBody = "{\"usuario\": \"" + cod_persona + "\",\"contrasena\": \"" + 420267 + "\"}";
    //         HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    //         // Hacer la solicitud POST
    //         ResponseEntity<Map> resp = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);	
	// 		System.out.println(resp.getBody().get("status").toString());
            
    //         return "redirect:/hardware-servicio/add_Persona";
    //     } else {
    //         return "redirect:/hardware/login";
    //     }
    // }

    @RequestMapping(value = "/persona", method = RequestMethod.POST)
    public String logearseCa(Model model, HttpServletRequest request,
            @RequestParam(name = "cod_persona", required = false) String cod_persona, RedirectAttributes flash)
            throws ParseException {

        Map<String, Object> requests = new HashMap<String, Object>();

        requests.put("usuario", cod_persona);

        // String url = "http://localhost:3333/api/londraPost/v1/obtenerDatos";
        // String url = "http://virtual.uap.edu.bo:7174/api/londraPost/v1/personaLondra/obtenerDatos";
        String url = "http://localhost:3333/api/londraPost/v1/personaLondra/obtenerDatos";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity(requests, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

        if (request.getSession().getAttribute("persona") != null) {
            try {

                if (resp.getStatusCode() == HttpStatus.OK) { // Verificar el estado de la respuesta

                    System.out.println("1");
                    Persona p = new Persona();
                    p.setNombre(resp.getBody().get("per_nombres").toString());
                    p.setAp_paterno(resp.getBody().get("per_ap_paterno").toString());
                    p.setAp_materno(resp.getBody().get("per_ap_materno").toString());
                    p.setCi(resp.getBody().get("per_num_doc").toString());
                    p.setCelular(Integer.parseInt(resp.getBody().get("perd_celular").toString()));

                    model.addAttribute("persona", p);
                    model.addAttribute("unidades", unidadService.findAll());
                    model.addAttribute("cargos", cargoService.findAll());

                }
                return "add_Persona";
            } catch (HttpServerErrorException.InternalServerError e) {
                System.out.println("4");
                flash.addFlashAttribute("error", "Ha ocurrido un error en el servidor");
                return "redirect:/hardware-servicio/add_Persona";
            } catch (HttpClientErrorException e) {
                System.out.println("5");
                flash.addFlashAttribute("error", "Error en la solicitud al servidor");
                return "redirect:/hardware-servicio/add_Persona";
            } catch (Exception e) {
                System.out.println("6");
                flash.addFlashAttribute("validado", "Persona No Existe, Agregar de Manera Manual");
                return "redirect:/hardware-servicio/add_Persona";
            }

        } else {
            return "redirect:/hardware/login";
        }

    }
}
