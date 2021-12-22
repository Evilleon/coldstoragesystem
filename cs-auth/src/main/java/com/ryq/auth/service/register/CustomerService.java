package com.ryq.auth.service.register;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService extends IService<Customer> {

    //插入客户
    public boolean insertCustomer(Customer customer);

    //根据id修改客户
    public boolean updateCustomerById(Customer customer);

    //根据id删除客户
    public boolean deleteCustomById(Integer id);

    //根据id查询客户
    public Customer selectCustomerByIdCustom(Integer id);

    //查询登录者全部客户
    public List<Customer> selectAllCustomerCustom(String loginUserName);

    //根据name查询客户
    public List<Customer> selectAllByName(String name);
}
