package com.devserocaco.springblog.service.impl;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.model.Autor;
import com.devserocaco.springblog.repository.ArtigoRepository;
import com.devserocaco.springblog.repository.AutorRepository;
import com.devserocaco.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

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
        if(artigo.getAutor().getCodigo() != null){
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor Inexistente"));
            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }
        return this.artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        ///Cria uma query com o criterio gratehr then
        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria
                .where("data").is(data)
                .and("status").is(status));

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Artigo atualizar(Artigo artigo) {
        if (artigoRepository.existsById(artigo.getCodigo())) {
            // Atualiza o artigo existente
            return artigoRepository.save(artigo);
        } else {
            // Se o artigo não existe, você pode lidar com isso de acordo com a lógica do seu aplicativo
            throw new IllegalArgumentException("Artigo não encontrado com ID: " + artigo.getCodigo());
        }
    }

    @Override
    public void atualizarArtigo(String id, String novaURL) {
        //aqui define o criterio de busca
       Query query = new Query(Criteria.where("_id").is(id));
       //aqui define os campos a serem atualizados
       Update update = new Update().set("url", novaURL);
       //aqui executa a atualização no banco
       this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status,
                                                  LocalDateTime data,
                                                  String titulo) {
        Criteria criteria = new Criteria();
        // Filtrar artigos com dada menor ou igual ao valor fornecido
        criteria.and("data").lte(data);

        // Filtrar somente artigos com status especificados
        if (status != null){
           criteria.and("status").is(status);
        }

        // Filtrar somente artigos cujo titulo exista
        if (titulo != null && !titulo.isEmpty()){
            criteria.and("titulo").regex(titulo, "i");
        }
        Query query = new Query(criteria);

        return mongoTemplate.find(query, Artigo.class);
    }

}
