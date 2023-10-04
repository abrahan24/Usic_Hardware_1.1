package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.ITipoServicioDao;
import com.hardware.SystemUsic.models.entity.TipoServicio;

@Service
public class TipoServicioServiceImpl implements ITipoServicioService{

    @Autowired
    private ITipoServicioDao tipoServicioDao;

    @Override
    public List<TipoServicio> findAll() {
        // TODO Auto-generated method stub
        return (List<TipoServicio>) tipoServicioDao.findAll();
    }

    @Override
    public void save(TipoServicio tipoServicio) {
        // TODO Auto-generated method stub
        tipoServicioDao.save(tipoServicio);
    }

    @Override
    public TipoServicio findOne(Long id) {
        // TODO Auto-generated method stub
        return tipoServicioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tipoServicioDao.deleteById(id);
    }
    
}
