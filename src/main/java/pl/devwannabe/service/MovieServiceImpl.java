package pl.devwannabe.service;

import com.mongodb.lang.Nullable;
import lombok.NonNull;
import lombok.val;
import org.apache.commons.lang3.Validate;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.devwannabe.domain.Movie;
import pl.devwannabe.domain.MovieRepository;
import pl.devwannabe.domain.MovieService;

import java.io.IOException;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @NonNull
    private MovieRepository movieRepository;

    public MovieServiceImpl(@NonNull MovieRepository movieRepository) {
        Validate.notNull(movieRepository);
        this.movieRepository = movieRepository;
    }

    @Override
    public void saveMovie(Movie movie, @Nullable MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            movieRepository.saveMovie(movie);
        } else {
            val binaryImage = new Binary(BsonBinarySubType.BINARY, image.getBytes());
            movie.setImage(binaryImage);
            movieRepository.saveMovie(movie);
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public void removeMovie(String id) {
        movieRepository.deleteMovie(id);
    }

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findOne(id);
    }

    @Override
    public List<Movie> getMoviesByTitle(String title) {
        return movieRepository.findByTitle(title);
    }
}
