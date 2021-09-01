package com.ryq.coldstoragesystem.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCubicle {

    private Integer id;
    private String code;
    private String status;
    private String comment;
    private Integer goodId;
    private String volume;
}
