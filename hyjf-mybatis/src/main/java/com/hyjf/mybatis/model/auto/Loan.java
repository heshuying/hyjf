package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class Loan implements Serializable {
    private Integer id;

    private String name;

    private String tel;

    private Integer sex;

    private Integer age;

    private String money;

    private String day;

    private String use;

    private String province;

    private String city;

    private String area;

    private String mortgage;

    private Integer mortgageState;

    private Integer state;

    private Integer addtime;

    private String addip;

    private String info;

    private String gname;

    private String gyear;

    private String gdress;

    private String gplay;

    private String gpro;

    private String gmoney;

    private String gget;

    private String gpay;

    private String remark;

    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use == null ? null : use.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage == null ? null : mortgage.trim();
    }

    public Integer getMortgageState() {
        return mortgageState;
    }

    public void setMortgageState(Integer mortgageState) {
        this.mortgageState = mortgageState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip == null ? null : addip.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    public String getGyear() {
        return gyear;
    }

    public void setGyear(String gyear) {
        this.gyear = gyear == null ? null : gyear.trim();
    }

    public String getGdress() {
        return gdress;
    }

    public void setGdress(String gdress) {
        this.gdress = gdress == null ? null : gdress.trim();
    }

    public String getGplay() {
        return gplay;
    }

    public void setGplay(String gplay) {
        this.gplay = gplay == null ? null : gplay.trim();
    }

    public String getGpro() {
        return gpro;
    }

    public void setGpro(String gpro) {
        this.gpro = gpro == null ? null : gpro.trim();
    }

    public String getGmoney() {
        return gmoney;
    }

    public void setGmoney(String gmoney) {
        this.gmoney = gmoney == null ? null : gmoney.trim();
    }

    public String getGget() {
        return gget;
    }

    public void setGget(String gget) {
        this.gget = gget == null ? null : gget.trim();
    }

    public String getGpay() {
        return gpay;
    }

    public void setGpay(String gpay) {
        this.gpay = gpay == null ? null : gpay.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}