package com.atguigu.atcrowdfunding.component;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author goodtime
 * @create 2020-02-13 2:22 下午
 */



@Component
public class SecurityUserDetailServiceImpl implements UserDetailsService{

    @Autowired
    TAdminMapper tadminMapper;//注入用户表的mapper

    @Autowired
    TRoleMapper tRoleMapper;//角色表

    @Autowired
    TPermissionMapper tPermissionMapper;//权限表

    Logger log = LoggerFactory.getLogger(SecurityUserDetailServiceImpl.class);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户对象
        TAdminExample tAdminExample = new TAdminExample();

        log.debug("来啦{}",username);

        tAdminExample.createCriteria().andLoginacctEqualTo(username);//传过来的username其实是loginacct

        List<TAdmin> tAdmins = tadminMapper.selectByExample(tAdminExample);


        if(tAdmins != null && tAdmins.size() == 1){
            TAdmin tAdmin = tAdmins.get(0);

            //2.查询用户拥有的角色集合

            log.debug("用户信息：{}",tAdmin);

            Integer id = tAdmin.getId();

            //根据id查它拥有的权限，联合中间表查询

            List<TRole> roleMapper = tRoleMapper.listRoleByAdminId(id);

            //3.查询权限

            List<TPermission> tPermission = tPermissionMapper.listPermissionByAdminId(id);


            //4.构建用户所有权限集合，也就是（ROLE_角色+权限）

            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

            for (TRole a:roleMapper
            ) {
                authorities.add(new SimpleGrantedAuthority("ROLE_"+a.getName()));//角色权限需要拼串
            }

            for (TPermission a:tPermission
            ) {
                authorities.add(new SimpleGrantedAuthority(a.getName()));
            }

            //这样登录的话，User类封装的东西太少，我们可以让一个类继承它，来封装登录用户的更多信息
            //return new User(tAdmin.getLoginacct(),tAdmin.getUserpswd(),authorities);
            return new TSecurityAdmin(tAdmin,authorities);
        }else{
            return null;
        }

    }
}

