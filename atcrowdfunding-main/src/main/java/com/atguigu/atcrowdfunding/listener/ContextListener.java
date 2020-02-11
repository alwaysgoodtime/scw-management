package com.atguigu.atcrowdfunding.listener;

import com.atguigu.atcrowdfunding.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author goodtime
 * @create 2020-02-02 2:13 下午
 */
public class ContextListener implements ServletContextListener {

    Logger log = LoggerFactory.getLogger(ContextListener.class);

    //加载了PATH这个常量在servletContext中，方便jsp中调用
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("开始监听");
        ServletContext servletContext = sce.getServletContext();
        String contextPath = servletContext.getContextPath();
        servletContext.setAttribute(Const.PATH,contextPath);
        log.info("contextPath={}",contextPath);
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
