package uahb.m1gl.service;


import org.springframework.stereotype.Component;
import uahb.m1gl.model.Customer;
import uahb.m1gl.repository.CustomerRepository;

@Component
public class CustomerImpl implements ICustomer{
    private final CustomerRepository customerRepository;

    public CustomerImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer find(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer findByTel(String tel) {
        return customerRepository.findByTel(tel);
    }
}
