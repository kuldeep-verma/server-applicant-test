package com.mytaxi.service.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.util.Constants;

/**
 * 
 * Search service implementation
 *
 */
@Service
public class DefaultSearchService implements SearchService
{

    private final DriverRepository driverRepository;


    public DefaultSearchService(final DriverRepository driverRepository)
    {
        this.driverRepository = driverRepository;
    }


    @Override
    public List<DriverDO> searchDriversByAttributes(String username, OnlineStatus onlinestatus, CarDO carDO) throws EntityNotFoundException
    {
        return driverRepository.findDriversByAttributes(username, onlinestatus, carDO).orElseThrow(() -> new EntityNotFoundException(Constants.ERR_MSG_NOT_RESULT_FOUND));
    }

}
