package com.xwkj.donate.component.config;

import net.sf.json.JSONObject;

public class Mail {

    public boolean using;

    // Senders's email address
    public String sender;

    // SMTP server address
    public String smtp;

    // User name for SMTP server
    public String username;

    // Password for SMTP server
    public String password;

    public Mail(JSONObject object) {
        this.using = object.getBoolean("using");
        this.sender = object.getString("sender");
        this.smtp = object.getString("smtp");
        this.username = object.getString("username");
        this.password = object.getString("password");
    }

}
