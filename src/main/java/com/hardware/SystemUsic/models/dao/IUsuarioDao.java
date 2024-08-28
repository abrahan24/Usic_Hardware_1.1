package com.hardware.SystemUsic.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hardware.SystemUsic.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

    @Query("select us from Usuario us where us.usuario=?1 and us.contrasena=?2")
    public Usuario getUsuario(String usuario,String contrasena);
    

    @Query(value = "SELECT u.* FROM usuario u \n" + //
                "WHERE u.estado = 'A' OR u.estado = 'S' ",nativeQuery = true)
    public List<Usuario> obtenerUsuariosConEstado_A();

    @Query(value = "SELECT u.* FROM persona p \n" + //
                "LEFT JOIN usuario u ON u.id_persona = p.id_persona \n" + //
                "WHERE p.ci = ?1",nativeQuery = true)
    public Usuario validarExisteUsuarioPorCI(String ci);
}
