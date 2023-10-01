package msTransacciones.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msTransacciones.domain.entities.enums.EstadoPublicacion;
import msTransacciones.domain.entities.enums.TipoProducto;
import msTransacciones.domain.entities.enums.TipoPublicacion;
import msTransacciones.domain.responses.DTOs.PublicacionDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "Publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_publicacion")
    private long idPublicacion;

    @Size(max = 50)
    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    @Enumerated(EnumType.STRING)
    private TipoPublicacion tipoPublicacion;

    private LocalDate fechaPublicacion;

    private Double precioVenta;
    private Double valorTruequeMax;
    private Double valorTruequeMin;

    @ManyToOne(cascade = CascadeType.ALL)
    private Particular particular;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CaracteristicaProducto> caracteristicaProducto;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    private String imagenes;

    public PublicacionDTO toDTO() {
        PublicacionDTO publicacionDTO = new PublicacionDTO();
        publicacionDTO.setIdPublicacion(idPublicacion);
        publicacionDTO.setParticularDTO(particular.toDTO());
        publicacionDTO.setFechaPublicacion(fechaPublicacion);
        publicacionDTO.setTitulo(titulo);
        publicacionDTO.setImagenes(imagenes);
        publicacionDTO.setDescripcion(descripcion);
        publicacionDTO.setTipoPublicacion(tipoPublicacion);
        publicacionDTO.setEstadoPublicacion(estadoPublicacion);
        publicacionDTO.setPrecioVenta(precioVenta);
        publicacionDTO.setValorTruequeMax(valorTruequeMax);
        publicacionDTO.setValorTruequeMin(valorTruequeMin);
        publicacionDTO.setCaracteristicaProducto(caracteristicaProducto);
        return publicacionDTO;
    }

}
