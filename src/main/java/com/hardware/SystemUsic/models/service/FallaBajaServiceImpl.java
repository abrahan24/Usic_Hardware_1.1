package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.IFallasBajaDao;
import com.hardware.SystemUsic.models.entity.FallasBaja;

@Service
public class FallaBajaServiceImpl implements IFallaBajaService{

    @Autowired
    private IFallasBajaDao fallasBajaDao;

    @Override
    public List<FallasBaja> findAll() {
        // TODO Auto-generated method stub
        return (List<FallasBaja>) fallasBajaDao.findAll();
    }

    @Override
    public void save(FallasBaja fallasBaja) {
        // TODO Auto-generated method stub
        fallasBajaDao.save(fallasBaja);
    }

    @Override
    public FallasBaja findOne(Long id) {
        // TODO Auto-generated method stub
        return fallasBajaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        fallasBajaDao.deleteById(id);
    }
    
}
