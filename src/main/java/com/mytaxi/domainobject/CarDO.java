package com.mytaxi.domainobject;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "car")
@Where(clause = "deleted=false")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CarDO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_created", nullable = false)
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

    @Column(name = "convertible")
    private Boolean isConvertible;

    @Column(name = "deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer")
    private ManufacturerDO manufacturerDO;

}
