package com.mytaxi.facade.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.search.SearchService;

/**
 * 
 * Search facade implementation
 *
 */
@Service
public class DefaultSearchFacade implements SearchFacade
{

    private final SearchService searchService;


    @Autowired
    public DefaultSearchFacade(final SearchService searchService)
    {
        this.searchService = searchService;
    }


    @Override
    public List<DriverDO> searchDriversByAttributes(String username, OnlineStatus onlinestatus, CarDO carDO) throws EntityNotFoundException
    {
        return searchService.searchDriversByAttributes(username, onlinestatus, carDO);
    }

}
