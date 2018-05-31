package com.mytaxi.service.driver;

import java.util.List;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

/**
 * 
 * Deriver service
 *
 */
public interface DriverService
{

    /**
     * To find a driver, by id
     * @param driverId
     * @return DriverDO
     * @throws EntityNotFoundException
     */
    DriverDO find(Long driverId) throws EntityNotFoundException;


    /**
     * To create new driver
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException
     */
    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;


    /**
     * To delete existing driver
     * @param driverId
     * @throws EntityNotFoundException
     */
    void delete(Long driverId) throws EntityNotFoundException;


    /**
     * To update location
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;


    /**
     * Find by Online status
     * @param onlineStatus
     * @return list of drivers
     */
    List<DriverDO> find(OnlineStatus onlineStatus);


    /**
     * To check car already in use
     * @param CarDO
     * @return true if car is already in user otherwise false
     */
    boolean isCarAlreadyInUse(CarDO carDO);


    /**
     * Find a driver user name
     * @param username
     * @return DriverDO
     * @throws EntityNotFoundException
     */
    DriverDO findDriverByUsername(String username) throws EntityNotFoundException;


    /**
     * Search drivers by car attributes
     * @param carDO
     * @return List of DriverDOs
     */
    List<DriverDO> findDriversByCarAttributes(CarDO carDO) throws EntityNotFoundException;
}
