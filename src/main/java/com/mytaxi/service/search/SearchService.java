package com.mytaxi.service.search;

import java.util.List;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;

/**
 * 
 * Search service
 *
 */
public interface SearchService
{
    /**
     * Search Drivers by different attributes i.e. username or onlinestatus or car characteristics
     * @param username
     * @param onlinestatus
     * @param carDO
     * @return List of DriverDO
     * @throws EntityNotFoundException when no result found
     */
    List<DriverDO> searchDriversByAttributes(String username, OnlineStatus onlinestatus, CarDO carDO) throws EntityNotFoundException;
}
