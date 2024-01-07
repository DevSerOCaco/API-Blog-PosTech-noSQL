package com.devserocaco.springblog.service.impl;

import com.devserocaco.springblog.model.Autor;
import com.devserocaco.springblog.repository.AutorRepository;
import com.devserocaco.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {

    @Autowired
    private AutorRepository autorRepository;
    
    @Override
    public List<Autor> obterTodos() {
        return this.autorRepository.findAll();
    }

    @Override
    public Autor obterPorCodigo(String codigo) {
        return this.autorRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Autor nao existe!"));
    }

    @Override
    public Autor criar(Autor autor) {
        return this.autorRepository.save(autor);
    }
}
