package com.xwkj.donate.component.config;

import net.sf.json.JSONObject;

public class Global {

    // Http protocal of Httper Web service.
    public String httpProtocol;

    // Domain nane of Httper Web service.
    public String domain;

    // Min donation money;
    public double min;

    public Global(JSONObject object) {
        this.httpProtocol = object.getString("httpProtocol");
        this.domain = object.getString("domain");
        this.min = object.getDouble("min");
    }

}
