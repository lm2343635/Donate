package com.xwkj.donate.component.config;

import net.sf.json.JSONObject;

public class Text {

    public String tradeName;
    public String male;
    public String female;

    public Text(JSONObject object) {
        this.tradeName = object.getString("tradeName");
        this.male = object.getString("male");
        this.female = object.getString("female");
    }

}
