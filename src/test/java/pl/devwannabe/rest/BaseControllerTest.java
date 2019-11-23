package pl.devwannabe.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class BaseControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void prepareMock(Object controller) {
        val mapper = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(mapper)
                .build();
    }
}
