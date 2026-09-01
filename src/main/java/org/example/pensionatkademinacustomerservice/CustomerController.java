package org.example.pensionatkademinacustomerservice;


import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping
    public String allCustomers(Model model) {
        List<CustomerDto> customerList = customerService.getAllCustomers();
        model.addAttribute("allCustomers", customerList);
        return "customer";
    }

    @PostMapping("add")
    public void addCustomer(@RequestBody String name) {
        customerService.addCustomer(CustomerDto.builder().name(name).build());
    }

    @PostMapping("edit/{id}")
    public void editCustomer(@PathVariable Long id, String name) {
        customerService.updateCustomerName(id, name);
    }

    @PostMapping("delete/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        String name = customerService.findCustomerById(id).getName();
        customerService.deleteCustomer(id);
        IO.print("Customer " + name +" has been deleted.");
    }

}
