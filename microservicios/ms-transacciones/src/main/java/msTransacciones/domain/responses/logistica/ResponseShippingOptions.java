package msTransacciones.domain.responses.logistica;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.responses.logistica.resultResponse.ResultShippingOptions;

import java.util.ArrayList;

@Builder
@Data
public class ResponseShippingOptions {
    
    private ArrayList<ResultShippingOptions> results;
}
