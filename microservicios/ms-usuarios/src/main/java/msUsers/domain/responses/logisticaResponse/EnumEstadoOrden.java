package msUsers.domain.responses.logisticaResponse;

public enum EnumEstadoOrden {

    Por_despachar("Por despachar"),
    Enviado("Enviado"),
    Entregado("Entregado"),
    Cancelado("Cancelado");

    private String estado;

    EnumEstadoOrden(String despachado) {
    }
}