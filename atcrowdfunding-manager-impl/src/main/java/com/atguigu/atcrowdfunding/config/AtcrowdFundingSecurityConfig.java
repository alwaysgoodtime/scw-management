package com.atguigu.atcrowdfunding.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author goodtime
 * @create 2020-02-13 1:53 下午
 */
//安全框架配置类

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class AtcrowdFundingSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//权限认证管理
        //super.configure(auth);

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());//注入用户服务对象的密码编码器

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {//管理登录、注销
        //super.configure(http);取消默认的
        http.authorizeRequests().antMatchers("/static/**","/welcome.jsp").permitAll().anyRequest().authenticated();//剩下都需要认证
        // /login.jsp==POST  用户登陆请求发给Security
        http.formLogin().loginPage("/login")//登录页的映射是login
                .usernameParameter("loginacct")//用户名
                .passwordParameter("userpswd")//密码
                .defaultSuccessUrl("/main").permitAll();
        http.csrf().disable();
        http.logout().logoutSuccessUrl("/index");//注销页面
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                String header = httpServletRequest.getHeader("X-Requested-With");//异步请求会带这个对象，同步不会
                if("XMLHttpRequest".equals(header)){
                    httpServletResponse.getWriter().print("403");//返回403，这种可以被前台ajax发送请求的result捕获
                }else{
                    String contextPath = httpServletRequest.getContextPath();
                    httpServletRequest.getRequestDispatcher("/WEB-INF/jsp/error/error.jsp").forward(httpServletRequest,httpServletResponse);
                    //这里转发路径一定写对，写不对，spring会帮你找虚拟地址+admin路径对应的资源
                    //如果发送的是同步请求，权限不够就会跳转错误页面
                }
            }
        });//权限不够时的异常处理
        http.rememberMe();
    }
}
