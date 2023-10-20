package msAutenticacion.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msAutenticacion.domain.entities.enums.TipoDocumento;
import msAutenticacion.domain.responses.DTOs.ParticularDTO;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Particulares")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Particular {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_particular")
    private long idParticular;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @NotNull
    @Size(max = 40)
    private String nombre;
    @NotNull
    @Size(max = 40)
    private String apellido;

    @NotNull
    @Size(max = 8)
    @Column(unique = true)
    private String dni;

    @NotNull
    @Size(max = 11)
    @Column(unique = true)
    private String cuil;

    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaNacimiento;

    @Enumerated(value = EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donacion> donaciones;

    private String publicKey;
    private String accessToken;

    public ParticularDTO toDTO() {
        ParticularDTO particularDTO = new ParticularDTO();
        particularDTO.setIdParticular(idParticular);
        particularDTO.setNombre(nombre);
        particularDTO.setApellido(apellido);
        particularDTO.setFechaNacimiento(fechaNacimiento);
        particularDTO.setDni(dni);
        particularDTO.setCuil(cuil);
        particularDTO.setTipoDocumento(tipoDocumento);
        particularDTO.setPuntaje(usuario.getPuntaje());
        particularDTO.setDirecciones(usuario.getDirecciones().stream().map(Direccion::toDTO).toList());
        particularDTO.setPublicKey(publicKey);
        particularDTO.setAccessToken(accessToken);
        return particularDTO;
    }
}

