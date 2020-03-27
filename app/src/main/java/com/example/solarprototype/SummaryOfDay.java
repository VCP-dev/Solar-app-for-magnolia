
package com.example.solarprototype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryOfDay {

    @SerializedName("system_id")
    @Expose
    private Integer systemId;
    @SerializedName("modules")
    @Expose
    private Integer modules;
    @SerializedName("size_w")
    @Expose
    private Integer sizeW;
    @SerializedName("current_power")
    @Expose
    private Integer currentPower;
    @SerializedName("energy_today")
    @Expose
    private Integer energyToday;
    @SerializedName("energy_lifetime")
    @Expose
    private Integer energyLifetime;
    @SerializedName("summary_date")
    @Expose
    private String summaryDate;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("operational_at")
    @Expose
    private Integer operationalAt;
    @SerializedName("last_report_at")
    @Expose
    private Integer lastReportAt;
    @SerializedName("last_interval_end_at")
    @Expose
    private Integer lastIntervalEndAt;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getModules() {
        return modules;
    }

    public void setModules(Integer modules) {
        this.modules = modules;
    }

    public Integer getSizeW() {
        return sizeW;
    }

    public void setSizeW(Integer sizeW) {
        this.sizeW = sizeW;
    }

    public Integer getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(Integer currentPower) {
        this.currentPower = currentPower;
    }

    public Integer getEnergyToday() {
        return energyToday;
    }

    public void setEnergyToday(Integer energyToday) {
        this.energyToday = energyToday;
    }

    public Integer getEnergyLifetime() {
        return energyLifetime;
    }

    public void setEnergyLifetime(Integer energyLifetime) {
        this.energyLifetime = energyLifetime;
    }

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOperationalAt() {
        return operationalAt;
    }

    public void setOperationalAt(Integer operationalAt) {
        this.operationalAt = operationalAt;
    }

    public Integer getLastReportAt() {
        return lastReportAt;
    }

    public void setLastReportAt(Integer lastReportAt) {
        this.lastReportAt = lastReportAt;
    }

    public Integer getLastIntervalEndAt() {
        return lastIntervalEndAt;
    }

    public void setLastIntervalEndAt(Integer lastIntervalEndAt) {
        this.lastIntervalEndAt = lastIntervalEndAt;
    }

}
