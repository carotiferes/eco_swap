package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShipDirection {
    private String zip_code;
    private String street_name;
    private String street_number;
    private String city;
    private String state;
    private String floor;
    private String unit;
    private String last_name;
    private String full_name;
    private String phone;
    private String email;
    private String lon;
    private String lat;
    private String doc_type;
    private String doc_number;
    private String line;
    private String address_line;
    private Country country;
    private String post_office_id;
    private String warehouse_id;
}
