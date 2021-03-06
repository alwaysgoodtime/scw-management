package com.atguigu.atcrowdfunding.service;

import com.atguigu.atcrowdfunding.bean.TMenu;

import java.util.List;

/**
 * @author goodtime
 * @create 2020-02-03 5:00 下午
 */
public interface TMenuService {

    List<TMenu> listAllMenu();

    List<TMenu> listAllTree();

    void addMenu(TMenu menu);

    void deleteMenuById(Integer id);

    void updateMenu(TMenu tMenu);
}
