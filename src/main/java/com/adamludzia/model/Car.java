package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @ApiModelProperty(value = "Car registration plate", required = true, example = "SR 99500")
    private String registrationPlate;

    @ApiModelProperty(value = "Is the car used for personal reasons?", required = true, example = "true")
    private boolean personalUsage;
}