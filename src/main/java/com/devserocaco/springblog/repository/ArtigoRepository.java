package com.devserocaco.springblog.repository;

import com.devserocaco.springblog.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    // Se precisar de métodos personalizados, você pode adicioná-los aqui
    public void deleteById(String id);
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);
    @Query("{ $and:  [ {'data':{ $gte: ?0 }}, { 'data': { $lte: ?1 }} ] }")//?0 pega o parametro "de" e o ?1 o parametro "aTE";
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
    Page<Artigo> findAll(Pageable pageable);


}