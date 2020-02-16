package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.TAdminService;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.atguigu.atcrowdfunding.util.Const;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-03 7:33 下午
 */
@Controller
public class TAdminController {

    @Autowired
    private TAdminService tadminService;

    @Autowired
    private TRoleService tRoleService;

   Logger log = LoggerFactory.getLogger(TAdminController.class);


    //    返回用户维护主页面，分页查出所有数据；同时承担模糊查询的功能
//    后台页面，不同于登录注册的页面，所有写到manager-impl包里
//    设置请求默认值，保证分页效果
    @RequestMapping("/admin/index")
    public String userIndex(@RequestParam(value = Const.CURRENT_PAGE, required = false, defaultValue = "1") Integer currentPage,
                            @RequestParam(value = Const.PAGE_SIZE, required = false, defaultValue = "2") Integer pageSize,
                            @RequestParam(value = Const.CONDITION, required = false,defaultValue = "") String condition,
                            Model model) {
        //表单提交的任何数据类型都是字符串类型,SpringMVC定义了转换器,将字符串转化为我们方法参数的各种类型.我们也可以实现自定义的转换器以实现自定义的参数类型转换

        log.debug("condition条件={}",condition);

        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put(Const.CURRENT_PAGE, currentPage);
        queryMap.put(Const.PAGE_SIZE, pageSize);
        queryMap.put(Const.CONDITION,condition);
//        线程绑定，保证整个线程只会有这两个值传入
        PageHelper.startPage(currentPage, pageSize);
//        pageinfo是pagehelper封装当前页数据的，注意不是pagehelper。
        PageInfo<TAdmin> pageInfo = tadminService.listTAdminPage(queryMap);
        model.addAttribute(Const.PAGE_INFO, pageInfo);
//      没有解决表单重复提交
        model.addAttribute(Const.CONDITION,condition);
        return "/admin/index";
    }

    @PreAuthorize("hasRole('PM - 项目经理')")
    @RequestMapping("/admin/delete")
    public String userDelete(String id){
        log.debug("id={}",id);
        tadminService.deleteById(id);
        return "forward:/admin/index";
    }

    @RequestMapping("/admin/assign")
    public String assign(Integer id, Model model){
        //查询所有角色
        List<TRole> tRoles = tRoleService.listAll();
        //查询当前ID的所有角色ID
        List<Integer> tAdminRoles = tRoleService.getRoleIdByAdminId(id);
        //已分配的角色放一个list
        List<TRole> assigned = new ArrayList<TRole>();
        List<TRole> unassigned = new ArrayList<TRole>();

        for (TRole a: tRoles
             ) {
            if(tAdminRoles.contains(a.getId())){
                assigned.add(a);
            }else {
                unassigned.add(a);
            }
        }
        //把得到的数据用model放到请求域中
        model.addAttribute("assigned",assigned);
        model.addAttribute("unassigned",unassigned);
        //未分配的角色放一个list
        log.debug("已分配={}",assigned);
        log.debug("未分配={}",unassigned);
        return "/admin/assignRole";
    }

    @ResponseBody
    @RequestMapping("admin/doAssign")
    public String doassign(Integer adminId,Integer[] roleId){
        tRoleService.saveAdminAndRoleRelationship(adminId,roleId);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("admin/undoAssign")
    public String undoassign(Integer adminId,Integer[] roleId){
        tRoleService.deleteAdminAndRoleRelationship(adminId,roleId);
        return "ok";
    }
}
