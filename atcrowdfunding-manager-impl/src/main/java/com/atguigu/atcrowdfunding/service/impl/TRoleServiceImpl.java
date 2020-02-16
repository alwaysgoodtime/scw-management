package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.*;
import com.atguigu.atcrowdfunding.mapper.TAdminRoleMapper;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import com.atguigu.atcrowdfunding.mapper.TRolePermissionMapper;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.atguigu.atcrowdfunding.util.Const;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-11 10:58 下午
 */
@Service
public class TRoleServiceImpl implements TRoleService {

    @Autowired
    TRoleMapper tRoleMapper;

    @Autowired
    TAdminRoleMapper tAdminRoleMapper;

    @Autowired
    TRolePermissionMapper tRolePermissionMapper;

    Logger log = LoggerFactory.getLogger(TRoleServiceImpl.class);


    public PageInfo<TRole> listRole(Map<String, Object> hashMap) {
        TRoleExample tRoleExample = new TRoleExample();
        //改造一下方法，防止其他页面（比如admin）调用此接口时出现空指针异常等错误
        Object conditionfalse = hashMap.get(Const.CONDITION);
        String condition = null;
        if(conditionfalse != null){
           condition = (String)conditionfalse;
        }
        //如果有条件，就根据条件查询
        if(condition!= null && condition.length() != 0){
            tRoleExample.createCriteria().andNameLike("%"+condition+"%");
        }

        log.warn("condition={}",condition);
        List<TRole> tRoles = tRoleMapper.selectByExample(tRoleExample);
        log.warn("tRoles={}",tRoles);
        PageInfo<TRole> pageInfo = new PageInfo<TRole>(tRoles,5);
        return pageInfo;
    }

    //添加角色
    public void addRole(TRole tRole) {
        tRoleMapper.insert(tRole);
    }

    //更新角色
    public void updateRole(TRole tRole) {
        tRoleMapper.updateByPrimaryKey(tRole);
    }

    //删除角色
    public void deleteRole(TRole tRole) {
        Integer id = tRole.getId();
        tRoleMapper.deleteByPrimaryKey(id);
    }


    //查询所有角色
    public List<TRole> listAll() {
        return tRoleMapper.selectByExample(null);
    }

    //通过AdminId和t_admin_role表查到RoleId
    public List<Integer> getRoleIdByAdminId(Integer id) {
        return tAdminRoleMapper.getRoleIdByAdminId(id);
    }

    public void saveAdminAndRoleRelationship(Integer adminId, Integer[] roleId) {
        for (Integer a:roleId
             ) {
            TAdminRole tAdminRole = new TAdminRole();
            tAdminRole.setAdminid(adminId);
            tAdminRole.setRoleid(a);
            tAdminRoleMapper.insert(tAdminRole);
        }
    }

    public void deleteAdminAndRoleRelationship(Integer adminId, Integer[] roleId) {
      tAdminRoleMapper.deleteAdminAndRoleRelation(adminId,roleId);
    }


    //给角色分配权限
    public void modifyRoleAndPermissionRelationship(List<Integer> permissionIds, Integer roleId) {
        //第一步：删除角色所有权限
        TRolePermissionExample tRolePermissionExample = new TRolePermissionExample();
        tRolePermissionExample.createCriteria().andRoleidEqualTo(roleId);
        tRolePermissionMapper.deleteByExample(tRolePermissionExample);
        //第二步：放入角色所有的权限
        tRolePermissionMapper.insertByPermissionAndRoleId(permissionIds,roleId);
    }

    public List<Integer> getPermissionIdByRoleId(Integer roleId) {
        log.debug("我是roleid={}",roleId);

        TRolePermissionExample tRolePermissionExample  = new TRolePermissionExample();
        tRolePermissionExample.createCriteria().andRoleidEqualTo(roleId);

        List<TRolePermission> tRolePermissions = tRolePermissionMapper.selectByExample(tRolePermissionExample);

        List<Integer> permissionId = new ArrayList<Integer>();

        if(tRolePermissions != null) {
            for (TRolePermission t : tRolePermissions
            ) {
                permissionId.add(t.getPermissionid());
            }
        }
        return permissionId;
    }
}
