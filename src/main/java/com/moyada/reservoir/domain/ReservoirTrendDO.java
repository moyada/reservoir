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
@Table(name="reservoir_trend")
public class ReservoirTrendDO {

    @Id	//主键id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//主键生成策略
    @Column(name="id")//数据库字段名
    private Integer id;

    @Column(name="reservoir_id")
    private Integer reservoirId;

    @Column(name="water_line")
    private Double waterLine;

    private Byte deleted;

    @Column(name="create_time")
    private Timestamp createTime;

    @Column(name="update_time")
    private Timestamp updateTime;
}
