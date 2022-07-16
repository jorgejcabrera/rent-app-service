package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.configuration.ItemBeanDefinition
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderImpl
import com.fiuba.rent_app.domain.item.service.ItemLenderDoesNotExistException
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.order.service.ItemNotFoundException
import com.fiuba.rent_app.presentation.ExceptionHandlerAdvice
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponse
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.Assertions
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders

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

    private MockMvc mockMvc

    @Autowired
    private ItemController controller

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
        def handler = new ExceptionHandlerAdvice()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(handler)
                .build()
    }

    @Test
    void "when try to republish an item that does not exist then an exception must be thrown"() {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAnItemThatDoesNotExist()

        // WHEN
        def response = this.mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/item/1/publishing")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()

        // THEN
        assertEquals("""{"errorCode":"invalid_item","message":"The item does not exist."}""", response)
    }

    @Test
    void "when the item lender does not exist then an exception must be thrown"() {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAnItemLenderThatDoesNotExist()

        // WHEN
        def response = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()

        assertEquals("""{"errorCode":"invalid_borrower","message":"The account does not exist."}""", response)
    }

    @Test
    void "when republish a valid item then it must work ok"() {
        // GIVEN
        String anItemRepublishingBody = giveAnItemRepublishingBody()
        givenAnItemRepublished()

        // WHEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/item/1/publishing")
                .content(anItemRepublishingBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
    }

    @Test
    void "when execute a request to create an item then the amount to pay must be retrieved"() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody)

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        // THEN
        thenTheItemMustContainAnTotalAmountToPay(response)
    }


    @Test
    void "when execute a request to create an item then the item service must be used"() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody)

        // WHEN
        whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        // THEN
        thenTheUserServiceWasUsed()
    }

    @Test
    void "when execute a request to create an item then it must be created with an id"() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody)

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        //THEN
        thenTheItemMustContainAnId(response)
    }

    @Test
    void "when execute a request to create an item then it must contain a price"() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAServiceThatCreateAnItem(anItemCreationBody)

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

    private void givenAServiceThatCreateAnItem(String anItemCreationBody) throws JsonProcessingException {
        Item newItem = bodyAsItem(anItemCreationBody)
        whenever(itemService.create(any(), any())).thenReturn(newItem)
    }

    private Item bodyAsItem(String anItemCreationBody) throws JsonProcessingException {
        ItemCreationBody body = mapper.readValue(anItemCreationBody, ItemCreationBody.class);
        return new ItemBuilderImpl()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .assuranceCost(body.assuranceCost)
                .id(1L)
                .build()
    }

    private void thenTheItemMustContainAnTotalAmountToPay(String response) throws JsonProcessingException {
        ItemHttpResponse httpResponse = mapper.readValue(response, ItemHttpResponse.class)
        assertNotNull(httpResponse.totalToPay)
    }

    private void thenTheItemMustContainAnId(String response) throws JsonProcessingException {
        Item itemCreated = responseAsItem(response)
        assertNotNull(itemCreated.getId())
    }

    private Item responseAsItem(String response) throws JsonProcessingException {
        return mapper.readValue(response, Item.class)
    }

    private String givenAnItemCreationBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemCreationBody(
                description: "mi item",
                assuranceCost: valueOf(1000),
                price: valueOf(10.0),
                rentingDays: 2))
    }

    private String giveAnItemRepublishingBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemRepublishingBody(
                price: 10.0,
                rentingDays: 2
        ))
    }

    private void thenTheUserServiceWasUsed() {
        verify(itemService, times(1)).create(any(), any())
    }

    private void thenTheItemContainsAPrice(String response) throws JsonProcessingException {
        ItemHttpResponse itemCreated = mapper.readValue(response, ItemHttpResponse.class)
        assertEquals(valueOf(10.0), itemCreated.price)
    }

    void givenAnItemRepublished() {
        Item commonItem = TestItemFactory.availableDrillWith(1)
        whenever(itemService.republish(any(), any())).thenReturn(commonItem)
    }

    void givenAnItemLenderThatDoesNotExist() {
        whenever(itemService.create(any(), any())).thenThrow(new ItemLenderDoesNotExistException("The account does not exist."))
    }

    void givenAnItemThatDoesNotExist() {
        whenever(itemService.republish(any(), any())).thenThrow(new ItemNotFoundException("The item does not exist."))
    }

}
