package uahb.m1gl.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uahb.m1gl.model.Customer;

@Service
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByTel(String tel);
}
