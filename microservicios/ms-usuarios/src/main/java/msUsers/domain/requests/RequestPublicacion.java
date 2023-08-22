package msUsers.domain.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;
import msUsers.domain.entities.enums.TipoPublicacion;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class RequestPublicacion {

    @Size(max = 50)
    @NotBlank(message = "La publicación debe contener un título.")
    private String titulo;

    @Size(max = 300)
    @NotBlank(message = "La publicación debe contener una descripción.")
    private String descripcion;

    @NotNull(message = "La publicación debe contener al menos una imagen.")
    private List<String> imagen;

    private long idParticular;

    private TipoProducto tipoProducto;

    private List<String> caracteristicasProducto;

    private TipoPublicacion tipoPublicacion;

    /* A definir en base al comentario de Caro: Tiene una fecha de inicio y una fecha de fin
       al igual que las colectas? */
    private LocalDate fechaPublicacion;

    private Boolean esVenta;

    // Contemplar el caso que precioVenta no tenga valor ingresado
    @Positive(message = "El precio de venta debe ser mayor a cero.")
    private Double precioVenta;

    @Positive(message = "El valor minimo del trueque debe ser mayor a cero.")
    private Double valorTruequeMin;

    @Positive(message = "El valor maximo del trueque debe ser mayor a cero.")
    private Double valorTruequeMax;

    @AssertTrue(message = "El valor máximo del trueque debe ser mayor que el valor mínimo.")
    public boolean isValorMaximoValido() {
        return valorTruequeMax > valorTruequeMin;
    }
}
