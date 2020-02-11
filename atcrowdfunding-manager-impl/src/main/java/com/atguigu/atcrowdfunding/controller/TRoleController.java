package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.atguigu.atcrowdfunding.util.Const;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-11 11:04 下午
 */
@Controller
public class TRoleController {

    @Autowired
    TRoleService tRoleService;

    Logger log = LoggerFactory.getLogger(TRoleController.class);



    //去角色维护主页
    @RequestMapping("/role/index")
    public String toRole(){
        //因为用异步ajax，并不会直接输出值
        log.debug("来啦，你看到了吗");
        return "/role/index";
    }

    //局部加载页面的值（ajax），直接返回json数据
    @ResponseBody
    @RequestMapping("/role/loadData")
    public PageInfo loadData(@RequestParam(value = Const.CURRENT_PAGE, required = false, defaultValue = "1") Integer currentPage,
        @RequestParam(value = Const.PAGE_SIZE, required = false, defaultValue = "10") Integer pageSize,
        @RequestParam(value = Const.CONDITION, required = false,defaultValue = "") String condition){

        log.debug("看我看我,当前页={}",currentPage);

        PageHelper.startPage(currentPage,pageSize);

        Map<String,Object> hashMap = new HashMap();

        hashMap.put(Const.CONDITION,condition);

        PageInfo<TRole> role = tRoleService.listRole(hashMap);

        return role;
    }

}
