package com.moyada.reservoir.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Data
@Entity
@Table(name="reservoir_info")
public class ReservoirInfoDO {

    @Id	//主键id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//主键生成策略
    @Column(name="id")//数据库字段名
    private Integer id;

    private String name;
    @Column(name="origin_name")
    private String originName;
    private Double area;

    private Integer level;

    private String province;
    @Column(name="province_code")
    private String provinceCode;

    private String city;
    @Column(name="city_code")
    private String cityCode;

    private String district;
    @Column(name="district_code")
    private String districtCode;

    private String town;
    @Column(name="town_code")
    private String townCode;

    private String address;

    @Column(name="river_name")
    private String riverName;
    @Column(name="water_source")
    private String waterSource;

    private Double latitude;
    private Double longitude;

    @Column(name="high_water")
    private Double highWater;

    @Column(name="water_line")
    private Double waterLine;

    @Column(name="water_trend")
    private Double waterTrend;

    private Byte deleted;

    @Column(name="create_time")
    private Timestamp createTime;

    @Column(name="update_time")
    private Timestamp updateTime;
}
