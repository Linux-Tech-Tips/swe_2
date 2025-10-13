package com.example.FACTS;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FactsApplicationTests {

    @Autowired
    private FactsApplication facts;

   @Test
   void contextLoads() throws Exception {
        assertThat(facts).isNotNull();
   }

}
