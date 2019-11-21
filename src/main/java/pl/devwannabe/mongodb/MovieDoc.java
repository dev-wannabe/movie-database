package pl.devwannabe.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.devwannabe.domain.Movie;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class MovieDoc {

    @Id
    private String id;

    private String title;

    private String director;

    private LocalDate premiere;

    private String description;

    private Binary image;


    public static MovieDoc convertFromMovie(Movie movie) {
        return MovieDoc.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .premiere(movie.getPremiere())
                .description(movie.getDescription())
                .image(movie.getImage())
                .build();
    }

    public Movie convertToMovie() {
        return Movie.builder()
                .id(id)
                .title(title)
                .director(director)
                .premiere(premiere)
                .description(description)
                .image(image)
                .build();
    }

}
