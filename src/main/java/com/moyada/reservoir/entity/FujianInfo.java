package com.moyada.reservoir.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Data
public class FujianInfo {

    private List<Data> data;

    @lombok.Data
    public static class Data {

        private String city;
        private String county;
        private String encl;
        private String river_name;
        private String name;
        private Float ensure_val;
        private Float catchment_area;
    }
}
