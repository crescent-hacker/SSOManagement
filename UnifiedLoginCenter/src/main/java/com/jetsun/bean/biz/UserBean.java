package com.jetsun.bean.biz;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/10/16
 * Desc:用户bean
 */
public class UserBean {
    /**
     * 用户id
     */
    private int userId;
    /**
     * 用户编码
     */
    private String operNo;
    /**
     * 用户名称
     */
    private String operName;
    /**
     * 用户状态
     */
    private int operState;
    /**
     * 统筹区id
     */
    private int areaId;
    /**
     * 医院id
     */
    private int hsptId;
    /**
     * 是否增加证书，添加用户时用
     */
    private int isAddCert;
    /**
     * 证书名，添加修改用户时用
     */
    private String certName;
    /**
     * 开始日期
     */
    private String operBegin;
    /**
     * 结束日期
     */
    private String operEnd;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public int getOperState() {
        return operState;
    }

    public void setOperState(int operState) {
        this.operState = operState;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getHsptId() {
        return hsptId;
    }

    public void setHsptId(int hsptId) {
        this.hsptId = hsptId;
    }

    public int getIsAddCert() {
        return isAddCert;
    }

    public void setIsAddCert(int isAddCert) {
        this.isAddCert = isAddCert;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getOperBegin() {
        return operBegin;
    }

    public void setOperBegin(String operBegin) {
        this.operBegin = operBegin;
    }

    public String getOperEnd() {
        return operEnd;
    }

    public void setOperEnd(String operEnd) {
        this.operEnd = operEnd;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId='" + userId + '\'' +
                ", operNo='" + operNo + '\'' +
                ", operName='" + operName + '\'' +
                ", operState=" + operState +
                ", areaId=" + areaId +
                ", hsptId=" + hsptId +
                ", isAddCert='" + isAddCert + '\'' +
                ", certName='" + certName + '\'' +
                ", operBegin='" + operBegin + '\'' +
                ", operEnd='" + operEnd + '\'' +
                '}';
    }
}
