package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-11 10:58 下午
 */
public interface TRoleService {

    PageInfo<TRole> listRole(Map<String, Object> hashMap);

    void addRole(TRole tRole);

    void updateRole(TRole tRole);

    void deleteRole(TRole tRole);

    List<TRole> listAll();

    List<Integer> getRoleIdByAdminId(Integer id);

    void saveAdminAndRoleRelationship(Integer adminId, Integer[] roleId);

    void deleteAdminAndRoleRelationship(Integer adminId, Integer[] roleId);

    void modifyRoleAndPermissionRelationship(List<Integer> permissionIds, Integer roleId);

    List<Integer> getPermissionIdByRoleId(Integer id);
}
