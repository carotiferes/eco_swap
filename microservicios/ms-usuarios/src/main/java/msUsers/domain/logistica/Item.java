package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.client.shipnow.VarianDimension;

@Builder
@Data
public class Item {
    //NOMBRE COMO LO BUSCAREMOS EN LA API DE SHIPNOW
    private String external_reference;
    private String title;
    private Integer quantity;
    private String status;
    private String extra_data;
    private String discount;
    private Boolean cross_docking;
    private Long id;
    private Float price;
    private String image_url;
    private String thumbnail_url;
    private Boolean archived;
    private String picking_preference;
    private Stock stock;
    private VarianDimension dimensions;
}
