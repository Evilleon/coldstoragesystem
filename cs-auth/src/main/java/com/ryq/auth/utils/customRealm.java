package com.ryq.auth.utils;

import com.ryq.coldstoragesystem.bean.Permission;
import com.ryq.coldstoragesystem.bean.Role;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.service.LoginService;
import com.ryq.coldstoragesystem.service.PermissionService;
import com.ryq.coldstoragesystem.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义Realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    LoginService loginService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUserName();
//        logger.info("username:" + userName);
        //返回AuthorizationInfo授权类的子类
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //2.根据用户名查询用户所有的角色信息
        List<Role> allRoleList = roleService.getAllRoleListByUsername(userName);
        Set<String> rolesSet = new HashSet<>();
        for (Role r : allRoleList) {
            String roleName = r.getName();
            rolesSet.add(roleName);
        }
//        logger.info("用户：{} 拥有的角色有：{}", userName, rolesSet);
        //设置用户角色信息
        simpleAuthorizationInfo.setRoles(rolesSet);

        //3.根据用户名查询用户所有的权限信息
        List<Permission> allPermissionList = permissionService.getAllPermissionListByUsername(userName);
        Set<String> permissionSet = new HashSet<>();
        for (Permission permission : allPermissionList) {
            String permissionName = permission.getName();
            permissionSet.add(permissionName);
        }
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
//        logger.info("用户：{} 拥有的权限有：{}", userName, permissionSet);
        return simpleAuthorizationInfo;

    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //Shiro认证判断逻辑
        //数据库中用户名和密码

        //1、判断用户名
        MyAuthenticationToken token = (MyAuthenticationToken)authenticationToken;

        User user = loginService.selectByUsername(token.getUsername());

        if (user == null){

            return null;

        }else if ("2".equals(user.getStatus())){
            //用户冻结
            throw new LockedAccountException();//账号冻结
        } else {

            if (!token.getUsername().equals(user.getUserName())) {
                //用户名不存在
                return null;
            }

            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());

        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof MyAuthenticationToken;
    }
}
