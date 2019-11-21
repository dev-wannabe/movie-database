package pl.devwannabe.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieMongoRepository extends MongoRepository <MovieDoc, String> {

    List<MovieDoc> findAllByTitleIgnoreCase(String title);

}
