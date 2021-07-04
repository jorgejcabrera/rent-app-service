package org.fiuba.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fiuba.domain.Item;
import org.fiuba.domain.ItemBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemBuilder itemBuilder;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void when_create_an_item_then_it_must_be_created_with_an_id() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody();

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody);

        //THEN
        thenTheItemMustContainAnId(response);

    }

    @NotNull
    private String whenExecuteTheRequestToCreateAnItem(String anItemCreationBody) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private void thenTheItemMustContainAnId(String response) throws JsonProcessingException {
        Item itemCreated = mapper.readValue(response, Item.class);
        assertNotNull(itemCreated.getId());

    }

    private String givenAnItemCreationBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemCreationBody("mi item",
                BigDecimal.valueOf(10.0),
                2));
    }
}