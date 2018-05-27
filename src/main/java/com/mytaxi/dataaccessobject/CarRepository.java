package com.mytaxi.dataaccessobject;

import org.springframework.data.repository.CrudRepository;

import com.mytaxi.domainobject.CarDO;

/**
 * Database Access Object for car table.
 * <p/>
 */
public interface CarRepository extends CrudRepository<CarDO, Long>
{

}
