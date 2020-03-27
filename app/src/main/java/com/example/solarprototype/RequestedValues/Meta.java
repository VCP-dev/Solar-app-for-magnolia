
package com.example.solarprototype.RequestedValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("last_report_at")
    @Expose
    private Integer lastReportAt;
    @SerializedName("last_energy_at")
    @Expose
    private Integer lastEnergyAt;
    @SerializedName("operational_at")
    @Expose
    private Integer operationalAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLastReportAt() {
        return lastReportAt;
    }

    public void setLastReportAt(Integer lastReportAt) {
        this.lastReportAt = lastReportAt;
    }

    public Integer getLastEnergyAt() {
        return lastEnergyAt;
    }

    public void setLastEnergyAt(Integer lastEnergyAt) {
        this.lastEnergyAt = lastEnergyAt;
    }

    public Integer getOperationalAt() {
        return operationalAt;
    }

    public void setOperationalAt(Integer operationalAt) {
        this.operationalAt = operationalAt;
    }

}
