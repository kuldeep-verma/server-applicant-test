package com.mytaxi.service.car;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.util.Constants;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some car specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService
{
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;


    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }


    @Override
    public CarDO findCarById(Long carId) throws EntityNotFoundException
    {
        return findCar(carId);
    }


    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            logger.warn(Constants.ERR_MSG_CONSTRAINTS, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCar(carId);
        carDO.setIsDeleted(true);
    }


    private CarDO findCar(Long carId) throws EntityNotFoundException
    {
        return carRepository
            .findById(carId)
            .orElseThrow(() -> new EntityNotFoundException(Constants.ERR_MSG_COULD_NOT_FIND_CAR + carId));
    }

}
