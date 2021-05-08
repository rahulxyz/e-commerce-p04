package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private User user;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        user = new User();
        user.setId(0);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(user));
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Rahul");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User body = response.getBody();
        assertNotNull(body);
        assertEquals(0,body.getId());
        assertEquals("Rahul",body.getUsername());
        assertEquals("thisIsHashed",body.getPassword());
    }

    @Test
    public void verify_findById() {
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertEquals(0, userResponseEntity.getBody().getId());
        userResponseEntity = userController.findById(1L);
        assertEquals(404,userResponseEntity.getStatusCodeValue());
    }

    @Test
    public void verify_password_validation() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Rahul");
        userRequest.setPassword("rahul");
        userRequest.setConfirmPassword("rahul");
        ResponseEntity<User> userResponseEntity = userController.createUser(userRequest);
        assertEquals(400, userResponseEntity.getStatusCodeValue());

    }
}
