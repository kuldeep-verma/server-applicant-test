package com.mytaxi.domainobject;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "car")
public class CarDO
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(name = "color")
    private String color;

    @Column(name = "license_plate")
    @NotNull(message = "License Plate can not be null!")
    private String licensePlate;

    @Column(name = "engine_type")
    private String engineType;

    @Column(name = "seat_count")
    private Integer seatCount;

    @Column
    private Boolean convertible;

    @Column(nullable = false)
    private Boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "manufacturer")
    private ManufacturerDO manufacturerDO;

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the dateCreated
     */
    public ZonedDateTime getDateCreated()
    {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(ZonedDateTime dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the color
     */
    public String getColor()
    {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * @return the licensePlate
     */
    public String getLicensePlate()
    {
        return licensePlate;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the engineType
     */
    public String getEngineType()
    {
        return engineType;
    }

    /**
     * @param engineType the engineType to set
     */
    public void setEngineType(String engineType)
    {
        this.engineType = engineType;
    }

    /**
     * @return the seatCount
     */
    public Integer getSeatCount()
    {
        return seatCount;
    }

    /**
     * @param seatCount the seatCount to set
     */
    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
    }

    /**
     * @return the convertible
     */
    public Boolean getConvertible()
    {
        return convertible;
    }

    /**
     * @param convertible the convertible to set
     */
    public void setConvertible(Boolean convertible)
    {
        this.convertible = convertible;
    }

    /**
     * @return the deleted
     */
    public Boolean getDeleted()
    {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    /**
     * @return the manufacturer
     */
    public ManufacturerDO getManufacturer()
    {
        return manufacturerDO;
    }

    /**
     * @param manufacturerDO the manufacturer to set
     */
    public void setManufacturer(ManufacturerDO manufacturerDO)
    {
        this.manufacturerDO = manufacturerDO;
    }
}
