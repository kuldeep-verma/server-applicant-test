package com.mytaxi.dataaccessobject;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

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
}
