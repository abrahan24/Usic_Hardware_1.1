package com.hardware.SystemUsic.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardware.SystemUsic.Metodos;
import com.hardware.SystemUsic.models.entity.Almacen;
import com.hardware.SystemUsic.models.entity.Baja;
import com.hardware.SystemUsic.models.entity.Cargo;
import com.hardware.SystemUsic.models.entity.DetalleAlmacenFallaBaja;
import com.hardware.SystemUsic.models.entity.DetalleBaja;
import com.hardware.SystemUsic.models.entity.Persona;
import com.hardware.SystemUsic.models.entity.Usuario;
import com.hardware.SystemUsic.models.service.IAlmacenService;
import com.hardware.SystemUsic.models.service.IBajaService;
import com.hardware.SystemUsic.models.service.ICargoService;
import com.hardware.SystemUsic.models.service.IDetalleAlmacenFallaBajaService;
import com.hardware.SystemUsic.models.service.IDetalleBajaService;
import com.hardware.SystemUsic.models.service.IFallaBajaService;
import com.hardware.SystemUsic.models.service.IPersonaService;
import com.hardware.SystemUsic.models.service.IUsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;


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

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ICargoService cargoService;


    @RequestMapping(value = "/informe_baja",method = RequestMethod.GET)
    public String Informe_Baja(Model model, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            
            model.addAttribute("baja", new Baja());
            model.addAttribute("personas", personaService.findAll());
            // model.addAttribute("activos", almacenService);

            return "informe_baja";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping(value = "/lista_informes_bajas",method = RequestMethod.GET)
    public String Lista_Informe_Baja(Model model,@RequestParam(name = "validado",required = false)String validado, RedirectAttributes flash, HttpServletRequest request ){

        if (request.getSession().getAttribute("persona") != null) {
            
            if (validado != null ) {
                model.addAttribute("validado", validado);
            }

            model.addAttribute("bajas", bajaService.findAll());
           
            return "INFORMES/Reporte_Informe_Baja";
		} else {
			return "redirect:/hardware/login";
		}
    }
    

    @RequestMapping(value = "/activos")
    public String getContent1(@RequestParam(value = "selectedValues", required = false) String selectedValues, Model model, HttpServletRequest request) {
        
        try {
            if (selectedValues != null) {
                // Dividir la cadena de valores separados por comas en un array de Long
                String[] selectedValuesArray = selectedValues.split(",");
                Long[] id_almacen = new Long[selectedValuesArray.length];
    
                for (int i = 0; i < selectedValuesArray.length; i++) {
                    id_almacen[i] = Long.parseLong(selectedValuesArray[i]);
                }
    
                List<Long> idsSeleccionados = Arrays.asList(id_almacen);
                model.addAttribute("activos_selec", almacenService.Lista_Activos_Por_Id(idsSeleccionados));
                // System.out.println(almacenService.Lista_Activos_Por_Id(idsSeleccionados).size());
            }
            return "content :: content2";
        } catch (Exception e) {
            return "content :: content2";
        }
       
    }

    
    // @RequestMapping(value = "/activos/{selectedValues}")
    // public String getContent3(@PathVariable(value = "selectedValues", required = false)String selectedValues, Model model, HttpServletRequest request) {
    //     if (selectedValues != null) {
            
    //         System.out.println(selectedValues);
    //         model.addAttribute("activos_selec", almacenService.Lista_Activos_Por_Codigo_Equipo(selectedValues));
    //     }

    //     return "content :: content2";
    // }

    @RequestMapping(value = "/obtener_activos/{cod_equipo}", method = RequestMethod.GET)
    public String getContent4(@PathVariable("cod_equipo") String cod_equipo, Model model) {
        if (cod_equipo != null) {
            model.addAttribute("Activos_S", almacenService.Lista_Activos_Cod_Equipo(cod_equipo));

        }
        return "content :: content4";
    }

    // @RequestMapping(value = "/activos", method = RequestMethod.POST)
    // public String getContent3(@RequestBody String[] selectedValues, Model model) {
    //     if (selectedValues != null) {
    //         // Realizar las operaciones necesarias con los datos capturados
    //         String[] cod_selec = new String[selectedValues.length];
    //         for (int i = 0; i < selectedValues.length; i++) {
    //             System.out.println(selectedValues[i]);
    //             cod_selec[i] = selectedValues[i];
    //         }

    //         List<String> cod_seleccionados = Arrays.asList(cod_selec);
    //         model.addAttribute("activos_selec", almacenService.Lista_Activos_Por_Codigo_Equipo(cod_seleccionados));
    //     }
    
    //     return "content :: content2";
    // }

    // @RequestMapping(value = "/activos")
    // public String getContent2(@RequestParam(value = "selectedValues", required = false) String selectedValues,
    //         Model model, HttpServletRequest request) {
    //     if (selectedValues != null) {
    //         String[] selectedValuesArray = selectedValues.split(",");
    //         List<Long> idsSeleccionados = new ArrayList<>();

    //         for (String value : selectedValuesArray) {
    //             try {
    //                 idsSeleccionados.add(Long.parseLong(value));
    //             } catch (NumberFormatException e) {
    //                 // Manejar la conversión fallida si es necesario
    //                 e.printStackTrace();
    //             }
    //         }

    //         if (!idsSeleccionados.isEmpty()) {
    //             model.addAttribute("activos_selec", almacenService.Lista_Activos_Por_Id(idsSeleccionados));
    //         }
    //     }

    //     return "content :: content2";
    // }


    @RequestMapping(value = "/add_informe_baja", method = RequestMethod.POST)
public String Form_Informe_Baja(Model model, @Validated Baja baja,
                                @RequestParam(name = "id_persona", required = false) Long id_persona,
                                @RequestParam(name = "id_usuario", required = false) Long id_usuario,
                                @RequestParam(name = "id_almacen", required = false) String idAlmacenJson,
                                @RequestParam MultiValueMap<String, String> params, // Recibir todos los parámetros
                                RedirectAttributes flash, HttpServletRequest request) {

    if (request.getSession().getAttribute("persona") != null) {
        baja.setEstado_baja("A");
        baja.setPersona(personaService.findOne(id_persona));
        baja.setUsuario(usuarioService.findOne(id_usuario));
        bajaService.save(baja);
        Long[] idAlmacenArray = null;
        if (idAlmacenJson != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                idAlmacenArray = objectMapper.readValue(idAlmacenJson, Long[].class);
                for (Long id : idAlmacenArray) {
                    // Obtener los valores de los checkboxes asociados a este id_almacen
                    Long[] id_fallaBaja = params.get("id_fallaBaja_" + id)
                            .stream()
                            .map(Long::valueOf)
                            .toArray(Long[]::new);

                    DetalleBaja detalleBaja = new DetalleBaja();
                    Almacen almacen = almacenService.findOne(id);
                    almacen.setEstado("B"); // Cambia el estado del activo a B Estado de Baja
                    almacenService.save(almacen);
                    detalleBaja.setAlmacen(almacen);
                    detalleBaja.setEstado_detalleBaja("A");
                    detalleBaja.setBaja(bajaService.findOne(baja.getId_baja())); // Aquí es donde surge el error
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
            } catch (IOException e) {
                System.out.println("Error al Registrar Informe de Baja");
            }
        }

        if (baja.getId_baja() != null) {
            baja.setFecha_baja(new Date());
            bajaService.save(baja);
            flash.addAttribute("validado", "Se ha Realizado el Informe N°" + baja.getId_baja() + " Con Éxito!");
        } else {
            baja.setFecha_modificacion(new Date());
            bajaService.save(baja);
            flash.addAttribute("validado", "Se ha Editado el Informe N°" + baja.getId_baja() + " Con Éxito!");
        }

        return "redirect:/hardware-servicio/lista_informes_bajas";
    } else {
        return "redirect:/hardware/login";
    }
}
    @RequestMapping("/ficha_tecnica_baja/{id_baja}")
    public String Imprimir_Informe_Baja(Model model,@PathVariable("id_baja")Long id_baja,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("persona") != null) {
            
            Baja baja = bajaService.findOne(id_baja);

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            usuario = usuarioService.findOne(usuario.getId_usuario());

            if (baja.getPersona().getGradoAcademico() != null) {

                model.addAttribute("baja", bajaService.findOne(id_baja));
                model.addAttribute("per_dirig",
                        baja.getPersona().getGradoAcademico().getSigla_gradoAcademico() + " "
                                + baja.getPersona().getNombre() + " " + baja.getPersona().getAp_paterno() + " "
                                + baja.getPersona().getAp_materno() + "<br><b>"
                                + baja.getPersona().getCargo().getCargo());
                model.addAttribute("user_tec",
                        baja.getUsuario().getPersona().getGradoAcademico().getSigla_gradoAcademico() + " "
                                + baja.getUsuario().getPersona().getNombre() + " "
                                + baja.getUsuario().getPersona().getAp_paterno() + " "
                                + baja.getUsuario().getPersona().getAp_materno() + "<br><b>"
                                + baja.getUsuario().getPersona().getCargo().getCargo());
            } else {
                model.addAttribute("baja", bajaService.findOne(id_baja));
                model.addAttribute("per_dirig",
                        baja.getPersona().getNombre() + " " + baja.getPersona().getAp_paterno() + " "
                                + baja.getPersona().getAp_materno() + "<br><b>"
                                + baja.getPersona().getCargo().getCargo());
                model.addAttribute("user_tec",
                        baja.getUsuario().getPersona().getGradoAcademico().getSigla_gradoAcademico() + " " +
                                baja.getUsuario().getPersona().getNombre() + " "
                                + baja.getUsuario().getPersona().getAp_paterno() + " "
                                + baja.getUsuario().getPersona().getAp_materno() + "<br><b>"
                                + baja.getUsuario().getPersona().getCargo().getCargo());
            }


            List<DetalleBaja> detalleBajasFiltrados = baja.getDetalleBajas()
                    .stream()
                    .filter(bd -> !bd.getEstado_detalleBaja().equals("X"))
                    .collect(Collectors.toList());

            model.addAttribute("detalleBajasFiltrados", detalleBajasFiltrados);
    
            return "INFORMES/ficha_baja";
		} else {
			return "redirect:/hardware/login";
		}
    }
    
    @RequestMapping("/eliminar_ficha_tecnica_baja/{id_baja}")
    public String Eliminar_Informe_Baja(Model model,@PathVariable("id_baja")long id_baja,RedirectAttributes flash, HttpServletRequest request){

         if (request.getSession().getAttribute("persona") != null) {
            
            Baja baja = bajaService.findOne(id_baja);

            baja.setEstado_baja("X");

            bajaService.save(baja);

            flash.addAttribute("validado", "Se ah Eliminado el Informe N°"+baja.getId_baja()+" Con Exito!");
            
            return "redirect:/hardware-servicio/lista_informes_bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/editar_ficha_tecnica_baja/{id_baja}")
    public String Editar_Informe_Baja(Model model,@PathVariable("id_baja")long id_baja,RedirectAttributes flash, HttpServletRequest request){

         if (request.getSession().getAttribute("persona") != null) {
            
            Baja baja = bajaService.findOne(id_baja);
            baja.setFecha_modificacion(new Date());

            for (DetalleBaja detalleBaja : baja.getDetalleBajas()) {
                detalleBaja.setEstado_detalleBaja("X");
                Almacen almacen = almacenService.findOne(detalleBaja.getAlmacen().getId_almacen());
                almacen.setEstado("A");
                almacenService.save(almacen);
                detalleBajaService.save(detalleBaja);
                for (DetalleAlmacenFallaBaja detalleAlmacenFallaBaja : detalleBaja.getDetalleAlmacenFallaBajas()) {
                    detalleAlmacenFallaBaja.setEstado_detalleAlmacenFallaBaja("X");
                    detalleAlmacenFallaBajaService.save(detalleAlmacenFallaBaja);
                }
            }

            model.addAttribute("baja", baja);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("activos", almacenService.findAll());
            
            return "informe_baja";
		} else {
			return "redirect:/hardware/login";
		}
    }

    @RequestMapping("/validar_baja/{id_baja}")
    public String Verificar_Informe_Baja(Model model,@PathVariable("id_baja")long id_baja,RedirectAttributes flash, HttpServletRequest request) throws FileNotFoundException, IOException{
            
        if (request.getSession().getAttribute("persona") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Date date = new Date();
        
            Cargo cargo = cargoService.findOne(usuario.getPersona().getCargo().getId_cargo());
            // Define el formato deseado
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            // Formatea la fecha y hora
            String formattedDate = formatter.format(date);
            System.out.println(usuario.getPersona().getNombre());
            Metodos metodos = new Metodos();
            Baja baja = bajaService.findOne(id_baja);
            
            baja.setEstado_baja("V");

            Path rootPath = Paths.get("uploads_bajas");
            Path rootAbsolutPath = rootPath.toAbsolutePath();
            String rutaDirectorio = rootAbsolutPath.toString() + "//" +"QR_baja_"+ baja.getId_baja()+".png";
            try {
                if (!Files.exists(rootPath)) {
                    Files.createDirectories(rootPath);
                    System.out.println("Directorio creado: " + rutaDirectorio);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorio);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
            String sigla = usuario.getPersona().getGradoAcademico().getSigla_gradoAcademico();
           
            System.out.println(cargo.getCargo());
            metodos.QR2(
                    "Autorizado por: "+sigla+usuario.getPersona().getNombre() + " " + usuario.getPersona().getAp_paterno() + " "+ usuario.getPersona().getAp_materno() + "\n" +
                            "Cargo:"+cargo.getCargo() +"\n" + "Fecha: " + formattedDate,
                    rutaDirectorio);
            baja.setQR_baja("QR_baja_"+baja.getId_baja());
            bajaService.save(baja);
            flash.addAttribute("validado", "Informe N°:"+baja.getId_baja()+" Autorizado!");
            return "redirect:/hardware-servicio/lista_informes_bajas";
		} else {
			return "redirect:/hardware/login";
		}
    }

}

