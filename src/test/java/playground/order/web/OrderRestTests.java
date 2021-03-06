package playground.order.web;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author KOGA, Yu
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class OrderRestTests {

    final MockMvc mvc;

    @Test
    void list() throws Exception {
        this.mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        File content = new ClassPathResource("order.json").getFile();
        this.mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.contentOf(content, "UTF-8")))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
