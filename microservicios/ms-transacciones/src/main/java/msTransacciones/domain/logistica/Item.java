package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Item {
    private Integer quantity;
    private String status;
    private String extra_data;
    private String discount;
    private Boolean cross_docking;
    private Long id;
    private String external_reference;
    private String title;
    private String image_url;
    private String thumbnail_url;
    private Boolean archived;
    private String picking_preference;
    private Stock stock;
    private Dimension dimensions;
}
