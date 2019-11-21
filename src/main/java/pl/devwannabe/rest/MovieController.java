package pl.devwannabe.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.devwannabe.domain.Movie;
import pl.devwannabe.domain.MovieService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movie-database")
public class MovieController {

    @NonNull
    private MovieService movieService;

    public MovieController(@NonNull MovieService movieService) {
        Validate.notNull(movieService);
        this.movieService = movieService;
    }

    @PostMapping(value = "/movie/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Object> saveMovie(@RequestParam("jsonMovieFile") MultipartFile jsonMovieFile, @RequestParam(value = "image", required = false) MultipartFile image) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("saveMovie (movie = [{}], image = [{}])", jsonMovieFile.getName(), image.getName());
        try {
            byte[] bytes = jsonMovieFile.getBytes();
            String completeData = new String(bytes);
            Movie movie = objectMapper.readValue(completeData, Movie.class);
            movieService.saveMovie(movie, image);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("importing movie failed", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("importing movie failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all-movies")
    ResponseEntity<List<Movie>> getAllMovies() {
        val body = movieService.getAllMovies();
        if (body.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(body);
    }

    @GetMapping("/movie/id/{id}")
    ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        val body = movieService.getMovieById(id);
        if (body == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping("/movie/title/{title}")
    ResponseEntity<List<Movie>> getMoviesByTitle(@PathVariable String title) {
        if (title == null) {
            return ResponseEntity.badRequest().build();
        }
        val body = movieService.getMoviesByTitle(title);
        if (body.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(body);
    }

    @DeleteMapping("/movie/delete/{id}")
    ResponseEntity deleteMovie(@PathVariable String id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        } else
            movieService.removeMovie(id);
        return ResponseEntity.ok().build();
    }
}
