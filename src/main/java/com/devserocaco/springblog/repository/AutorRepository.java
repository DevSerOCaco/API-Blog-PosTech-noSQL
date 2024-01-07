package com.devserocaco.springblog.repository;

import com.devserocaco.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    // Se precisar de métodos personalizados, você pode adicioná-los aqui
}