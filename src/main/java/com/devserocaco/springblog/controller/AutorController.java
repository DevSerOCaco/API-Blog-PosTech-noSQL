package com.devserocaco.springblog.controller;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.service.ArtigoService;
import com.devserocaco.springblog.service.impl.ArtigoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    public ArtigoService artigoService;

    @GetMapping
    public List<Artigo> obterTodos(){
        return this.artigoService.obterTodos();
    }
    @GetMapping("/{codigo}")
    public Artigo oberPorCodigo(@PathVariable String codigo){
        return this.artigoService.obterPorCodigo(codigo);
    }
    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo){
        return this.artigoService.criar(artigo);
    }
}
