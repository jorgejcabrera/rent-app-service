package org.fiuba.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fiuba.domain.item.Item;
import org.fiuba.domain.item.ItemBuilder;
import org.fiuba.domain.item.ItemService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ItemBuilder itemBuilder;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void when_execute_a_request_to_create_an_item_then_the_item_service_must_be_used() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody();

        // WHEN
        whenExecuteTheRequestToCreateAnItem(anItemCreationBody);

        // THEN
        thenTheUserServiceWasUsed();
    }

    @Test
    public void when_execute_a_request_to_create_an_item_then_it_must_be_created_with_an_id() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody();
        givenAServiceThatCreateAnItem(anItemCreationBody, 1L);

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody);

        //THEN
        thenTheItemMustContainAnId(response);
    }


    @Test
    public void when_execute_a_request_to_create_an_item_then_it_must_contain_a_price() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody();
        givenAServiceThatCreateAnItem(anItemCreationBody, 1L);

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody);

        //THEN
        thenTheItemContainsAPrice(response);
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


    private void givenAServiceThatCreateAnItem(String anItemCreationBody, Long renterId) throws JsonProcessingException {
        Item newItem = asItem(anItemCreationBody, renterId);
        whenever(itemService.create(any(), any())).thenReturn(newItem);
    }

    private Item asItem(String anItemCreationBody, Long renterId) throws JsonProcessingException {
        ItemCreationBody body = mapper.readValue(anItemCreationBody, ItemCreationBody.class);
        return itemBuilder
                .price(body.price)
                .rentDaysDuration(body.rentDaysDuration)
                .description(body.description)
                .renter(renterId)
                .build();
    }

    private void thenTheItemMustContainAnId(String response) throws JsonProcessingException {
        Item itemCreated = asItem(response);
        assertNotNull(itemCreated.getId());
    }

    private Item asItem(String response) throws JsonProcessingException {
        return mapper.readValue(response, Item.class);
    }

    private String givenAnItemCreationBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemCreationBody(
                "mi item",
                BigDecimal.valueOf(10.0),
                2));
    }

    private void thenTheUserServiceWasUsed() {
        verify(itemService, times(1)).create(any(), any());
    }

    private void thenTheItemContainsAPrice(String response) throws JsonProcessingException {
        Item itemCreated = asItem(response);
        assertEquals(BigDecimal.valueOf(10.0), itemCreated.getPrice());
    }
}