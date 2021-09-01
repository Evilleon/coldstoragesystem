package com.ryq.coldstoragesystem.bean;


import lombok.Data;

@Data
public class ExportList {

    private Integer id;

    private String itemName;
    private String itemWeight;
    private String itemVolume;
    private String distance;
    private String warehousingDate;
    private String exportDate;
    private String operator;
    private String storageTime;
    private String allCost;
    private String consignee;
    private String contactInformation;
    private String receiveAddress;
    private String lat;
    private String lng;
    private String deliveryMode;
    private String deliverCost;
    private String amountCost;
    private String status;
    private String statusText;
    private String tempId;

}
