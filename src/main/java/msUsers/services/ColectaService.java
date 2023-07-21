package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.donaciones.RequestComunicarDonacionColectaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ColectaService {

    @Autowired
    ColectaRepository colectaRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ParticularRepository particularRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    DonacionesRepository donacionesRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    @Autowired
    CaracteristicaDonacionRepository caracteristicaDonacionRepository;

    public void crearDonacionComunicacion(RequestComunicarDonacionColectaModel request, Long idcolecta) {
           log.info(">> SERVICE: Se comenzo la creacion de donacion para la colecta: {}", idcolecta);
           Colecta colecta = colectaRepository.findById(idcolecta).get();
           Producto producto = colecta.getProductos().
                   stream()
                   .filter(x -> x.getIdProducto() == request.getColectaProductoModel().getProductoId())
                   .findAny().get();
           List<CaracteristicaDonacion> lista = request.getColectaProductoModel()
                   .getCaracteristicas()
                   .stream()
                   .map(s -> CaracteristicaDonacion.armarCarateristica(s, request.getIdParticular()))
                   .toList();
           Particular particular = particularRepository.findById(request.getIdParticular()).get();

           List<String> nombreImagenes = new ArrayList<>();
           for (int i = 0; i < request.getColectaProductoModel().getImagenes().size(); i++) {
               nombreImagenes.add(imageService.saveImage(request.getColectaProductoModel().getImagenes().get(i)));
           }

           Donacion donacionNueva = new Donacion();
           donacionNueva.setCantidadDonacion(request.getColectaProductoModel().getCantidadOfrecida());
           donacionNueva.setDescripcion(request.getColectaProductoModel().getMensaje());
           donacionNueva.setEstadoDonacion(EstadoDonacion.PENDIENTE);
           donacionNueva.setParticular(particular);
           donacionNueva.setProducto(producto);
           donacionNueva.setCaracteristicaDonacion(lista);
           donacionNueva.setColecta(colecta);
           donacionNueva.setImagenes(String.join("|", nombreImagenes));


           log.info("<< Donacion creado con ID: {}", donacionNueva.getIdDonacion());
           List<Donacion> listaDonaciones = colecta.getDonaciones();
           log.info("<< Listado de donaciones originales de solciitud ID de donaciones: {}, cantidad original {}",
                   idcolecta, listaDonaciones.size());
           listaDonaciones.add(donacionNueva);
           colecta.setDonaciones(listaDonaciones);
           var entity = colectaRepository.save(colecta);
           log.info("<< Colecta actualizado con ID de donaciones: {}",
                   listaDonaciones
                           .stream()
                           .map(Donacion::getIdDonacion)
                           .toList());
    }

    public List<Donacion> obtenerTodasLasdonacionesComunicacion(Long idcolecta) {
        log.info(">> Obtener todas las donaciones de comunicacion de una Colecta: {}", idcolecta);
        List<Donacion> donacioncolectas = colectaRepository.findById(idcolecta).get().getDonaciones();
        log.info("<< Cantidad obtenidas: {}", donacioncolectas.size());
        return donacioncolectas;

    }

    public Donacion obtenerdonacionesComunicacionXId(Long idDonacionComunicacion) {
        log.info(">> Obtener 1 donacion de comunicacion de colecta x id: {}", idDonacionComunicacion);
        return donacionesRepository.findById(idDonacionComunicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la comunicacion de donacion con ID: "
                        + idDonacionComunicacion));
    }
/* POSTERGAMOS ESTE DESARROLLO
    //PUEDE ACTUALIZAR LA DONACION E INCLUSO LA colecta
    public void agregarMensajeParaDonacionComunicacion(
            RequestMensajeRespuesta request, Long idcolecta, Long idDonacion) {
        log.info(">> Service crear mensaje para comunicacion de donacion con request: {}", request.toString());

        Donacion donacion = donacionesRepository.findById(idDonacion).get();
        donacion.setCaracteristicaDonacion();

        MensajeRespuesta respuesta = caracteristicaDonacionRepository.save(
                CaracteristicaDonacion.builder()
            //            .fechaYHora(LocalDateTime.now())
           //             .idEmisor(request.getIdEmisor())
                        .mensaje(request.getMensaje())
                        .build()
        );
        log.info("<< Mensaje para comunicacion de donacion con CREADO");
        caracteristicaDonacionRepository.addNuevaRespuesta(respuesta);

        if(request.getEstadoDonacion()!= null) {
            donacioncolecta.setEstadoDonacion(request.getEstadoDonacion());
            if(request.getEstadoDonacion().equals(EstadoDonacion.RECIBIDA)) {
                //ACTULIZA LA colecta LA CANTDIDAD YA OBTENIDA
         //       donacioncolecta.set

                Colecta colecta = colectaRepository.findById(idcolecta).get();
                colecta.setActiva(false);
                Producto productoAActualizar = colecta.getProductos()
                        .stream()
                        .filter(producto ->
                                producto.getIdProducto()==donacioncolecta.getIdProducto())
                        .toList().get(0);
                Integer sumatoriaProductosSolicitados = productoAActualizar.getCantidadRecibida()
                        + donacioncolecta.getCantidadOfrecida();
                productoAActualizar.setCantidadRecibida(sumatoriaProductosSolicitados);
                colectaRepository.save(colecta);
            }
        }
        donacioncolectaRepository.save(donacioncolecta);
        log.info("<< Mensaje añadido a la lista de donacion id {}", idcolecta);
    }
    */

}
