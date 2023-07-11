package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CaracteristicaPropuestas")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaracteristicaPropuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String caracteristica;

    public static CaracteristicaPropuesta armarCarateristica(String caracteristica, Long userId) {
        return CaracteristicaPropuesta.builder()
                .userId(userId)
                .caracteristica(caracteristica)
                .build();
    }
}
