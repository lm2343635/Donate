package com.xwkj.donate.bean;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class JSAPIResult {

    private String nonceStr;
    private String prepayId;
    private String timestamp;
    private String paySign;

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public JSAPIResult(String nonceStr, String prepayId, String timestamp, String paySign) {
        this.nonceStr = nonceStr;
        this.prepayId = prepayId;
        this.timestamp = timestamp;
        this.paySign = paySign;
    }
}
