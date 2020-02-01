package com.atguigu.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author goodtime
 * @create 2020-02-02 12:43 上午
 */
public class Slf4jTest {

    public static void main(String[] args) {
//  变量名最好叫log，不要叫logger
        Logger log = LoggerFactory.getLogger(Slf4jTest.class);
        log.info("普通消息");
        log.error("错误消息");
        log.warn("警告消息");
        log.debug("debug消息id={},name={}",1,"zhangsan");

    }
}
