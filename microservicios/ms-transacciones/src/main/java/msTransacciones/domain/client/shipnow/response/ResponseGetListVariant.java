package msTransacciones.domain.client.shipnow.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class ResponseGetListVariant {

        private ArrayList<ResponseVariant> results;
}
