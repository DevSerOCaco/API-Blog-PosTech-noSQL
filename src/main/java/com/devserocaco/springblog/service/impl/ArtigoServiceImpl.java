package com.devserocaco.springblog.service.impl;

import com.devserocaco.springblog.model.Artigo;
import com.devserocaco.springblog.model.ArtigoStatusCount;
import com.devserocaco.springblog.model.Autor;
import com.devserocaco.springblog.model.AutorTotalArtigo;
import com.devserocaco.springblog.repository.ArtigoRepository;
import com.devserocaco.springblog.repository.AutorRepository;
import com.devserocaco.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimistcLockingFaulureException(
            OptimisticLockingFailureException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).
                body("Erro de concorrência: O Artigo foi atualizado por outro usuario");
    }


    public ArtigoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo nao existe!"));
    }

    @Override
    @Transactional
    public Artigo criar(Artigo artigo) {

        if(artigo.getAutor().getCodigo() != null){
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor Inexistente"));
            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }
        try{
            return this.artigoRepository.save(artigo);
        } catch (OptimisticLockingFailureException ex) {
            Artigo atualizado = artigoRepository.findById(artigo.getCodigo()).orElse(null);
            if (atualizado != null) {
                atualizado.setTitulo(artigo.getTitulo());
                atualizado.setTexto(artigo.getTexto());
                atualizado.setStatus(artigo.getStatus());

                atualizado.setVersion(atualizado.getVersion() + 1);


            }
            return this.artigoRepository.save(artigo);
        }



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
    @Transactional
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
    @Transactional
    public void atualizarArtigo(String id, String novaURL) {
        //aqui define o criterio de busca
       Query query = new Query(Criteria.where("_id").is(id));
       //aqui define os campos a serem atualizados
       Update update = new Update().set("url", novaURL);
       //aqui executa a atualização no banco
       this.mongoTemplate.updateFirst(query, update, Artigo.class);

    }

    @Override
    @Transactional
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

    @Override
    public Page<Artigo> findAll(Pageable pageable) {
        Sort sort = Sort.by("titulo").descending();
        Pageable paginacao = PageRequest.of(pageable.getPageNumber(),
                                            pageable.getPageSize(), sort);

        return this.artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status) {
        return this.artigoRepository.obterArtigosPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findiByTexto(String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);
        TextQuery textQuery = TextQuery.queryText(textCriteria).sortByScore();
        return mongoTemplate.find(textQuery, Artigo.class);

    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.group("status").count().as("quantidade"),
                Aggregation.project("quantidade").and("status")
                        .previousOperation()
        );
        AggregationResults<ArtigoStatusCount> result = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);

        return result.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match(
                        Criteria.where("data").gte(dataInicio.toLocalDate().atStartOfDay())
                                .lte(dataFim.toLocalDate().atStartOfDay())
                ),
                Aggregation.group("autor").count().as("totalArtigos"),
                Aggregation.project("totalArtigos").and("autor")
                        .previousOperation()
        );
        AggregationResults<AutorTotalArtigo> result = mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);

        return result.getMappedResults();
    }

}
