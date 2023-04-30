package org.rmatwell.instock.gpu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.rmatwell.instock.gpu.controller.ListingController;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private ListingController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
