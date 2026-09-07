package org.example.pensionatkademinacustomerservice;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CustomerServiceIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;

    private Customer savedCustomer;

    @Container
    @ServiceConnection
    static MySQLContainer db =
            new MySQLContainer(
                    "mysql:8");


    @BeforeEach
    void setUp() {

        customerRepository.deleteAll();
        savedCustomer = customerRepository.save(Customer.builder().name("Filip").build());

    }

    @Test
    void getAllCustomers() throws Exception {
        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$[0].name").value(savedCustomer.getName()));
    }


    @Test
    void getCustomerById() throws Exception {
        mockMvc.perform(get("/customer/" + savedCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.name").value(savedCustomer.getName()));
    }

    @Test
    void addCustomer() throws Exception {
        mockMvc.perform(post("/customer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Gabriel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Gabriel"));
    }

    @Test
    void updateCustomer() throws Exception {

        CustomerDto customer = CustomerDto.builder().id(savedCustomer.getId()).name("Gabriel").build();
        mockMvc.perform(post("/customer/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.name").value("Gabriel"));

    }

    @Test
    void deleteCustomer() throws Exception {

        mockMvc.perform(post("/customer/delete/" + savedCustomer.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/customer/" + savedCustomer.getId()))
                .andExpect(status().isNotFound());

    }

}

