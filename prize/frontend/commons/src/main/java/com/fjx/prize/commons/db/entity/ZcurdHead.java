package com.fjx.prize.commons.db.entity;

import java.io.Serializable;
import java.util.Date;

public class ZcurdHead implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.table_name
     *
     * @mbg.generated
     */
    private String tableName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.form_name
     *
     * @mbg.generated
     */
    private String formName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.id_field
     *
     * @mbg.generated
     */
    private String idField;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.is_auto
     *
     * @mbg.generated
     */
    private Integer isAuto;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.form_type
     *
     * @mbg.generated
     */
    private Integer formType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.dialog_size
     *
     * @mbg.generated
     */
    private String dialogSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.db_source
     *
     * @mbg.generated
     */
    private String dbSource;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.handle_class
     *
     * @mbg.generated
     */
    private String handleClass;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.delete_flag_field
     *
     * @mbg.generated
     */
    private String deleteFlagField;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zcurd_head.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table zcurd_head
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.id
     *
     * @return the value of zcurd_head.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.id
     *
     * @param id the value for zcurd_head.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.table_name
     *
     * @return the value of zcurd_head.table_name
     *
     * @mbg.generated
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.table_name
     *
     * @param tableName the value for zcurd_head.table_name
     *
     * @mbg.generated
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.form_name
     *
     * @return the value of zcurd_head.form_name
     *
     * @mbg.generated
     */
    public String getFormName() {
        return formName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.form_name
     *
     * @param formName the value for zcurd_head.form_name
     *
     * @mbg.generated
     */
    public void setFormName(String formName) {
        this.formName = formName == null ? null : formName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.id_field
     *
     * @return the value of zcurd_head.id_field
     *
     * @mbg.generated
     */
    public String getIdField() {
        return idField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.id_field
     *
     * @param idField the value for zcurd_head.id_field
     *
     * @mbg.generated
     */
    public void setIdField(String idField) {
        this.idField = idField == null ? null : idField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.is_auto
     *
     * @return the value of zcurd_head.is_auto
     *
     * @mbg.generated
     */
    public Integer getIsAuto() {
        return isAuto;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.is_auto
     *
     * @param isAuto the value for zcurd_head.is_auto
     *
     * @mbg.generated
     */
    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.form_type
     *
     * @return the value of zcurd_head.form_type
     *
     * @mbg.generated
     */
    public Integer getFormType() {
        return formType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.form_type
     *
     * @param formType the value for zcurd_head.form_type
     *
     * @mbg.generated
     */
    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.dialog_size
     *
     * @return the value of zcurd_head.dialog_size
     *
     * @mbg.generated
     */
    public String getDialogSize() {
        return dialogSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.dialog_size
     *
     * @param dialogSize the value for zcurd_head.dialog_size
     *
     * @mbg.generated
     */
    public void setDialogSize(String dialogSize) {
        this.dialogSize = dialogSize == null ? null : dialogSize.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.db_source
     *
     * @return the value of zcurd_head.db_source
     *
     * @mbg.generated
     */
    public String getDbSource() {
        return dbSource;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.db_source
     *
     * @param dbSource the value for zcurd_head.db_source
     *
     * @mbg.generated
     */
    public void setDbSource(String dbSource) {
        this.dbSource = dbSource == null ? null : dbSource.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.handle_class
     *
     * @return the value of zcurd_head.handle_class
     *
     * @mbg.generated
     */
    public String getHandleClass() {
        return handleClass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.handle_class
     *
     * @param handleClass the value for zcurd_head.handle_class
     *
     * @mbg.generated
     */
    public void setHandleClass(String handleClass) {
        this.handleClass = handleClass == null ? null : handleClass.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.delete_flag_field
     *
     * @return the value of zcurd_head.delete_flag_field
     *
     * @mbg.generated
     */
    public String getDeleteFlagField() {
        return deleteFlagField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.delete_flag_field
     *
     * @param deleteFlagField the value for zcurd_head.delete_flag_field
     *
     * @mbg.generated
     */
    public void setDeleteFlagField(String deleteFlagField) {
        this.deleteFlagField = deleteFlagField == null ? null : deleteFlagField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zcurd_head.create_time
     *
     * @return the value of zcurd_head.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zcurd_head.create_time
     *
     * @param createTime the value for zcurd_head.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}