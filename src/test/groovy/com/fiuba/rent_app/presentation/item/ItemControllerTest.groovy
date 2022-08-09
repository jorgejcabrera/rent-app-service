package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.configuration.ItemBeanDefinition
import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.exception.ItemLenderDoesNotExistException
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import com.fiuba.rent_app.presentation.ExceptionHandlerAdvice
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponse
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static java.math.BigDecimal.valueOf
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.mockito.ArgumentMatchers.any
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ItemController.class])
@AutoConfigureMockMvc
@ContextConfiguration(classes = [ItemController.class, ItemBeanDefinition.class])
class ItemControllerTest {

    private MockMvc mockMvc

    @Autowired
    private ItemController controller

    @Autowired
    private ItemService itemService

    @Autowired
    private ItemHttpResponseFactory responseFactory

    @Autowired
    private ObjectMapper mapper

    @MockBean
    private JpaItemRepository jpaItemRepository

    @MockBean
    private JpaAccountRepository accountRepository

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
    void "when try to create an item with a lender that not exist,then an exception must be thrown"() {
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
    void "when the item does not have a rent duration, then it must not be valid"() {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody(new ItemCreationBody(description: "Ps5", title: "Ps5", price: 20.0))
        givenAnExistingAccount()

        // WHEN
        def response = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString()

        assertEquals("""{"errorCode":"invalid_rent_duration","message":"A rent duration must be specified"}""", response)
    }

    @Test
    void "when the item does not have a title, then it must not be valid"() {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody(new ItemCreationBody(description: "Ps5", price: 20.0, rentingDays: 1))
        givenAnExistingAccount()

        // WHEN
        def response = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString()

        assertEquals("""{"errorCode":"item_title_empty","message":"Title must be not empty"}""", response)
    }

    @Test
    void "when the item does not have a price, then it must not be valid"() {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody(new ItemCreationBody(description: "Ps5", title: "Ps5", rentingDays: 1))
        givenAnExistingAccount()

        // WHEN
        def response = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/1/item")
                .content(anItemCreationBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString()

        assertEquals("""{"errorCode":"item_price_empty","message":"Price must be not empty"}""", response)
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
        givenAnExistingAccount()

        // WHEN
        String response = whenExecuteTheRequestToCreateAnItem(anItemCreationBody)

        // THEN
        thenTheItemMustContainAnTotalAmountToPay(response)
    }

    @Test
    void "when execute a request to create an item then it must contain a price"() throws Exception {
        // GIVEN
        String anItemCreationBody = givenAnItemCreationBody()
        givenAnExistingAccount()

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

    private void thenTheItemMustContainAnTotalAmountToPay(String response) throws JsonProcessingException {
        ItemHttpResponse httpResponse = mapper.readValue(response, ItemHttpResponse.class)
        assertNotNull(httpResponse.totalToPay)
    }

    private String givenAnItemCreationBody(ItemCreationBody item = new ItemCreationBody(
            description: "mi item",
            title: "Item",
            assuranceCost: valueOf(1000),
            price: valueOf(10.0),
            rentingDays: 2)) throws JsonProcessingException {
        return mapper.writeValueAsString(item)
    }

    private String giveAnItemRepublishingBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new ItemRepublishingBody(
                price: 10.0,
                rentingDays: 2
        ))
    }

    private void thenTheItemContainsAPrice(String response) throws JsonProcessingException {
        ItemHttpResponse itemCreated = mapper.readValue(response, ItemHttpResponse.class)
        assertEquals(valueOf(10.0), itemCreated.price)
    }

    private void givenAnItemRepublished() {
        Item commonItem = TestItemFactory.availableDrillWith(1)
        whenever(jpaItemRepository.findById(any())).thenReturn(Optional.of(commonItem))
    }

    private void givenAnItemLenderThatDoesNotExist() {
        whenever(accountRepository.findById(any())).thenThrow(new ItemLenderDoesNotExistException("The account does not exist."))
    }

    private void givenAnItemThatDoesNotExist() {
        whenever(jpaItemRepository.findById(any())).thenThrow(new ItemNotFoundException("The item does not exist."))
    }

    private void givenAnExistingAccount() {
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(new Account(id: 1L, email: "cabrerajjorge@gmail.com")))
    }

}
