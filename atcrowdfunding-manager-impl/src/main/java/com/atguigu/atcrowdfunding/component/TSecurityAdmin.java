package com.atguigu.atcrowdfunding.component;
import com.atguigu.atcrowdfunding.bean.TAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 包装User类，在自带类的功能上，装入用户的更多信息
 * @author goodtime
 * @create 2020-02-13 5:48 下午
 */
public class TSecurityAdmin extends User {

    TAdmin admin;

    public TSecurityAdmin(TAdmin admin, Collection<? extends GrantedAuthority> authorities) {
        //继承user类的构造器，其中的true是设置账号密码过不过期的东西
        super(admin.getUsername(),admin.getUserpswd(),true,true,true,true,authorities);
        this.admin = admin;
    }

}
