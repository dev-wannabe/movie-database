package pl.devwannabe.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.devwannabe.domain.Movie;
import pl.devwannabe.domain.MovieService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextHierarchy(@ContextConfiguration(classes = MovieController.class))
@WebMvcTest(value = {MovieController.class})
public class MovieControllerTest extends BaseControllerTest {

    @Autowired
    private MovieController movieController;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ObjectMapper objectMapper;

    private static final String MOVIE_DATABASE = "/movie-database";

    private Binary image = new Binary(BsonBinarySubType.BINARY, new byte[12345]);
    private String jsonMovieFile = "{ \"title\" : \"some title\"}";
    private MockMultipartFile multipartJsonFile = givenJsonMovieFile(jsonMovieFile);
    private MockMultipartFile multipartImageFile = givenImageFile(image);

    @Before
    public void setup() {
        prepareMock(movieController);
    }

    @Test
    public void shouldAcceptFiles() throws Exception {

        mockMvc.perform(multipart(MOVIE_DATABASE + "/movie/save")
                .file(multipartJsonFile)
                .file(multipartImageFile))
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void getAllMovies() throws Exception {

        //given
        given(movieService.getAllMovies()).willReturn(givenMovies());

        //when
        val result = mockMvc.perform(MockMvcRequestBuilders.get(MOVIE_DATABASE +
                "/all-movies"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void getMovieById() throws Exception {
        //given
        given(movieService.getMovieById(any())).willReturn(givenMovie("1"));

        //when
        val result = mockMvc.perform(MockMvcRequestBuilders.get(MOVIE_DATABASE +
                "/movie/id/1"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void getMoviesByTitle() throws Exception {
        //given
        given(movieService.getMoviesByTitle(any())).willReturn(givenMovies());

        //when
        val result = mockMvc.perform(MockMvcRequestBuilders.get(MOVIE_DATABASE +
                "/movie/title/some title"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void deleteMovie() throws Exception {

        //when
        val result = mockMvc.perform(MockMvcRequestBuilders.delete(MOVIE_DATABASE +
                "/movie/delete/1"));
        //then
        result.andExpect(status().isOk());
    }

    @NonNull
    private MockMultipartFile givenJsonMovieFile(String movie) {
        return new MockMultipartFile("jsonMovieFile", "jsonMovieFile", "text/plain",
                movie.getBytes());
    }

    private MockMultipartFile givenImageFile(Binary image) {
        return new MockMultipartFile("image", "image", "text/plain",
                image.getData());
    }

    private List<Movie> givenMovies() {
        List<Movie> list = new ArrayList<>();
        list.add(givenMovie("1"));
        list.add(givenMovie("2"));
        list.add(givenMovie("3"));
        return list;
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