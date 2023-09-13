package msUsers.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.donaciones.RequestComunicarDonacionColectaModel;
import msUsers.exceptions.DonacionCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ColectaService {

    @Autowired
    private ColectasRepository colectasRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ParticularesRepository particularesRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DonacionesRepository donacionesRepository;
    @Autowired
    private MensajeRespuestaRepository mensajeRespuestaRepository;
    @Autowired
    private CaracteristicaDonacionRepository caracteristicaDonacionRepository;

    public void crearDonacion(RequestComunicarDonacionColectaModel request, Long idColecta, Long idParticular) {
        log.info(">> SERVICE: Se comenzo la creacion de donacion para la colecta: {}", idColecta);
        Colecta colecta = colectasRepository.findById(idColecta).get();
        Producto producto = colecta.getProductos().
                   stream()
                   .filter(x -> x.getIdProducto() == request.getProductoId())
                   .findAny().get();
        List<CaracteristicaDonacion> lista = request.getCaracteristicas()
                   .stream()
                   .map(s -> CaracteristicaDonacion.armarCarateristica(s, idParticular))
                   .toList();
        Particular particular = particularesRepository.findById(idParticular).get();

        if(request.getCantidadOfrecida() > producto.getCantidadSolicitada())
            throw new DonacionCreationException("Error: Cantidad ofrecida mayor a la requerida.");

        try{
            Donacion donacionNueva = new Donacion();
            donacionNueva.setCantidadDonacion(request.getCantidadOfrecida());
            donacionNueva.setDescripcion(request.getMensaje());
            donacionNueva.setEstadoDonacion(EstadoDonacion.PENDIENTE);
            donacionNueva.setParticular(particular);
            donacionNueva.setProducto(producto);
            donacionNueva.setCaracteristicaDonacion(lista);
            donacionNueva.setFechaDonacion(LocalDate.now());

            List<String> nombreImagenes = new ArrayList<>();
            for (int i = 0; i < request.getImagenes().size(); i++) {
                nombreImagenes.add(imageService.saveImage(request.getImagenes().get(i)));
            }

            donacionNueva.setImagenes(String.join("|", nombreImagenes));
            var entity = this.donacionesRepository.save(donacionNueva);
            this.donacionesRepository.save(donacionNueva);
            log.info("<< Donacion creado con ID: {}", entity.getIdDonacion());
        } catch (Exception e){
            throw new DonacionCreationException("Error al crear la donacion: " + e.getMessage());
        }

        List<Donacion> listaDonaciones = colecta.getProductos().stream().flatMap(prod -> prod.getDonaciones().stream()).toList();

        log.info("<< Listado de donaciones originales de solciitud ID de donaciones: {}, cantidad original {}", idColecta, listaDonaciones.size());
        log.info("<< Colecta actualizada con ID de donaciones: {}", listaDonaciones.stream().map(Donacion::getIdDonacion).toList());
    }

    public List<Donacion> obtenerTodasLasDonaciones(Long idColecta) {
        log.info(">> Obtener todas las donaciones de la Colecta: {}", idColecta);
        Colecta colecta = this.colectasRepository.findById(idColecta)
                        .orElseThrow(() -> new EntityNotFoundException("No fue encontrada la colecta: " + idColecta));
        List<Donacion> donaciones = colecta.getProductos().stream().flatMap(prod -> prod.getDonaciones().stream()).toList();;
        log.info("<< Cantidad obtenidas: {}", donaciones.size());
        return donaciones;

    }

    public Donacion obtenerDonacionXIdDonacion(Long idDonacion) {
        log.info(">> Obtener donacion de colecta x id: {}", idDonacion);
        return donacionesRepository.findById(idDonacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la donacion con ID: " + idDonacion));
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
