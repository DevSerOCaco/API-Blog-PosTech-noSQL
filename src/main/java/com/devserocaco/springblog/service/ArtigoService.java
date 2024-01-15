package com.devserocaco.springblog.service;

import ch.qos.logback.core.util.Loader;
import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.model.ArtigoStatusCount;
import com.devserocaco.springblog.model.AutorTotalArtigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> obterTodos();
    public Artigo obterPorCodigo(String codigo);
    /*public Artigo criar(Artigo artigo);*/
    public ResponseEntity<?> criar(Artigo artigo);
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo);
    public List<Artigo> findByDataGreaterThan(LocalDateTime data);
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);
    public Artigo atualizar(Artigo artigo);
    public void atualizarArtigo(String id, String novaURL);
    public void deleteById(String id);
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo);
    Page<Artigo> findAll(Pageable pageable);
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status);
    public List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status);
    public List<Artigo> findiByTexto(String searchTerm);
    public List<ArtigoStatusCount> contarArtigosPorStatus();
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);
}
