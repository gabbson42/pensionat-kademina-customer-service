package org.example.pensionatkademinacustomerservice;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public List<CustomerDto> getAllCustomers(){
        return customerRepository.findAll().stream().map(this::customerToCustomerDto).toList();
    }

    @Override
    public CustomerDto customerToCustomerDto(Customer customer) {
        return CustomerDto.builder().id(customer.getId()).name(customer.getName()).build();
    }

    @Override
    public Customer customerDtoToCustomer(CustomerDto customerDto) {
        return Customer.builder().id(customerDto.getId()).name(customerDto.getName()).build();
    }


    @Override
    public void addCustomer(String name) {
        customerRepository.save(Customer.builder().name(name).build());
    }

    @Override
    public void updateCustomerName(Long id, String newName) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setName(newName);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        return customerToCustomerDto(customerRepository.findById(id).orElseThrow());
    }

    @Override
    public void deleteCustomer (Long customerId){
        customerRepository.deleteById(customerId);
    }

}

