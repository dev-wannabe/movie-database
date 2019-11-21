package pl.devwannabe.mongodb;


import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Repository;
import pl.devwannabe.domain.Movie;
import pl.devwannabe.domain.MovieRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MDBMovieRepository implements MovieRepository {

    @NonNull
    private MovieMongoRepository repository;

    public MDBMovieRepository(@NonNull MovieMongoRepository repository) {
        Validate.notNull(repository);
        this.repository = repository;
    }

    @Override
    public void addMovie(Movie movie) {
        repository.save(MovieDoc.convertFromMovie(movie));
    }

    @Override
    public void deleteMovie(String id) {
        repository.deleteById(id);
    }

    @Override
    public Movie findOne(String id) {
        return repository.findById(id).orElse(null).convertToMovie();
    }

    @Override
    public List<Movie> findAll() {
        return repository.findAll().stream()
                .map(MovieDoc::convertToMovie)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByTitle(String title) {
        if (repository.findAllByTitleIgnoreCase(title) != null) {
            return repository.findAllByTitleIgnoreCase(title).stream()
                    .map(MovieDoc::convertToMovie)
                    .collect(Collectors.toList());
        } else
            return Collections.emptyList();
    }
}
