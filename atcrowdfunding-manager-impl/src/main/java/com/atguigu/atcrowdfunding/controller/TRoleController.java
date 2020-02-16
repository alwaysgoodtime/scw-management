package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.Datas;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
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

    //自动封装到实体类中
    @ResponseBody
    @PreAuthorize("hasRole('PM - 项目经理')")//方法细粒度的控制，保证此方法只有项目经理能使用
    @RequestMapping("/role/add")
    public String add(TRole tRole){
        tRoleService.addRole(tRole);
        return "ok";//注意：这里是异步请求，不能转发到别的页面，那边还在等返回值，所以返回些状态码或者ok即可
    }


    @ResponseBody
    @RequestMapping("/role/update")
    public String update(TRole tRole){
        tRoleService.updateRole(tRole);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/role/delete")
    public String delete(TRole tRole){
        tRoleService.deleteRole(tRole);
        return "ok";
    }

    //回显角色所拥有的权限
    @ResponseBody
    @RequestMapping("role/permission")
    public List<Integer> rolePermission(Integer roleId){
        List<Integer> a = tRoleService.getPermissionIdByRoleId(roleId);
        return a;
    }


    //给角色分配权限
    @ResponseBody
    @RequestMapping("/role/modifyRoleAndPermissionRelationship")
    public String modifyRoleAndPermissionRelationship(Datas datas,Integer roleId){
        List<Integer> permissionIds = datas.getIds();
        tRoleService.modifyRoleAndPermissionRelationship(permissionIds,roleId);
        return "ok";
    }




}
