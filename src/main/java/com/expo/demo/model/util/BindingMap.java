package com.expo.demo.model.util;



/**
 * Created by Garik on 23-May-19, 16:09
 */

public class BindingMap {
    private Long id;
    private Integer pageAdmin;
    private Integer pageModerator;
    private Integer pageMember;
    private Integer tab;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTab() {
        return tab;
    }

    public void setTab(Integer tab) {
        this.tab = tab;
    }

    public Integer getPageAdmin() {
        return pageAdmin;
    }

    public void setPageAdmin(Integer pageAdmin) {
        this.pageAdmin = pageAdmin;
    }

    public Integer getPageModerator() {
        return pageModerator;
    }

    public void setPageModerator(Integer pageModerator) {
        this.pageModerator = pageModerator;
    }

    public Integer getPageMember() {
        return pageMember;
    }

    public void setPageMember(Integer pageMember) {
        this.pageMember = pageMember;
    }



}
