package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> list;

    private String ITEM_NAME = "bulbasaur model";

    @Before
    public void init(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        list = new ArrayList<Item>();

        Item item1 = new Item();
        item1.setId(0L);
        item1.setName(ITEM_NAME);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item1));

        list.add(item1);
        when(itemRepository.findByName(ITEM_NAME)).thenReturn(list);
        when(itemRepository.findAll()).thenReturn(list);
    }

    @Test
    public void verify_getItemById(){
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void verify_getItemsByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName(ITEM_NAME);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void verify_getItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
