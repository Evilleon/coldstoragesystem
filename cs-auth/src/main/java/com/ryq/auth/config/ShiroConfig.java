package com.ryq.auth.config;

import com.ryq.coldstoragesystem.utils.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro配置类
 * @Qualifier 通知指定要寻找的注入
 */

@Configuration
public class ShiroConfig {

    //创建 ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //1、设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        /**
         *  anon：无须认证即可访问
         *  authc：必须认证才可访问
         *  user：使用到remeberMe的功能才可用到
         *  perms：必须得到资源权限才可以访问
         *  roles：必须得到角色权限才可访问
         */
        Map<String, String> filterMaps = new LinkedHashMap<>();

        //设置拦截
        //放行导向重定向登录页面
        filterMaps.put("/tologin","anon");
        //放行localhost:8080
        filterMaps.put("/","anon");
        //放行重定向页面
        filterMaps.put("/index.html","anon");
        //放行登录
        filterMaps.put("/login","anon");


        //设置授权拦截器
        filterMaps.put("/addroominformation","perms[admin:addroominformation]");
        filterMaps.put("/roominformationdisplay","perms[client:roominformationdisplay]");
        filterMaps.put("/addcoldstorageitem","perms[inspector:addcoldstorageitem]");

        filterMaps.put("/manualaddcolditem","perms[client:manualaddcolditem]");
        filterMaps.put("/excoldstorageitem","perms[client:excoldstorageitem]");
        filterMaps.put("/addcustomlabel","perms[admin:addcustomlabel]");

        filterMaps.put("/querycheckitem","perms[inspector:querycheckitem]");
        filterMaps.put("/toinsertcustomer","perms[client:toinsertcustomer]");
        filterMaps.put("/displaycustomer","perms[client:displaycustomer]");

        filterMaps.put("/toinserttransport","perms[admin:toinserttransport]");
        filterMaps.put("/todisplaytransport","perms[client:todisplaytransport]");
        filterMaps.put("/displayexportlists","perms[client:displayexportlists]");

        filterMaps.put("/exportitem","perms[client:exportitem]");
        filterMaps.put("/revisecustomer","perms[client:revisecustomer]");
        filterMaps.put("/deletecustomer","perms[client:deletecustomer]");

        filterMaps.put("/revisetransport","perms[admin:revisetransport]");
        filterMaps.put("/deletetransport","perms[admin:deletetransport]");

        filterMaps.put("/toinsertuser","perms[admin:toinsertuser]");
        filterMaps.put("/insertuser","perms[admin:insertuser]");
        filterMaps.put("/selectalluser","perms[admin:selectalluser]");

        filterMaps.put("/displaywarehousing","perms[client:displaywarehousing]");
        filterMaps.put("/displaydelivery","perms[client:displaydelivery]");
        filterMaps.put("/reviseroom","perms[admin:reviseroom]");
        filterMaps.put("/reviseinroom","perms[admin:reviseinroom]");

        filterMaps.put("/waitcheckitem","perms[client:waitcheckitem]");
        filterMaps.put("/revoke","perms[client:revoke]");

        filterMaps.put("/displaylabel","perms[client:displaylabel]");
        filterMaps.put("/deletelabel","perms[admin:deletelabel]");
        //拦截除登录页之外的页面
        filterMaps.put("/*","authc");
        //放入拦截Map
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMaps);
        //设置拦截后的跳转界面
        shiroFilterFactoryBean.setLoginUrl("/tologin");
        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        return shiroFilterFactoryBean;
    }

    //创建 DefaultWebSecurityManager
    @Bean("defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("customRealm") CustomRealm customRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //设置Realm使用hash凭证匹配器

        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置哈希算法名字
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置哈希次数
        hashedCredentialsMatcher.setHashIterations(1024);

        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        //需要关联Realm
        securityManager.setRealm(customRealm);
        return securityManager;
    }

    //创建 Realm
    @Bean("customRealm")
    public CustomRealm getRealm(){
        return new CustomRealm();
    }
}
