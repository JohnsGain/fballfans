package com.john.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 张居瓦
 * 2019-02-14 11:50
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginalCaptureImage {
    /**
     * 设备ID
     */
    @JsonProperty("DeviceID")
    private String deviceID;
    @JsonProperty("SnapUuid")
    private String snapUuid;
    /**
     * 拍摄时间
     */
    @JsonProperty("ShotTime")
    private LocalDateTime shotTime;
    /**
     * 小图Url
     */
    @JsonProperty("StoragePath2")
    private String storagePath2;
    /**
     * 大图Url
     */
    @JsonProperty("StoragePath")
    private String storagePath;

    @JsonProperty("Height")
    private int height;
    @JsonProperty("Width")
    private int width;
    @JsonProperty("FileFormat")
    private String fileFormat;
    @JsonProperty("ImageID")
    private String imageID;

    @Override
    public String toString() {
        return "OriginalCaptureImage{" +
                "deviceID='" + deviceID + '\'' +
                ", snapUuid='" + snapUuid + '\'' +
                ", shotTime=" + shotTime +
                ", storagePath2='" + storagePath2 + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", fileFormat='" + fileFormat + '\'' +
                ", imageID='" + imageID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OriginalCaptureImage that = (OriginalCaptureImage) o;
        return height == that.height &&
                width == that.width &&
                Objects.equals(deviceID, that.deviceID) &&
                Objects.equals(snapUuid, that.snapUuid) &&
                Objects.equals(shotTime, that.shotTime) &&
                Objects.equals(storagePath2, that.storagePath2) &&
                Objects.equals(storagePath, that.storagePath) &&
                Objects.equals(fileFormat, that.fileFormat) &&
                Objects.equals(imageID, that.imageID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceID, snapUuid, shotTime, storagePath2, storagePath, height, width, fileFormat, imageID);
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSnapUuid() {
        return snapUuid;
    }

    public void setSnapUuid(String snapUuid) {
        this.snapUuid = snapUuid;
    }

    public LocalDateTime getShotTime() {
        return shotTime;
    }

    public void setShotTime(LocalDateTime shotTime) {
        this.shotTime = shotTime;
    }

    public String getStoragePath2() {
        return storagePath2;
    }

    public void setStoragePath2(String storagePath2) {
        this.storagePath2 = storagePath2;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }
}
