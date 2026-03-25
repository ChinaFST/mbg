package com.dy.colony.mvp.model.entity;


/**
 * @author luoyl
 * @desc
 * @date 2026/3/23
 */
public class Platform_LoginBack {

    private String attributes;
    private ObjUserData obj;
    private String resultCode;
    private String msg;
    private boolean success;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public ObjUserData getObj() {
        return obj;
    }

    public void setObj(ObjUserData obj) {
        this.obj = obj;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
