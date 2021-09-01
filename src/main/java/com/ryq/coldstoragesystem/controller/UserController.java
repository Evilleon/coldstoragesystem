package com.ryq.coldstoragesystem.controller;

import com.ryq.coldstoragesystem.bean.Role;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.bean.UserRole;
import com.ryq.coldstoragesystem.service.RoleService;
import com.ryq.coldstoragesystem.service.UserRoleService;
import com.ryq.coldstoragesystem.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;


    @GetMapping("/toinsertuser")
    public String toInsertUser(Model model){

        //model.addAttribute("currentdata",user);
        List<Role> allRoleList = roleService.getAllRoleList();

        for(Role role:allRoleList){
            if (role.getName().equals("admin")){
                role.setComment("管理员");
            }
            if (role.getName().equals("client")){
                role.setComment("客户");
            }
            if (role.getName().equals("inspector")){
                role.setComment("审查员");
            }
        }

        model.addAttribute("role",allRoleList);

        System.out.println(allRoleList);

        return "AddUser";
    }

    @PostMapping("/insertuser")
    public String insertUser(User user,@RequestParam(value = "role") List<String> role){
        System.out.println(user);

        //获得相应角色
        System.out.println(role);

        StringBuilder flag = new StringBuilder();
        String sources = "0123456789";
        Random rand = new Random();
        for (int j = 0; j < 5; j++)
        {
            flag.append(sources.charAt(rand.nextInt(9)));
        }
        System.out.println(flag.toString());
        user.setIdentifier(flag.toString());
        user.setStatus("1");
        int userId = userService.insertUser(user);
        System.out.println("+++++++++++++++++++++8");
        System.out.println(userId);

        for (String each_role:role){

            UserRole userRole=new UserRole();
            userRole.setRoleId(Integer.valueOf(each_role));
            userRole.setUserId(userId);
            userRoleService.insertUserRole(userRole);

        }
        //

        return "index";
    }

    @GetMapping("/selectalluser")
    public String selectAllUser(Model model){

        List<User> users = userService.selectAllUser();
        for (User user:users){
            if (user.getStatus().equals("1")){
                user.setStatusText("正常");
            }else{
                user.setStatusText("锁定");
            }
        }

        model.addAttribute("users",users);
        return "DisplayAllUsers";
    }

    @GetMapping("/locked")
    public String locked(String id){

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setStatus("2");

        userService.updateStatusById(user);
        User user1 = userService.selectById(Integer.valueOf(id));
        User user2 = (User) SecurityUtils.getSubject().getPrincipal();
        if (user1.getUserName().equals(user2.getUserName())){
            return "redirect:index.html";
        }
        return "redirect:/selectalluser";
    }

    @GetMapping("/unlocked")
    public String unLocked(String id){

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setStatus("1");

        userService.updateStatusById(user);
        return "redirect:/selectalluser";
    }

    @GetMapping("/delete")
    public String delete(String id){


        userService.deleteByIdBoolean(Integer.valueOf(id));

        return "redirect:/selectalluser";
    }

}
