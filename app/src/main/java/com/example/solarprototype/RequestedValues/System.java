
package com.example.solarprototype.RequestedValues;

import java.util.List;

import com.example.solarprototype.RequestedValues.Meta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class System {

    @SerializedName("system_id")
    @Expose
    private Integer systemId;
    @SerializedName("system_name")
    @Expose
    private String systemName;
    @SerializedName("system_public_name")
    @Expose
    private String systemPublicName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("other_references")
    @Expose
    private List<String> otherReferences = null;
    @SerializedName("connection_type")
    @Expose
    private String connectionType;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemPublicName() {
        return systemPublicName;
    }

    public void setSystemPublicName(String systemPublicName) {
        this.systemPublicName = systemPublicName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<String> getOtherReferences() {
        return otherReferences;
    }

    public void setOtherReferences(List<String> otherReferences) {
        this.otherReferences = otherReferences;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
