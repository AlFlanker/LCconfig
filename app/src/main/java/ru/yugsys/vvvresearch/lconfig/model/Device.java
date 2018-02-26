package ru.yugsys.vvvresearch.lconfig.model;

public class Device {

    public Long id;
    public String type;
    public String eui;
    public String appeui;
    public String appkey;
    public String nwkid;
    public String devadr;
    public String nwkskey;
    public String appskey;
    public double Latitude;
    public double Longitude;
    public String outType;
    public String kV;
    public String kI;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEui() {
        return eui;
    }

    public void setEui(String eui) {
        this.eui = eui;
    }

    public String getAppeui() {
        return appeui;
    }

    public void setAppeui(String appeui) {
        this.appeui = appeui;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getNwkid() {
        return nwkid;
    }

    public void setNwkid(String nwkid) {
        this.nwkid = nwkid;
    }

    public String getDevadr() {
        return devadr;
    }

    public void setDevadr(String devadr) {
        this.devadr = devadr;
    }

    public String getNwkskey() {
        return nwkskey;
    }

    public void setNwkskey(String nwkskey) {
        this.nwkskey = nwkskey;
    }

    public String getAppskey() {
        return appskey;
    }

    public void setAppskey(String appskey) {
        this.appskey = appskey;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getkV() {
        return kV;
    }

    public void setkV(String kV) {
        this.kV = kV;
    }

    public String getkI() {
        return kI;
    }

    public void setkI(String kI) {
        this.kI = kI;
    }
}
