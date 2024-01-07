package com.devserocaco.springblog.service.impl;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.repository.ArtigoRepository;
import com.devserocaco.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository artigoRepository;
    
    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo nao existe!"));
    }

    @Override
    public Artigo criar(Artigo artigo) {
        return this.artigoRepository.save(artigo);
    }
}
