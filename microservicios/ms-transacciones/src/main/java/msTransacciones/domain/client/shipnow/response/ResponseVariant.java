package msTransacciones.domain.client.shipnow.response;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.client.shipnow.VarianDimension;
import msTransacciones.domain.client.shipnow.VarianPrice;
import msTransacciones.domain.client.shipnow.VarianStock;

@Builder
@Data
public class ResponseVariant {

    private Boolean archived;
    private String created_at;
    private String currency;
    private String external_reference;
    private String external_reference_user;
    private Long id;
    private Integer min_stock;
    private String picking_preference;
    private String title;
    private Boolean serializable;
    private String type;
    private String updated_at;
    private Long seller_id;
    private String seller_name;
    private VarianStock stock;
    private VarianDimension dimensions;
    private VarianDimension declared_dimensions;
    private VarianPrice price;
    private String image_url;
    private String thumbnail_url;
}
