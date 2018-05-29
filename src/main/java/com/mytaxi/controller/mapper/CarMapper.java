package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;

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


    public static CarDO makeCarDO(CarDTO carDTO)
    {
        CarDO carDO = new CarDO();
        carDO.setIsConvertible(carDTO.getConvertible());
        carDO.setEngineType(carDTO.getEngineType());
        carDO.setLicensePlate(carDTO.getLicensePlate());
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName(carDTO.getManufacturerName());
        carDO.setManufacturerDO(manufacturerDO);
        carDO.setSeatCount(carDTO.getSeatCount());

        return carDO;
    }
}
