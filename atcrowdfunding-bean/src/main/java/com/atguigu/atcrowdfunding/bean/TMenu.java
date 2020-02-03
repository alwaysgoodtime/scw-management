package com.atguigu.atcrowdfunding.bean;

import java.util.ArrayList;
import java.util.List;

public class TMenu {
    private Integer id;

    private Integer pid;

    private String name;

    private String icon;

    private String url;

    private List<TMenu> childMenu = new ArrayList<TMenu>();//注意：一定要new，否则默认值是个空指针，
    //后续不好调用，从数据库查也无法查到

    public List<TMenu> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<TMenu> childMenu) {
        this.childMenu = childMenu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}