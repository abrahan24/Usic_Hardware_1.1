package com.hardware.SystemUsic.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hardware.SystemUsic.models.dao.IGradoAcademicoDao;
import com.hardware.SystemUsic.models.entity.GradoAcademico;

@Service
public class GradoAcademicoServiceImpl implements IGradoAcademicoService{

    @Autowired
    private IGradoAcademicoDao gradoAcademicoDao;

    @Override
    public List<GradoAcademico> findAll() {
        // TODO Auto-generated method stub
        return (List<GradoAcademico>) gradoAcademicoDao.findAll();
    }

    @Override
    public void save(GradoAcademico gradoAcademico) {
        // TODO Auto-generated method stub
        gradoAcademicoDao.save(gradoAcademico);
    }

    @Override
    public GradoAcademico findOne(Long id) {
        // TODO Auto-generated method stub
        return gradoAcademicoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        gradoAcademicoDao.deleteById(id);
    }
    
}
