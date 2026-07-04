package com.example.lab4;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void productConstructor_setsCorrectValues() {
        Product product = new Product("1", "Laptop", 999.99);

        assertEquals("1", product.getProductId());
        assertEquals("Laptop", product.getProductName());
        assertEquals(999.99, product.getProductPrice(), 0.001);
    }

    @Test
    public void defaultConstructor_createsEmptyProduct() {
        Product product = new Product();

        assertNull(product.getProductId());
        assertNull(product.getProductName());
        assertEquals(0.0, product.getProductPrice(), 0.001);
    }

    @Test
    public void productStoresDecimalPrice() {
        Product product = new Product("2", "Phone", 499.50);

        assertEquals(499.50, product.getProductPrice(), 0.001);
    }
}