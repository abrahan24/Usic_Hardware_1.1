package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.IDetalleBajaDao;
import com.hardware.SystemUsic.models.entity.DetalleBaja;

@Service
public class DetalleBajaServiceImpl implements IDetalleBajaService{

    @Autowired
    private IDetalleBajaDao detalleBajaDao;

    @Override
    public List<DetalleBaja> findAll() {
        // TODO Auto-generated method stub
        return (List<DetalleBaja>) detalleBajaDao.findAll();
    }

    @Override
    public void save(DetalleBaja detalleBaja) {
        // TODO Auto-generated method stub
        detalleBajaDao.save(detalleBaja);
    }

    @Override
    public DetalleBaja findOne(Long id) {
        // TODO Auto-generated method stub
        return detalleBajaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        detalleBajaDao.deleteById(id);
    }
    
}
