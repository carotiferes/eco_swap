package msUsers.domain.responses;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.responses.logistica.resultResponse.ResultShippingOptions;

import java.util.ArrayList;

@Builder
@Data
public class ResponseShippingOptions {
    
    private ArrayList<ResultShippingOptions> results;
}
