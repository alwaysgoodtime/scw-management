package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.service.TAdminService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.MD5Util;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-02 6:24 下午
 */

@Service
public class TAdminServiceImpl implements TAdminService{

    @Autowired
    //Spring已经整合了Mybatis，所以可以自动注入，不过方法还未实现，要么用注解，要么用xml文件配置，这里用的是
    //Mybatis逆向工程生成的模板类，来实现Mapper的方法。
    private TAdminMapper adminMapper;


    public TAdmin getTAdminByLogin(Map<String, Object> adminMap) throws Exception {

        Logger log = LoggerFactory.getLogger(TAdminServiceImpl.class);


        String loginacct = (String)adminMap.get(Const.LOGINACCT);
        String userpswd = (String)adminMap.get(Const.USERPSWD);


//        让数据库密文存密码
        String secretUserpswd = MD5Util.digest(userpswd);


        //判断是否有用户名存在，
        TAdminExample adminExample = new TAdminExample();

        adminExample.createCriteria().andLoginacctEqualTo(loginacct);

        List<TAdmin> tAdmins = adminMapper.selectByExample(adminExample);

//        没有用户存在，抛给controller新的异常
        if(tAdmins == null || tAdmins.size() == 0){
            log.debug("没有用户存在");
            throw new LoginException(Const.LOGIN_LOGINACCT_ERROR);
        }

        TAdmin tAdmin = tAdmins.get(0);

//        密码不对
        if(!tAdmin.getUserpswd().equals(secretUserpswd)){
            log.debug("密码不对");
            throw new LoginException(Const.LOGIN_USERPSWD_ERROR);
        };

        log.debug("有用户，正常返回");
//        正常返回
        return  tAdmin;

    }

    public PageInfo<TAdmin> listTAdminPage(Map<String, Object> queryMap) {

        Logger log = LoggerFactory.getLogger(TAdminServiceImpl.class);

        //通过查询所有用户，然后用PageHelper分页

        TAdminExample tAdminExample = new TAdminExample();

        List<TAdmin> tAdmins = adminMapper.selectByExample(tAdminExample);

        PageInfo<TAdmin> pageInfo = new PageInfo<TAdmin>(tAdmins,5);//用分页类封装返回的结果
        // 5是导航页，也就是最多有5个页面，和pagesize不一样，pagesize是每页固定显示5个数据
        // 对于mysql就是调用limit而已


        log.debug("pageInfo={}",pageInfo);

        return pageInfo;
    }
}
