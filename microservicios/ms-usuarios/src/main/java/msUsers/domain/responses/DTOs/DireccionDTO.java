package msUsers.domain.responses.DTOs;


import lombok.Data;

@Data
public class DireccionDTO {
    private String direccion;
    private String altura;
    private String piso;
    private String dpto;
    private String codigoPostal;
}
