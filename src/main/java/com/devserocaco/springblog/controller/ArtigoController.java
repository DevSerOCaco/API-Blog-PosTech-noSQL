package com.devserocaco.springblog.controller;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.service.ArtigoService;
import com.devserocaco.springblog.service.impl.ArtigoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam LocalDateTime date) {
        return this.artigoService.findByDataGreaterThan(date);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(@RequestParam LocalDateTime date,
                                            @RequestParam Integer status){
        return this.artigoService.findByDataAndStatus(date, status);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo) {

        this.artigoService.atualizar(artigo);
    }

    @PutMapping("/{id}")
    public void atualizarArtigo(@PathVariable String id,
                                @RequestBody String novaURL) {
        this.artigoService.atualizarArtigo(id, novaURL);

    }

    @DeleteMapping("/{id}")
    public void deleteArtigoById(@PathVariable String id) {
        this.artigoService.deleteById(id);
    }

    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThen (
            @RequestParam Integer status,
            @RequestParam LocalDateTime data) {

        return this.artigoService.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/periodo")
    public List<Artigo> obterArtirgoPorDataHora(@RequestParam LocalDateTime de,
                                                @RequestParam LocalDateTime ate){
        return this.artigoService.obterArtigoPorDataHora(de, ate);
    }

}
