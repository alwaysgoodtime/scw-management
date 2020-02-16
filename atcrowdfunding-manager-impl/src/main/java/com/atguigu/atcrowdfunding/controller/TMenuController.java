package com.atguigu.atcrowdfunding.controller;
import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.service.TMenuService;
import com.atguigu.atcrowdfunding.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author goodtime
 * @create 2020-02-12 2:39 下午
 */
@Controller
public class TMenuController {

    Logger log = LoggerFactory.getLogger(TMenuController.class);

    @Autowired
    TMenuService tMenuService;


    //来到菜单页面
    @RequestMapping("/menu/index")
    public String index(){
        return "/menu/index";
    }



    //返回ztree的数据
    @ResponseBody
    @RequestMapping("/menu/loadData")
    public List<TMenu> loadData(){
        return tMenuService.listAllTree();
    }

    //添加按钮
    @ResponseBody
    @RequestMapping("/menu/add")
    public String add(TMenu menu, HttpSession httpSession){
        tMenuService.addMenu(menu);
        List<TMenu> tMenuList = tMenuService.listAllMenu();
        httpSession.setAttribute(Const.MENU_LIST,tMenuList);

        return "ok";
    }

    //删除按钮
    @ResponseBody
    @RequestMapping("/menu/delete")
    public String delete(Integer id,HttpSession httpSession){
        log.debug("删除的id={}",id);
        tMenuService.deleteMenuById(id);
        List<TMenu> tMenuList = tMenuService.listAllMenu();
        httpSession.setAttribute(Const.MENU_LIST,tMenuList);
        return "ok";
    }

    //修改按钮
    @ResponseBody
    @RequestMapping("/menu/update")
    public String update(TMenu tMenu,HttpSession httpSession){
        tMenuService.updateMenu(tMenu);
        List<TMenu> tMenuList = tMenuService.listAllMenu();
        httpSession.setAttribute(Const.MENU_LIST,tMenuList);
        return "ok";
    }



}
