package org.sjf.demo.dto;

/**
 * Created by SJF on 2017/6/12.
 */
public class JsonData<T> {

    private int errCode;
    private String errMsg;
    private T content;

    public JsonData(int errCode, String errMsg, T content) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.content = content;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "JsonData{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", content=" + content +
                '}';
    }
}
