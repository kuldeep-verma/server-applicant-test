package com.mytaxi.service.driver;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import java.util.List;

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

}