package com.mytaxi.facade.driver;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverIsOfflineException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.driver.DriverService;
import com.mytaxi.util.Constants;

@Service
public class DefaultDriverFacade implements DriverFacade
{
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultDriverFacade.class);

    private final DriverService driverService;

    private final CarService carService;


    @Autowired
    public DefaultDriverFacade(final DriverService driverService, final CarService carService)
    {
        this.driverService = driverService;
        this.carService = carService;
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
    public DriverDTO selectCar(Long driverId, Long carId) throws EntityNotFoundException, ConstraintsViolationException, CarAlreadyInUseException, DriverIsOfflineException
    {
        DriverDO driverDO = driverService.find(driverId);
        CarDO carDO = carService.findCarById(carId);
        if (OnlineStatus.ONLINE.equals(driverDO.getOnlineStatus()))
        {
            if (driverService.isCarAlreadyInUse(carDO))
            {
                throw new CarAlreadyInUseException(Constants.ERR_MSG_CAR_ALREADY_IN_USE);
            }
            driverDO.setCarDO(carDO);
            driverDO = driverService.create(driverDO);
        }
        else
        {
            throw new DriverIsOfflineException(Constants.ERR_MSG_DRIVER_IS_OFFLINE);
        }

        return DriverMapper.makeDriverDTO(driverDO, carDO);
    }


    @Override
    public void deselectCar(long driverId) throws EntityNotFoundException, ConstraintsViolationException
    {
        DriverDO driverDO = driverService.find(driverId);
        if (null != driverDO)
        {
            CarDO carDO = driverDO.getCarDO();
            if (null != carDO)
            {
                driverDO.setCarDO(null);
                driverService.create(driverDO);
            }
            else
            {
                logger.info(Constants.ERR_MSG_NO_CAR_TO_DESELECT);
            }
        }
    }
}
