package com.john.auth.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 张居瓦
 * 2019-02-16 16:27
 **/
public class CameraCaptureImage extends OriginalCaptureImage{

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraCaptureImage.class);
    private String url;
    private String deviceID;
    private String deviceName;
    private LocalDateTime shotTime;

    public CameraCaptureImage() {
    }

    public CameraCaptureImage(String url, String deviceID, String deviceName, LocalDateTime shotTime) {
        this.url = url;
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.shotTime = shotTime;
    }

    public CameraCaptureImage(OriginalCaptureImage captureImage) {
        this.deviceID = captureImage.getDeviceID();
        this.shotTime = captureImage.getShotTime();
        this.url = captureImage.getStoragePath2();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CameraCaptureImage that = (CameraCaptureImage) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(deviceID, that.deviceID) &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(shotTime, that.shotTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, deviceID, deviceName, shotTime);
    }

    @Override
    public String toString() {
        return "CameraCaptureImage{" +
                "url='" + url + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", shotTime=" + shotTime +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public LocalDateTime getShotTime() {
        return shotTime;
    }

    public void setShotTime(LocalDateTime shotTime) {
        this.shotTime = shotTime;
    }
}
