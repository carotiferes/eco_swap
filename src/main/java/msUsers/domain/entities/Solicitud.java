package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Solicitudes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idSolicitud")
public class Solicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idSolicitud;

    @Size(max = 50)
    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

  //  @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaSolicitud;

    private boolean activa;

 //   @NotNull
    @Lob
    @Column(length = 100000)
    private String imagen;

  //  @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Fundacion fundacion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitud")
    @JsonManagedReference
    private List<Producto> productos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitud")
    @JsonManagedReference
    private List<Propuesta> propuestas;

}
