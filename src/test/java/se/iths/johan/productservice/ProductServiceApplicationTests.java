package se.iths.johan.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import se.iths.johan.productservice.config.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
