package pl.devwannabe.service;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import pl.devwannabe.domain.Movie;
import pl.devwannabe.mongodb.MDBMovieRepository;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl cut;

    @Mock
    private MDBMovieRepository repository;

    private Binary image = new Binary(BsonBinarySubType.BINARY, new byte[12345]);

    @Test
    public void shouldSaveMovie() throws IOException {

        //given
        val multipartFileArgumentCaptor = ArgumentCaptor.forClass(MultipartFile.class).capture();

        //when
        cut.saveMovie(givenMovie("1"), multipartFileArgumentCaptor);

        //then
        then(repository).should().saveMovie(any());
    }

    @Test
    public void shouldGetAllMovies() {

        //given
        val movies = ImmutableList.of(givenMovie("1"), givenMovie("2"), givenMovie("3"));

        //and
        given(repository.findAll()).willReturn(movies);

        //when
        val result = cut.getAllMovies();

        //then
        assertThat(result).isEqualTo(movies);
    }

    @Test
    public void shouldRemoveMovieById() {

        //when
        cut.removeMovie("1");

        //then
        then(repository).should().deleteMovie("1");
    }

    @Test
    public void shouldGetMovieByMovieId() {

        //given
        given(repository.findOne(any())).willReturn(givenMovie("1"));

        //when
        val result = cut.getMovieById("2");

        //then
        assertThat(result).isEqualTo(givenMovie("1"));
    }

    @Test
    public void getMoviesByTitle() {

        //given
        val movies = ImmutableList.of(givenMovie("1", "some title"), givenMovie("2", "some title"));

        //and
        given(repository.findByTitle("some title")).willReturn(movies);

        //when
        val result = cut.getMoviesByTitle("some title");

        //then
        assertThat(result).isEqualTo(movies);
    }

    private Movie givenMovie(String id, String title) {
        return Movie.builder()
                .id(id)
                .title(title)
                .director("some director")
                .premiere(LocalDate.of(1987, 12, 12))
                .description("not description yet")
                .image(image)
                .build();
    }

    private Movie givenMovie(String id) {
        return Movie.builder()
                .id(id)
                .title("some title")
                .director("some director")
                .premiere(LocalDate.of(1987, 12, 12))
                .description("not description yet")
                .image(image)
                .build();
    }

}
