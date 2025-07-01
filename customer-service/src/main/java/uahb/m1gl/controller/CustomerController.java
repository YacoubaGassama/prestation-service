package uahb.m1gl.controller;

//import com.ecole221.prestation.dto.CustomerResponse;
//import com.ecole221.prestation.helper.CustomerHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uahb.m1gl.dto.CustomerResponse;
import uahb.m1gl.helper.CustomerHelper;

//@CrossOrigin
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerHelper customerHelper;

    public CustomerController(CustomerHelper customerHelper) {
        this.customerHelper = customerHelper;
    }

    @GetMapping("/{tel}")
    public CustomerResponse getCustomer(@PathVariable String tel){
        return customerHelper.getCustomer(tel);
    }

    @GetMapping("/find/{id}")
    public CustomerResponse getCustomerById(@PathVariable String id){
        return customerHelper.getCustomerById(id);
    }
}
