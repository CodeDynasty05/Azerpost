package com.azerpost.app.model.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private Integer totalShipment;
    private Integer totalCourier;
    private Integer totalUser;
    private Integer totalReturn;
    private Integer totalOperator;
    private Integer successShipment;
    private Integer failedShipment;
}
