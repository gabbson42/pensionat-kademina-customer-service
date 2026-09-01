package org.example.pensionatkademinacustomerservice;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
    void addCustomer(CustomerDto customerDto);
    void updateCustomerName(Long id, String newName);
    CustomerDto findCustomerById(Long id);
    void deleteCustomer (Long customerId);


}
