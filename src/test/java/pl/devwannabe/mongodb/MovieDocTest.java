package pl.devwannabe.mongodb;

import lombok.val;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.devwannabe.domain.Movie;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MovieDocTest {

    @Test
    public void shouldConvertDomainObjectToDocumentAndViceVersa() {
        //given
        val input = givenMovie();

        //when
        val afterConversion = MovieDoc.convertFromMovie(input).convertToMovie();

        //then
        assertThat(afterConversion).isEqualTo(input);
    }

    private Movie givenMovie() {
        return Movie.builder()
                .id("12")
                .title("some title")
                .director("some director")
                .premiere(LocalDate.of(1987, 12, 12))
                .description("not description yet")
                .image(new Binary(BsonBinarySubType.BINARY, new byte[12345]))
                .build();
    }


}