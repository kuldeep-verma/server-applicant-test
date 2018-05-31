package com.mytaxi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.facade.search.SearchFacade;

@RestController
@RequestMapping("v1/drivers/search")
public class SearchController
{
    private final SearchFacade searchFacade;


    @Autowired
    public SearchController(final SearchFacade searchFacade)
    {
        this.searchFacade = searchFacade;
    }


    @GetMapping("/attributes")
    public List<DriverDTO> searchDriversByAttributes(
        @Valid @RequestParam(required = false) String username, @Valid @RequestParam(required = false) OnlineStatus onlineStatus, @Valid @ModelAttribute CarDTO carDTO)
        throws EntityNotFoundException
    {
        List<DriverDO> driverDOs = searchFacade.searchDriversByAttributes(username, onlineStatus, CarMapper.makeCarDO(carDTO));

        List<DriverDTO> driverDTOs = new ArrayList<>();
        driverDOs.forEach(driverDO -> driverDTOs.add(DriverMapper.makeDriverDTO(driverDO, driverDO.getCarDO())));

        return driverDTOs;
    }
}
