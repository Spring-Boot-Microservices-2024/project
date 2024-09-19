package org.naukma.spring.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        var modules = ApplicationModules.of(Application.class);
        modules.forEach(System.out::println);
    }

    @Test
    void verify() {
        ApplicationModules.of(Application.class).verify();
    }

    @Test
    void writeDocumentationSnippets() {
        var modules = ApplicationModules.of(Application.class).verify();

        new Documenter(modules, "documentation")
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml().writeAggregatingDocument();
    }
}
