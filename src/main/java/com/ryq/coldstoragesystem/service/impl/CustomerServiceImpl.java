package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.Customer;
import com.ryq.coldstoragesystem.mapper.CustomerMapper;
import com.ryq.coldstoragesystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public boolean insertCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public boolean updateCustomerById(Customer customer) {
        return customerMapper.updateCustomerById(customer);
    }

    @Override
    public boolean deleteCustomById(Integer id) {
        return customerMapper.deleteCustomById(id);
    }

    @Override
    public Customer selectCustomerByIdCustom(Integer id) {
        return customerMapper.selectCustomerByIdCustom(id);
    }

    @Override
    public List<Customer> selectAllCustomerCustom(String loginUserName) {
        return customerMapper.selectAllCustomerCustom(loginUserName);
    }

    //根据name查询客户
    @Override
    public List<Customer> selectAllByName(String name) {
        return customerMapper.selectAllByName(name);
    }
}
