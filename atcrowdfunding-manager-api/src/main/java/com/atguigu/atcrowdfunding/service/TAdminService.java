package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-02 6:24 下午
 */

public interface TAdminService {

    TAdmin getTAdminByLogin(Map<String, Object> adminMap) throws Exception;

    PageInfo<TAdmin> listTAdminPage(Map<String, Object> queryMap);
//  如果无法解决包的导入，需要在pom中配置一下
}
