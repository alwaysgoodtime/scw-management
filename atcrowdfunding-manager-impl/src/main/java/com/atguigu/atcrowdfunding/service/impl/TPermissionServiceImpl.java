package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.service.TPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author goodtime
 * @create 2020-02-16 8:57 下午
 */



@Service
public class TPermissionServiceImpl implements TPermissionService {


    @Autowired
    TPermissionMapper tPermissionMapper;

    public List<TPermission> listAllTree() {
        List<TPermission> tPermissions = tPermissionMapper.selectByExample(null);//查询所有权限

        Map<Integer,TPermission> parent = new HashMap<Integer,TPermission>();//存放查询的父权限

        List<TPermission> rt = new ArrayList();

        //如果权限没有父亲，pid=0，把它放入一个集合中
        for (TPermission t:tPermissions
             ) {
            if(t.getPid() == 0){
                parent.put(t.getId(),t);
            }
        }
        for (TPermission t: tPermissions
             ) {
            if(t.getPid() != 0){
                TPermission tPermission = parent.get(t.getPid());
                tPermission.getChildren().add(t);
            }
        }
        Set<Integer> integers = parent.keySet();
        for (Integer a:integers
             ) {
            rt.add(parent.get(a));
        }

        return rt;
    }

    public void addPermission(TPermission tPermission) {
        tPermissionMapper.insert(tPermission);
    }

    public List<TPermission> listAllPermission() {
        return tPermissionMapper.selectByExample(null);
    }

    public void deletePermissionById(Integer id) {
        tPermissionMapper.deleteByPrimaryKey(id);
    }

    public void updatePermission(TPermission tPermission) {
        tPermissionMapper.updateByPrimaryKey(tPermission);
    }
}
