package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CaracteristicaDonaciones")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CaracteristicaDonacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String caracteristica;

    public static CaracteristicaDonacion armarCarateristica(String caracteristica, Long userId) {
        return CaracteristicaDonacion.builder()
                .userId(userId)
                .caracteristica(caracteristica)
                .build();
    }
}
