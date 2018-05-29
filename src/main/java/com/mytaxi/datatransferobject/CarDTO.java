package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is responsible for Car data.
 * <p/>
 */
@Data
@Builder
@NoArgsConstructor
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


    public CarDTO(Long id, String licensePlate, String color, String engineType, Boolean convertible, Integer seatCount, String manufacturerName)
    {
        super();
        this.id = id;
        this.licensePlate = licensePlate;
        this.color = color;
        this.engineType = engineType;
        this.convertible = convertible;
        this.seatCount = seatCount;
        this.manufacturerName = manufacturerName;
    }
}
