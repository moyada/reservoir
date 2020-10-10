package com.moyada.reservoir.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Data
public class FujianTrend {

    private List<Data> data;

    @lombok.Data
    public static class Data {

        private String name;
        private String city_name;
        private String city_code;
        private String area_name;
        private String area_id;
        private String town_name;
        private String town_code;
        private String address;
        private String river_name;
        private String water_system;
        private Float val;
        private Float ensure_val;
        private Double lat;
        private Double lng;
    }
}
