package com.ryq.auth.controller.sign;

import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.service.LoginService;
import com.ryq.coldstoragesystem.utils.MyAuthenticationToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    //处理登录检测逻辑
    @PostMapping("/login")
    public String login(User userInformation, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request){
        String userName = userInformation.getUserName();
        String password = userInformation.getPassword();
        String status = userInformation.getStatus();
        /**
         * 开始认证
         */
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        //自定义Token
        MyAuthenticationToken token = new MyAuthenticationToken(userName, password.toCharArray(),status);
        //执行登录方法
        try {
            subject.login(token);
            model.addAttribute("username",userName);
            HttpSession session = request.getSession(true);
            session.setAttribute("username",userName);
            return "index";
        }catch (LockedAccountException lae){
            redirectAttributes.addAttribute("loginErrMsg","账号被锁定，请联系管理员");
            return "redirect:index.html";
        } catch (UnknownAccountException uae){
            redirectAttributes.addAttribute("loginErrMsg","用户名不存在");
            return "redirect:index.html";
        } catch (IncorrectCredentialsException ice){
            redirectAttributes.addAttribute("loginErrMsg","密码错误");
            return "redirect:index.html";
        }
    }

    @GetMapping("/tologin")
    public String toLogin(){
        return "redirect:/index.html";
    }

    @GetMapping("/noAuth")
    public String noAuth(){
        return "NoAuth";
    }

    //去修改密码页面
    @GetMapping("/changepassword")
    public String changePassword(Model model){

        User user = (User)SecurityUtils.getSubject().getPrincipal();

        User user1 = loginService.selectByUsername(user.getUserName());

        model.addAttribute("userName",user1.getUserName());

        return "ChangePassword";
    }

    @PostMapping("/querysamename")
    @ResponseBody
    public Boolean querySameName(String userName){

        User user = loginService.selectByUsername(userName);
        return user!=null;

    }

    @PostMapping("/revisepassword")
    public String revisePassword(User user){


        Boolean aBoolean = loginService.updatePasswordByUserName(user);

        return "redirect:/tologin";
    }

    @GetMapping("/tests")
    public String test(){
        return "test";
    }

    @GetMapping("/returntoindex")
    public String returnToIndex(){
        return "index";
    }
}
