package pl.devwannabe.domain;


import java.util.List;

public interface MovieRepository {

    void saveMovie(Movie movie);

    void deleteMovie(String id);

    Movie findOne(String id);

    List<Movie> findAll();

    List<Movie> findByTitle(String title);

}
