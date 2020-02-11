package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.bean.TRoleExample;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.atguigu.atcrowdfunding.util.Const;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    Logger log = LoggerFactory.getLogger(TRoleServiceImpl.class);


    public PageInfo<TRole> listRole(Map<String, Object> hashMap) {
        TRoleExample tRoleExample = new TRoleExample();
        String condition = (String)hashMap.get(Const.CONDITION);
        //如果有条件，就根据条件查询
        if(condition.length() != 0){
            tRoleExample.createCriteria().andNameLike("%"+condition+"%");
        }

        log.warn("condition={}",condition);
        List<TRole> tRoles = tRoleMapper.selectByExample(tRoleExample);
        log.warn("tRoles={}",tRoles);
        PageInfo<TRole> pageInfo = new PageInfo<TRole>(tRoles,5);
        return pageInfo;
    }
}
