package org.example.pensionatkademinacustomerservice;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping
    public List<CustomerDto> allCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("add")
    public void addCustomer(@RequestBody String name) {
        customerService.addCustomer(name);
    }

    @PostMapping("edit/{id}")
    public CustomerDto editCustomer(@RequestBody CustomerDto customer) {

        customerService.updateCustomerName(customer.getId(), customer.getName());
        return customerService.findCustomerById(customer.getId());
    }

    @PostMapping("delete/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        String name = customerService.findCustomerById(id).getName();
        customerService.deleteCustomer(id);
        IO.print("Customer " + name +" has been deleted.");
    }

    @GetMapping("/{id}")
    public CustomerDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

}
