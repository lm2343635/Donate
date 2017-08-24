package com.xwkj.donate.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "donate_donation")
public class Donation implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String did;

    @Column(nullable = false)
    private Long createAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer money;

    @Column(nullable = false)
    private Boolean payed;

    @Column
    private Long payAt;

    @ManyToOne
    @JoinColumn(name = "wid", nullable = false)
    private Wechater wechater;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Long getPayAt() {
        return payAt;
    }

    public void setPayAt(Long payAt) {
        this.payAt = payAt;
    }

    public Wechater getWechater() {
        return wechater;
    }

    public void setWechater(Wechater wechater) {
        this.wechater = wechater;
    }

}
