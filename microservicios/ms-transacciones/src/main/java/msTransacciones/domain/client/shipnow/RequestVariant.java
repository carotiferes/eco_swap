package msTransacciones.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestVariant {

    private String external_reference;
    private String title;
    private String external_reference_user;
    private Integer stock;
    private Float price;
    private String currency;
    private String image_url;
    private VarianDimension dimensions;
}
