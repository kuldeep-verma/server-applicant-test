package com.mytaxi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.facade.car.CarFacade;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
public class CarController
{

    private final CarFacade carFacade;


    @Autowired
    public CarController(final CarFacade carFacade)
    {
        this.carFacade = carFacade;
    }


    @GetMapping("/{carId}")
    public CarDTO getDriver(@Valid @PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carFacade.findCarById(carId));
    }

}
