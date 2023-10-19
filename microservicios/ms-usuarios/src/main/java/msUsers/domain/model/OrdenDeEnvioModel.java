package msUsers.domain.model;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.OrdenDeEnvio;

@Builder
@Data
public class OrdenDeEnvioModel {

    private String cantPaquetes;

    private String ordenId;

    private String usuarioIdEIniciales;

    private String codigoOrden;
    private String primeros5LetrasDeCodigo;
    private String tipoEntrega;
    private String codigoTipoEntrega;
    private String nombrePersona;
    private String direccionCalleYNumero;
    private String codigoPostal;
    private String barrio;
    private String ciudad;
    private String telefono;
    private String nroPiso;
    private String nroDepartamento;

    public static OrdenDeEnvioModel fromDomain(OrdenDeEnvio ordenDeEnvio) {
        return OrdenDeEnvioModel.builder()
                .ordenId(String.valueOf(ordenDeEnvio.getIdOrden()))
                .nroDepartamento(ordenDeEnvio.getDpto())
                .direccionCalleYNumero(ordenDeEnvio.getNombreCalle() + " "+ ordenDeEnvio.getAltura())
                .barrio(ordenDeEnvio.getBarrio())
                .ciudad(ordenDeEnvio.getCiudad())
                .telefono(ordenDeEnvio.getTelefono())
                .cantPaquetes("Paquete con "+ ordenDeEnvio.getCantidad() + " productos")
                .nroPiso(ordenDeEnvio.getPiso())
                .codigoPostal(ordenDeEnvio.getCodigoPostal())
                .usuarioIdEIniciales(ordenDeEnvio.getNombreUserOrigen().substring(0,2)+ordenDeEnvio.getIdOrden())
                .codigoOrden("18653"+ordenDeEnvio.getNombreUserDestino().substring(0,2)+ordenDeEnvio.getIdOrden())
                .build();
    }
}
