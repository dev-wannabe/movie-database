package pl.devwannabe.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    void saveMovie(Movie movie, MultipartFile image) throws IOException;

    void removeMovie(String id);

    Movie getMovieById(String id);

    List<Movie> getAllMovies();

    List<Movie> getMoviesByTitle(String title);

}
