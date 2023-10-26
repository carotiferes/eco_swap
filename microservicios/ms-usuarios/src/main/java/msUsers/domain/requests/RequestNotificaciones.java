package msUsers.domain.requests;

import lombok.Data;

import java.util.List;

@Data
public class RequestNotificaciones {
    private List<Long> idNotificaciones;
}
