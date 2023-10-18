package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.IDetalleAlmacenFallaBajaDao;
import com.hardware.SystemUsic.models.entity.DetalleAlmacenFallaBaja;

@Service
public class DetalleAlmacenFallaBajaServiceImpl implements IDetalleAlmacenFallaBajaService{

    @Autowired
    private IDetalleAlmacenFallaBajaDao detalleAlmacenFallaBajaDao;

    @Override
    public List<DetalleAlmacenFallaBaja> findAll() {
        // TODO Auto-generated method stub
        return (List<DetalleAlmacenFallaBaja>) detalleAlmacenFallaBajaDao.findAll();
    }

    @Override
    public void save(DetalleAlmacenFallaBaja detalleAlmacenFallaBaja) {
        // TODO Auto-generated method stub
        detalleAlmacenFallaBajaDao.save(detalleAlmacenFallaBaja);
    }

    @Override
    public DetalleAlmacenFallaBaja findOne(Long id) {
        // TODO Auto-generated method stub
        return detalleAlmacenFallaBajaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        detalleAlmacenFallaBajaDao.deleteById(id);
    }
    
}
