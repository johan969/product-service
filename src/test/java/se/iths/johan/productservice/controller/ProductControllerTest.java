package se.iths.johan.productservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.johan.productservice.dto.ProductRequestDto;
import se.iths.johan.productservice.service.ProductService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;


    //det gick inte med Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        ProductRequestDto productRequestDto = new ProductRequestDto("johan","beskrivning",new BigDecimal("100.00"),5);
        productService.create(productRequestDto);

    }

    @Test
    void findAllTest() throws Exception{
    mockMvc.perform(get("/products")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isOk());
    }
    @Test
    void unauthorizedFindAllTest() throws Exception{
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void findByIdTest() throws Exception{
        mockMvc.perform(get("/products/{id}",1)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("johan"));

    }
    @Test
    void unauthorizedFindByIdTest() throws Exception{
        mockMvc.perform(get("/products/{id}",1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTest() throws Exception{
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "frank",
                "annan beskrivning",
                new BigDecimal("100.00"),
                5);

        mockMvc.perform(post("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("frank"));

    }

    @Test
    void forbiddenCreateTest() throws Exception{
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "frank",
                "annan beskrivning",
                new BigDecimal("100.00"),
                5);


        mockMvc.perform(post("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedCreateTest() throws Exception{
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "frank",
                "annan beskrivning",
                new BigDecimal("100.00"),
                5);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void deleteTest() throws Exception{
        mockMvc.perform(delete("/products/{id}",1)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());


    }

    @Test
    void forbiddenDeleteTest()  throws Exception{
        mockMvc.perform(delete("/products/{id}",1)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }
    @Test
    void unauthorizedDeleteTest() throws Exception{
        mockMvc.perform(delete("/products/{id}",1))
                .andExpect(status().isUnauthorized());

    }

}
