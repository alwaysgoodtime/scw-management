package com.atguigu.atcrowdfunding.exception;

/**
 * @author goodtime
 * @create 2020-02-02 7:02 下午
 */
//最好继承运行时异常，因为spring事务控制，默认是运行时异常才回滚，其他异常不会回滚，不过本项目里我们已在spring-tx中配置
public class LoginException extends RuntimeException{

    public LoginException(){};

    public LoginException(String message){
        super(message);
    }

}
