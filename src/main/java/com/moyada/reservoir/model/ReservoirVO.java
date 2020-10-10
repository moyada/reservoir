package com.moyada.reservoir.model;

import lombok.Data;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Data
public class ReservoirVO {

    private String name;
    private Double area;

    private String type;

    private String province;
    private String city;
    private String district;
    private String town;
    private String address;

    private Double waterLine;
    private Double waterTrend;
    private Double highWater;

    private Float latitude;
    private Float longitude;

    private String updateTime;
}
