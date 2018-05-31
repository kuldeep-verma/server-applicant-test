package com.mytaxi.dataaccessobject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>
{

    /**
     * Find drivers by OnlineStatus
     * @param onlineStatus
     * @return List of DriverDO
     */
    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);


    /**
     * To find driver by Car
     * @param carDO
     * @return DriverDO
     */
    DriverDO findByCarDO(CarDO carDO);


    /**
     * Find by user name
     * @param username
     * @return DriverDO
     */
    Optional<DriverDO> findByUsernameIgnoreCase(String username);


    @Query("SELECT D FROM DriverDO D "
        + "WHERE D.carDO IN (SELECT C.id FROM CarDO C "
        + "WHERE LOWER(C.licensePlate) = LOWER(:#{#carDO.getLicensePlate()}) "
        + "OR LOWER(C.color) = LOWER(:#{#carDO.getColor()}) "
        + "OR LOWER(C.engineType) = LOWER(:#{#carDO.getEngineType()}) "
        + "OR C.seatCount = :#{#carDO.getSeatCount()} "
        + "OR C.isConvertible = :#{#carDO.getIsConvertible()})")
    Optional<List<DriverDO>> findDriversByCarAttributes(@Param("carDO") final CarDO carDO);
}
