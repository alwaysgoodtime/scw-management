package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.service.TPermissionService;
import com.atguigu.atcrowdfunding.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author goodtime
 * @create 2020-02-16 9:00 下午
 */

@Controller
public class TPermissionController {

    Logger log = LoggerFactory.getLogger(TPermissionController.class);
    
    @Autowired
    TPermissionService tPermissionService;

    //来到权限管理的主页面
    @RequestMapping("/permission/index")
    public String index(){
        log.debug("哥们来啦");
        return "/permission/index";
    }


    //返回ztree的数据
    @ResponseBody
    @RequestMapping("/permission/loadData")
    public List<TPermission> loadData(){
        return tPermissionService.listAllTree();
    }

    //添加按钮
    @ResponseBody
    @RequestMapping("/permission/add")
    public String add(TPermission tPermission, HttpSession httpSession){
        tPermissionService.addPermission(tPermission);
        List<TPermission> tPermissions = tPermissionService.listAllPermission();
        httpSession.setAttribute(Const.MENU_LIST,tPermissions);

        return "ok";
    }

    //删除按钮
    @ResponseBody
    @RequestMapping("/permission/delete")
    public String delete(Integer id, HttpSession httpSession){
        log.debug("删除的id={}",id);
        tPermissionService.deletePermissionById(id);
        List<TPermission> tPermissions = tPermissionService.listAllPermission();
        httpSession.setAttribute("permissionList",tPermissions);
        return "ok";
    }

    //修改按钮
    @ResponseBody
    @RequestMapping("/permission/update")
    public String update(TPermission tPermission,HttpSession httpSession){
        tPermissionService.updatePermission(tPermission);
        List<TPermission> tPermissions = tPermissionService.listAllPermission();
        httpSession.setAttribute("permissionList",tPermissions);
        return "ok";
    }


}
