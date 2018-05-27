package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

/**
 * This class is responsible for Car data.
 * <p/>
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO
{

    private Long id;

    private String licensePlate;

    private String color;

    private String engineType;

    private Boolean convertible;

    private Integer seatCount;

    private String manufacturerName;
}
