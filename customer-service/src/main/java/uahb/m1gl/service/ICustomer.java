package uahb.m1gl.service;

import uahb.m1gl.model.Customer;
public interface ICustomer {
    Customer save(Customer customer);
    Customer find(long id);
    Customer findByTel(String tel);
}
