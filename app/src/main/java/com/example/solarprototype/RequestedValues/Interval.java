package com.example.solarprototype.RequestedValues; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interval {

    @SerializedName("end_at")
    @Expose
    private String endAt;
    @SerializedName("devices_reporting")
    @Expose
    private Integer devicesReporting;
    @SerializedName("powr")
    @Expose
    private Integer powr;
    @SerializedName("enwh")
    @Expose
    private Integer enwh;

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public Integer getDevicesReporting() {
        return devicesReporting;
    }

    public void setDevicesReporting(Integer devicesReporting) {
        this.devicesReporting = devicesReporting;
    }

    public Integer getPowr() {
        return powr;
    }

    public void setPowr(Integer powr) {
        this.powr = powr;
    }

    public Integer getEnwh() {
        return enwh;
    }

    public void setEnwh(Integer enwh) {
        this.enwh = enwh;
    }

}
