package org.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UiTest {

    @Test
    void firstTest() {
        System.out.println("This is a test.");
    }

    @Test
    @Tag("smoke")
    void smokeTest() {
        System.out.println("This is a smoke test.");
    }
}
