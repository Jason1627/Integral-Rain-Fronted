package com.fjx.prize.commons.db.entity;

import java.io.Serializable;
import java.util.Date;

public class SysMenu implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_name
     *
     * @mbg.generated
     */
    private String menuName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_url
     *
     * @mbg.generated
     */
    private String menuUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.parent_id
     *
     * @mbg.generated
     */
    private Integer parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.icon
     *
     * @mbg.generated
     */
    private String icon;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.order_num
     *
     * @mbg.generated
     */
    private Integer orderNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.id
     *
     * @return the value of sys_menu.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.id
     *
     * @param id the value for sys_menu.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_name
     *
     * @return the value of sys_menu.menu_name
     *
     * @mbg.generated
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_name
     *
     * @param menuName the value for sys_menu.menu_name
     *
     * @mbg.generated
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_url
     *
     * @return the value of sys_menu.menu_url
     *
     * @mbg.generated
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_url
     *
     * @param menuUrl the value for sys_menu.menu_url
     *
     * @mbg.generated
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.parent_id
     *
     * @return the value of sys_menu.parent_id
     *
     * @mbg.generated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.parent_id
     *
     * @param parentId the value for sys_menu.parent_id
     *
     * @mbg.generated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.icon
     *
     * @return the value of sys_menu.icon
     *
     * @mbg.generated
     */
    public String getIcon() {
        return icon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.icon
     *
     * @param icon the value for sys_menu.icon
     *
     * @mbg.generated
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.order_num
     *
     * @return the value of sys_menu.order_num
     *
     * @mbg.generated
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.order_num
     *
     * @param orderNum the value for sys_menu.order_num
     *
     * @mbg.generated
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.create_time
     *
     * @return the value of sys_menu.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.create_time
     *
     * @param createTime the value for sys_menu.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}