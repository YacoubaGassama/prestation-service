package uahb.m1gl.helper;

//import com.ecole221.prestation.exception.CustomerServiceNotFoundException;

import org.springframework.stereotype.Component;
import uahb.m1gl.dto.CustomerResponse;
import uahb.m1gl.exception.CustomerServiceNotFoundException;
import uahb.m1gl.mapper.Mapper;
import uahb.m1gl.model.Customer;
import uahb.m1gl.service.ICustomer;

@Component
public class CustomerHelper {
    private final ICustomer iCustomer;
    private final Mapper mapper;

    public CustomerHelper(ICustomer iCustomer, Mapper mapper) {
        this.iCustomer = iCustomer;
        this.mapper = mapper;
    }

    public CustomerResponse getCustomer(String tel){
        Customer customer = iCustomer.findByTel(tel);
        if(customer == null){
            throw new CustomerServiceNotFoundException("pas de customer avec le tel:"+tel);
        }
        return  mapper.modelMapper().map(customer, CustomerResponse.class);
    }
    public CustomerResponse getCustomerById(String id){
        Customer customer = iCustomer.find(Long.parseLong(id));
        if(customer == null){
            throw new CustomerServiceNotFoundException("Customer avec id "+id+" introuvable!");
        }
        return  mapper.modelMapper().map(customer, CustomerResponse.class);
    }
}
