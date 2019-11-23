package pl.devwannabe.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Create or update movie by sending files: json with movie object and optional image.")
    @PostMapping(value = "/movie/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Object> saveMovie(@RequestParam("jsonMovieFile")
                                     @ApiParam(value = "example: /movie-database/src/main/resources/movie.json")
                                     @NonNull MultipartFile jsonMovieFile,
                                     @RequestParam(value = "image", required = false) MultipartFile image) {
        if (jsonMovieFile == null || jsonMovieFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                log.info("saveMovie (movie = [{}]", jsonMovieFile);
                byte[] bytes = jsonMovieFile.getBytes();
                String jsonMovie = new String(bytes);
                val movie = objectMapper.readValue(jsonMovie, Movie.class);
                movieService.saveMovie(movie, image);
                return ResponseEntity.ok().build();
            } catch (IOException e) {
                log.error("importing files failed", e.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                log.error("importing files failed", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
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
    ResponseEntity<Movie> getMovieById(@PathVariable @NonNull String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        val body = movieService.getMovieById(id.trim());
        if (body == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping("/movie/title/{title}")
    ResponseEntity<List<Movie>> getMoviesByTitle(@PathVariable @NonNull String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        val body = movieService.getMoviesByTitle(title);
        if (body.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(body);
    }

    @DeleteMapping("/movie/delete/{id}")
    ResponseEntity deleteMovie(@PathVariable @NonNull String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else
            movieService.removeMovie(id);
        return ResponseEntity.ok().build();
    }
}
