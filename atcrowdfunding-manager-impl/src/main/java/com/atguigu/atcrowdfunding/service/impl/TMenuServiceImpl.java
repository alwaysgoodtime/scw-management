package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.bean.TMenuExample;
import com.atguigu.atcrowdfunding.mapper.TMenuMapper;
import com.atguigu.atcrowdfunding.service.TMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author goodtime
 * @create 2020-02-03 5:02 下午
 */
@Service
public class TMenuServiceImpl implements TMenuService {

    Logger log = LoggerFactory.getLogger(TMenuServiceImpl.class);

    @Autowired
    private TMenuMapper menuMapper;

    public List<TMenu> listAllMenu() {

        List<TMenu> tMenuList = new ArrayList<TMenu>();
        Map<Integer,TMenu> map = new HashMap<Integer,TMenu>();


        List<TMenu> tMenus = menuMapper.selectByExample(null);//查询所有元素的方法

        log.debug("返回的集合有{}个", tMenus.size());

        for (TMenu t : tMenus
        ) {
            if (t.getPid() == 0) {
                tMenuList.add(t);
                map.put(t.getId(),t);//注意是getId
            }
        }
        for (TMenu t : tMenus
        ) {
            if (t.getPid() != 0){
                TMenu tMenu = map.get(t.getPid());
                log.debug("看看能不能取到");
                List<TMenu> childMenu = tMenu.getChildMenu();
                //逆向工程中又新加了个childMenu属性，存放父menu的子menu.
                childMenu.add(t);
            }
        }

        return tMenuList;

    }

    public List<TMenu> listAllTree() {
        return menuMapper.selectByExample(null);//查找全部数据，直接返回，用作ztree的显示
    }

    public void addMenu(TMenu tMenu){
        Logger log = LoggerFactory.getLogger(TMenuServiceImpl.class);
        log.debug("传入的参数为{}",tMenu);
        menuMapper.insert(tMenu);
    }

    public void deleteMenuById(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }

    public void updateMenu(TMenu tMenu) {
        menuMapper.updateByPrimaryKey(tMenu);
    }

}
