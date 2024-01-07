package com.devserocaco.springblog.repository;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends MongoRepository<Autor, String> {
    // Se precisar de métodos personalizados, você pode adicioná-los aqui
}