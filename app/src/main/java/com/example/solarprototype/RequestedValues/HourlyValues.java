package com.example.solarprototype.RequestedValues; ;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HourlyValues {

    @SerializedName("system_id")
    @Expose
    private Integer systemId;
    @SerializedName("total_devices")
    @Expose
    private Integer totalDevices;
    @SerializedName("intervals")
    @Expose
    private List<Interval> intervals = null;
    @SerializedName("meta")
    @Expose
    private meta met;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Integer totalDevices) {
        this.totalDevices = totalDevices;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public meta getMeta() {
        return met;
    }

    public void setMeta(meta meta) {
        this.met = meta;
    }

}
