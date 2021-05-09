package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderContollerTest {
    private OrderController orderController;
    private UserRepository userRepository= mock(UserRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);
    private UserOrder userOrderMock = org.mockito.Mockito.mock(UserOrder.class);

    private User user ;
    private Item item ;
    private Cart cart;
    private List<Item> list_item;

    @Before
    public void init(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        user = new User();
        item = new Item();
        cart = new Cart();
        list_item = new ArrayList<Item>();

        user.setId(0L);
        user.setUsername("Rahul");
        user.setPassword("rahul@123");
        when(userRepository.findByUsername("Rahul")).thenReturn(user);

        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setName("Pikachu model");
        item.setDescription("Pikachu is a pokemon");
        list_item.add(item);

        cart.setId(0L);
        cart.setItems(list_item);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(2.99));
        user.setCart(cart);
    }

    @Test
    public void verify_submit(){
        ResponseEntity<UserOrder> response = orderController.submit("Rahul");
        assertEquals(BigDecimal.valueOf(2.99), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(null, response.getBody().getId());
    }

//    @Test
//    public void verify_getOrdersForUser(){
//        orderController.submit("Rahul");
//        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Rahul");
//        assertEquals(1, response.getBody().size());
//        assertEquals(2.99, response.getBody().get(0).getTotal());
//    }
}
