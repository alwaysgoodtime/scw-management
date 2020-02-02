package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TAdmin;

import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-02 6:24 下午
 */

public interface TAdminService {

    TAdmin getTAdminByLogin(Map<String, Object> adminMap) throws Exception;
}
