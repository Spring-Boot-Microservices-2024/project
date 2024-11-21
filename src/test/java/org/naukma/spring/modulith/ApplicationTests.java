package org.naukma.spring.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        var modules = ApplicationModules.of(Application.class);
        modules.forEach(System.out::println);
    }
}
