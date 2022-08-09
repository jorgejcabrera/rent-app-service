package com.fiuba.rent_app.presentation.order

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.configuration.OrderBeanDefinition
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.ItemIsNotAvailableForOrderingException
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import com.fiuba.rent_app.domain.order.service.OrderService
import com.fiuba.rent_app.presentation.ExceptionHandlerAdvice
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponseFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OrderController.class])
@AutoConfigureMockMvc
@ContextConfiguration(classes = [OrderController.class, OrderBeanDefinition.class])
class OrderControllerTest {

    private MockMvc mockMvc

    @Autowired
    private OrderController controller

    @MockBean
    private OrderService orderService

    @Autowired
    private OrderHttpResponseFactory responseFactory

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this)
        def handler = new ExceptionHandlerAdvice()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(handler)
                .build()
    }

    @Test
    void "when the item borrower is the item lender then an exception must be raise"() {
        // GIVEN
        givenAnItemWithAnInvalidBorrower()

        // WHEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/item/1/order")
                .header("x-caller-id", 2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
    }

    @Test
    void "when try to rent an item that does not exist then it must fail"() {
        // GIVEN
        givenAnItemThatDoesNotExist()

        // WHEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/item/1/order")
                .header("x-caller-id", 2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
    }

    @Test
    void "when try to rent an item not available then it must fail"() {
        // GIVEN
        givenAnItemAlreadyRented()

        // WHEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/item/1/order")
                .header("x-caller-id", 2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
    }

    @Test
    void "when execute a request to create an order then the order service must be used"() {
        // GIVEN
        givenAnOrderCreated()

        // WHEN
        whenExecuteARequestToCreateAnOrder()

        // THEN
        thenTheServiceWasUsed()
    }

    private ResultActions whenExecuteARequestToCreateAnOrder() {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/item/1/order")
                .header("x-caller-id", 2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
    }


    void givenAnOrderCreated() {
        Order order = givenAnOrder()
        whenever(orderService.createFor(any(), any())).thenReturn(order)
    }

    private static Order givenAnOrder() {
        def item = TestItemFactory.rentedDrillWith(2)
        return item.orders[0]
    }

    void thenTheServiceWasUsed() {
        verify(orderService, times(1)).createFor(any(), any())
    }

    void givenAnItemAlreadyRented() {
        whenever(orderService.createFor(any(), any())).thenThrow(new ItemIsNotAvailableForOrderingException("The item is not avilable."))
    }

    void givenAnItemThatDoesNotExist() {
        whenever(orderService.createFor(any(), any())).thenThrow(new ItemNotFoundException("The item is not avilable."))
    }

    void givenAnItemWithAnInvalidBorrower() {
        whenever(orderService.createFor(any(), any())).thenThrow(new InvalidCallerException("The borrower can't be the owner of the item"))
    }
}


