package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Cart cart = new Cart();
    private User user = new User();
    private Item item = new Item();

    @Before
    public void init() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        user.setId(0L);
        user.setUsername("Rahul");
        user.setPassword("rahul@123");
        user.setCart(cart);

        item.setId(0L);
        item.setName("Pikachu model");
        item.setPrice(BigDecimal.valueOf(2.99));

        when(userRepository.findByUsername("Rahul")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(item));
    }

    @Test
    public void verify_addTocart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(8.97), response.getBody().getTotal());
    }

    @Test
    public void verify_removeFromcart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        //remove one item
        ModifyCartRequest cartRequestModified = new ModifyCartRequest();
        cartRequestModified.setItemId(0L);
        cartRequestModified.setQuantity(1);
        cartRequestModified.setUsername(user.getUsername());
        response = cartController.removeFromcart(cartRequestModified);
        assertEquals(BigDecimal.valueOf(2.99), response.getBody().getTotal());
    }
}
