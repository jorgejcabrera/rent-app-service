package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiuba.rent_app.configuration.ItemBeanDefinition
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderAdapter
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static java.math.BigDecimal.valueOf
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ItemController.class])
@AutoConfigureMockMvc
@ContextConfiguration(classes = [ItemController.class, ItemBeanDefinition.class])
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private JpaItemRepository jpaItemRepository

    @MockBean
    private ItemService itemService

    @Autowired
    private ItemHttpResponseFactory responseFactory

    @Autowired
    private ObjectMapper mapper

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void when_execute_a_request_to_create_an_item_then_the_item_service_must_be_used() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody, 1L)

        // WHEN
        whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        // THEN
        thenTheUserServiceWasUsed()
    }

    @Test
    void when_execute_a_request_to_create_an_item_then_it_must_be_created_with_an_id() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody, 1L)

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        //THEN
        thenTheItemMustContainAnId(response)
    }

    @Test
    void when_execute_a_request_to_create_an_item_then_it_must_contain_a_price() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody, 1L)

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        //THEN
        thenTheItemContainsAPrice(response)
    }

    @NotNull
    private String whenExecuteTheRequestToCreateAnItem(String anItemCreationBody) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
    }


    private void givenAServiceThatCreateAnItem(String anItemCreationBody, Long renterId) throws JsonProcessingException {
        Item newItem = asItem(anItemCreationBody, renterId)
        whenever(itemService.create(any(), any())).thenReturn(newItem)
    }

    private Item asItem(String anItemCreationBody, Long renterId) throws JsonProcessingException {
        ItemCreationBody body = mapper.readValue(anItemCreationBody, ItemCreationBody.class);
        return new ItemBuilderAdapter()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .renter(renterId)
                .id(1L)
                .build()
    }

    private void thenTheItemMustContainAnId(String response) throws JsonProcessingException {
        Item itemCreated = asItem(response)
        assertNotNull(itemCreated.getId())
    }

    private Item asItem(String response) throws JsonProcessingException {
        return mapper.readValue(response, Item.class)
    }

    private String givenAnItemCreationBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemCreationBody(
                description: "mi item",
                price: valueOf(10.0),
                rentingDays: 2))
    }

    private void thenTheUserServiceWasUsed() {
        verify(itemService, times(1)).create(any(), any())
    }

    private void thenTheItemContainsAPrice(String response) throws JsonProcessingException {
        Item itemCreated = asItem(response);
        assertEquals(valueOf(10.0), itemCreated.getPrice())
    }
}
