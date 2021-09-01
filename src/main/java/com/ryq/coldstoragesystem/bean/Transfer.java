package com.ryq.coldstoragesystem.bean;

import lombok.Data;

@Data
public class Transfer {
    private int id;
    private String oldRoom;
    private String newRoom;
    private String identifier;
    private String transferTimestamp;

}
