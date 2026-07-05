package org.northernarc.unittesting.service;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorServiceTest {
    private static Impl impl;

    @BeforeAll
    public static void init(){
        System.out.println("Initializing test environment...");
        impl = new Impl();
    }
    @BeforeEach
    public void setUp() {
        System.err.println("Setting up test environment...");
    }

    @AfterEach
    public void tearDown() {
        System.err.println("Tearing down test environment...");

    }
    @AfterAll
    public static void cleanup(){
        System.out.println("Cleaning up test environment...");
        impl = null;
    }

    @Test
    public void testAdd(){
        int result = impl.add(2,3);
        assertEquals(5,result);
    }

    @Test
    public void testSub(){
        int result = impl.sub(5,3);
        assertEquals(2,result);
    }

    @Test
    public void testMul(){
        int result = impl.mul(2,3);
        assertEquals(6,result);
    }

    @Test
    public void testDivide(){
        int result = impl.divide(6,3);
        assertEquals(2,result);
    }
}
