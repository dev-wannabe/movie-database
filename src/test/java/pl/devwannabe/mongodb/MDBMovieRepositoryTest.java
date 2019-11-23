package pl.devwannabe.mongodb;

import lombok.val;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pl.devwannabe.domain.Movie;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest()
public class MDBMovieRepositoryTest {

    @Autowired
    private MDBMovieRepository cut;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private MovieMongoRepository movieMongoRepositoryRepo;

    @Before
    public void setup() {
        //movieMongoRepositoryRepo.deleteAll();
    }

    @Ignore
    @Test
    public void shouldStoreMovie() {

        //given
        val movie = givenMovie();

        //and
        cut.saveMovie(movie);

        //when
        val result = cut.findOne("12");

        //then
        assertThat(result).isEqualTo(movie);

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