package com.fiuba.rent_app.presentation.order


import com.fiuba.rent_app.configuration.OrderBeanDefinition
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.service.OrderService
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponse
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponseFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.time.LocalDateTime

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OrderController.class])
@AutoConfigureMockMvc
@ContextConfiguration(classes = [OrderController.class, OrderBeanDefinition.class])
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OrderService orderService

    @MockBean
    private OrderHttpResponseFactory responseFactory

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void when_execute_a_request_to_create_an_order_then_the_order_service_must_be_used() {
        // GIVEN
        givenAnOrderCreated()
        givenASuccessfullyHttpResponse()

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
        return new Order(1L, 1L, LocalDateTime.now().plusDays(2), null)
    }

    void givenASuccessfullyHttpResponse() {
        whenever(responseFactory.from(any()))
                .thenReturn(ResponseEntity.status(CREATED)
                        .body(new OrderHttpResponse()))
    }

    void thenTheServiceWasUsed() {
        verify(orderService, times(1)).createFor(any(), any())
    }
}


