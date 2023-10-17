package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Store {
    private String id;
    private String created_at;
    private String updated_ar;
    private String name;
    private String url;
    private String image_url;
    private Boolean active;
    private Long seller_id;
    private String store_type;
}
