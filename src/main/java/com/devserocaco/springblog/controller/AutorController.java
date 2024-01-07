package com.devserocaco.springblog.controller;

import com.devserocaco.springblog.model.Autor;
import com.devserocaco.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/autores")
public class AutorController {

    @Autowired
    public AutorService autorService;

    @GetMapping
    public List<Autor> obterTodos(){
        return this.autorService.obterTodos();
    }
    @GetMapping("/{codigo}")
    public Autor oberPorCodigo(@PathVariable String codigo){
        return this.autorService.obterPorCodigo(codigo);
    }
    @PostMapping
    public Autor criar(@RequestBody Autor autor){
        return this.autorService.criar(autor);
    }
}
