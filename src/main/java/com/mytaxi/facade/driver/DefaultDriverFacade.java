package com.mytaxi.facade.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DriverService;

@Service
public class DefaultDriverFacade implements DriverFacade
{
    private final DriverService driverService;


    @Autowired
    public DefaultDriverFacade(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return driverService.find(driverId);
    }


    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        return driverService.create(driverDO);
    }


    @Override
    public void delete(Long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);

    }


    @Override
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);

    }


    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {

        return driverService.find(onlineStatus);
    }

}
