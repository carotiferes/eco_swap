package msTransacciones.domain.responses;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.responses.logistica.resultResponse.ResultShippingService;

@Builder
@Data
public class ResponseShippingOption {

    private String fechaMinima;
    private String fechaMaxima;
    private Float precioBase;
    private Float precioConImpuestos;
    private ResultShippingService carrier;

}
