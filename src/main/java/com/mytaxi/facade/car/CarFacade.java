package com.mytaxi.facade.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

public interface CarFacade
{
    /**
     * To find a car by id
     * @param carId
     * @return CarDO
     * @throws EntityNotFoundException if no car found
     */
    CarDO findCarById(Long carId) throws EntityNotFoundException;


    /**
     * To create a new car
     * @param carDO
     * @return CarDo
     * @throws ConstraintsViolationException
     */
    CarDO create(CarDO carDO) throws ConstraintsViolationException;


    /**
     * To Delete a car by car id
     * @param carId
     * @throws EntityNotFoundException if no car found
     */
    void delete(Long carId) throws EntityNotFoundException;

}
