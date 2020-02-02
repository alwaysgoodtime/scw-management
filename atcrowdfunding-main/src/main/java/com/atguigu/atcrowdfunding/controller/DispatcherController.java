package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.service.TAdminService;
import com.atguigu.atcrowdfunding.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-02 1:12 下午
 */
//处理主页显示、基本登录和注销，在具体业务逻辑之前，可以放到main模块当中

@Controller
public class DispatcherController {

    @Autowired
    private TAdminService adminService;

    Logger log = LoggerFactory.getLogger(DispatcherController.class);

    //    welcome.jsp转发的主页请求
    @RequestMapping("/index")
    public String index() {
        log.info("返回主页面");
        return "index";
    }

    //   去登录页面
    @RequestMapping("/login")
    public String login() {
        log.info("去登录页面");
        return "login";
    }

    //   去注册页面
    @RequestMapping("/reg")
    public String reg() {
        log.info("去注册页面");
        return "reg";
    }

    //    从登录页面发起登录post请求
    @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, Model map, HttpSession httpSession) {
        Map<String, Object> adminMap = new HashMap<String, Object>();
        log.debug("loginacct={},uesrpswd={}", loginacct, userpswd);
        adminMap.put(Const.LOGINACCT, loginacct);
        adminMap.put(Const.USERPSWD, userpswd);
        //    查询失败的话，显示错误信息
        try {
            TAdmin tAdmin = adminService.getTAdminByLogin(adminMap);
            //      查询成功，存session，跳转后台主页面main.jsp
            httpSession.setAttribute(Const.LOGIN_ADMIN, loginacct);
        //  如果return放外面，捕获完异常还是会return
            return "main";
        } catch (Exception e) {
            map.addAttribute(Const.MESSAGE, e.getMessage());
            //回显用户名
            map.addAttribute(Const.LOGINACCT,loginacct);
            log.debug("找不到用户啦");
            return "login";
        //    重新转发到login页面登录
        }
    }

//   注销功能
    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        if(httpSession != null){
//            session默认保存时间是30分钟，如果用户登录后30分钟未执行任何操作，可能session就清空了，
//            所以先判断是否为空，否则会空指针异常
            httpSession.removeAttribute(Const.LOGIN_ADMIN);
            httpSession.invalidate();
        }
//       转发是不会改变地址的，所以如果客户刷新页面，相当于一直在注销，不如直接重定向到主页,后面要写相对于上下文路径的地址
//        这里相当于重新发了一次请求
        return "redirect:/index";
    }
}
