package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.IFallaEquipoBajaDao;
import com.hardware.SystemUsic.models.entity.FallaEquipoBaja;

@Service
public class FallaEquipoBajaServiceImpl implements IFallaEquipoBajaService{


    @Autowired
    private IFallaEquipoBajaDao fallaEquipoBajaDao;

    @Override
    public List<FallaEquipoBaja> findAll() {
        // TODO Auto-generated method stub
        return (List<FallaEquipoBaja>) fallaEquipoBajaDao.findAll();
    }

    @Override
    public void save(FallaEquipoBaja fallaEquipoBaja) {
        // TODO Auto-generated method stub
        fallaEquipoBajaDao.save(fallaEquipoBaja);
    }

    @Override
    public FallaEquipoBaja findOne(Long id) {
        // TODO Auto-generated method stub
        return fallaEquipoBajaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        fallaEquipoBajaDao.deleteById(id);
    }
    
}
