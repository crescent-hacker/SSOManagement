package com.jetsun.bean.biz;

import com.jetsun.utility.StringUtil;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/10/9
 * Desc:权限bean
 */
public class OperationBean {
    /**
     * 权限id
     */
    private int opId;
    /**
     * 系统id
     */
    private long rsId;
    /**
     * 权限名称
     */
    private String opName;
    /**
     * 权限备注
     */
    private String opNote;
    /**
     * 权限父id
     */
    private int opIId;
    /**
     * 权限url
     */
    private String opAction;
    /**
     * 权限类型
     */
    private int opType;
    /**
     * 是否检查 1不检查 0检查
     */
    private int opIsPass;

    /**
     * 构造器
     */
    public OperationBean(int opId, long rsId, String opName, String opNote, int opIId, String opAction, int opType, int opIsPass) {
        this.opId = opId;
        this.rsId = rsId;
        this.opName = opName;
        this.opNote = StringUtil.transNull(opNote);
        this.opIId = opIId;
        this.opAction = StringUtil.transNull(opAction);
        this.opType = opType;
        this.opIsPass = opIsPass;
    }

    /**
     * 无参构造器
     */
    public OperationBean() {
    }

    public int getOpId() {
        return opId;
    }

    public void setOpId(int opId) {
        this.opId = opId;
    }

    public long getRsId() {
        return rsId;
    }

    public void setRsId(long rsId) {
        this.rsId = rsId;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpNote() {
        return opNote;
    }

    public void setOpNote(String opNote) {
        this.opNote = opNote;
    }

    public int getOpIId() {
        return opIId;
    }

    public void setOpIId(int opIId) {
        this.opIId = opIId;
    }

    public String getOpAction() {
        return opAction;
    }

    public void setOpAction(String opAction) {
        this.opAction = opAction;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getOpIsPass() {
        return opIsPass;
    }

    public void setOpIsPass(int opIsPass) {
        this.opIsPass = opIsPass;
    }
}
