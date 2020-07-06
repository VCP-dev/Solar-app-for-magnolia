package com.example.solarprototype.RequestedValues;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LifetimeValues {

    @SerializedName("system_id")
    @Expose
    private Integer systemId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("production")
    @Expose
    private List<Integer> production = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getProduction() {
        return production;
    }

    public void setProduction(List<Integer> production) {
        this.production = production;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }



}
