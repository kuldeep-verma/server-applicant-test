package com.mytaxi.facade.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;

@Service
public class DefaultCarFacade implements CarFacade
{

    @Autowired
    CarService carService;


    @Override
    public CarDO findCarById(Long carId) throws EntityNotFoundException
    {
        return carService.findCarById(carId);
    }


    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        return carService.create(carDO);
    }


    @Override
    public void delete(Long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
    }

}
