package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-11 10:58 下午
 */
public interface TRoleService {

    PageInfo<TRole> listRole(Map<String, Object> hashMap);
}
