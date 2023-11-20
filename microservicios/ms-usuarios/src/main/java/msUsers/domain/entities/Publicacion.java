package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.TipoProducto;
import msUsers.domain.entities.enums.TipoPublicacion;
import msUsers.domain.logistica.enums.EstadoEnvio;
import msUsers.domain.responses.DTOs.PublicacionDTO;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "publicacion")
    private List<Compra> compras;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estadoEnvio;

    private String imagenes;

    private Double peso;

    public PublicacionDTO toDTO() {
        PublicacionDTO publicacionDTO = new PublicacionDTO();
        publicacionDTO.setIdPublicacion(idPublicacion);
        publicacionDTO.setParticularDTO(particular.toDTO());
        publicacionDTO.setFechaPublicacion(fechaPublicacion);
        publicacionDTO.setTitulo(titulo);
        publicacionDTO.setImagenes(imagenes);
        publicacionDTO.setDescripcion(descripcion);
        publicacionDTO.setTipoPublicacion(tipoPublicacion);
        publicacionDTO.setTipoProducto(tipoProducto);
        publicacionDTO.setEstadoPublicacion(estadoPublicacion);
        publicacionDTO.setEstadoEnvio(estadoEnvio);
        publicacionDTO.setPrecioVenta(precioVenta);
        publicacionDTO.setValorTruequeMax(valorTruequeMax);
        publicacionDTO.setValorTruequeMin(valorTruequeMin);
        publicacionDTO.setCaracteristicaProducto(caracteristicaProducto);
        publicacionDTO.setPeso(peso);
        publicacionDTO.setVendido(!compras.isEmpty());
        return publicacionDTO;
    }

}
