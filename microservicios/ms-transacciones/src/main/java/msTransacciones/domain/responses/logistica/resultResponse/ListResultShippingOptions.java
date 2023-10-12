package msTransacciones.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class ListResultShippingOptions {
    private ArrayList<ResultShippingOptions> results;
}
