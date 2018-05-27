package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

/**
 * 
 * To map car object
 *
 */
public class CarMapper
{
    public static CarDTO makeCarDTO(CarDO carDO)
    {
        return CarDTO
            .builder()
            .id(carDO.getId())
            .licensePlate(carDO.getLicensePlate())
            .color(carDO.getColor())
            .engineType(carDO.getEngineType())
            .convertible(carDO.getIsConvertible())
            .seatCount(carDO.getSeatCount())
            .manufacturerName(carDO.getManufacturerDO().getName())
            .build();
    }
}
