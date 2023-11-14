package msAutenticacion.domain.responses.DTOs;


import lombok.Data;

@Data
public class DireccionDTO {
    private String calle;
    private String altura;
    private String piso;
    private String dpto;
    private String localidad;
    private String codigoPostal;
}
