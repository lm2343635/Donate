package com.xwkj.donate.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wechat_token")
public class Token implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String tid;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private long tokenUpdate;

    @Column(nullable = false)
    private String ticket;

    @Column(nullable = false)
    private long ticketUpdate;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenUpdate() {
        return tokenUpdate;
    }

    public void setTokenUpdate(long tokenUpdate) {
        this.tokenUpdate = tokenUpdate;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getTicketUpdate() {
        return ticketUpdate;
    }

    public void setTicketUpdate(long ticketUpdate) {
        this.ticketUpdate = ticketUpdate;
    }

}
