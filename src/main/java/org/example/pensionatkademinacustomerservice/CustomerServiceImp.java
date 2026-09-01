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
    public void addCustomer(CustomerDto customerDto) {
        Customer customer = customerDtoToCustomer(customerDto);
        customerRepository.save(customer);
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

        //Ska kolla med order service om det finns bokning på kunden innan den tar bort.
        boolean hasBooking = bookingRepository.existsByCustomer_Id(customerId);

        if (hasBooking){
            throw new IllegalArgumentException();
        }

        customerRepository.deleteById(customerId);
    }

}

