package msTransacciones.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import msTransacciones.domain.entities.enums.EstadoDonacion;
import msTransacciones.domain.responses.DTOs.DonacionDTO;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Donaciones")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idDonacion")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idDonacion;

    private String descripcion;
    private int cantidadDonacion;

    @Enumerated(value = EnumType.STRING)
    private EstadoDonacion estadoDonacion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Particular particular;

    @ManyToOne(cascade = CascadeType.ALL)
    private Producto producto;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CaracteristicaDonacion> caracteristicaDonacion;

    private String imagenes;

    private LocalDate fechaDonacion;

    public DonacionDTO toDTO() {
        DonacionDTO donacionDTO = new DonacionDTO();
        donacionDTO.setIdDonacion(idDonacion);
        donacionDTO.setCantidadDonacion(cantidadDonacion);
        donacionDTO.setDescripcion(descripcion);
        donacionDTO.setCaracteristicaDonacion(caracteristicaDonacion);
        donacionDTO.setEstadoDonacion(estadoDonacion);
        donacionDTO.setImagenes(imagenes);
        donacionDTO.setParticularDTO(particular.toDTO());
        donacionDTO.setProducto(producto.toDTO(true));
        donacionDTO.setFechaDonacion(fechaDonacion);
        return donacionDTO;
    }
}
