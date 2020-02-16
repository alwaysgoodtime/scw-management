package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.bean.TPermission;

import java.util.List;

/**
 * @author goodtime
 * @create 2020-02-16 8:58 下午
 */
public interface TPermissionService {

    List<TPermission> listAllTree();

    void addPermission(TPermission tPermission);

    //返回所有权限
    List<TPermission> listAllPermission();

    void deletePermissionById(Integer id);

    void updatePermission(TPermission tPermission);
}
