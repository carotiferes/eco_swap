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
import java.time.ZoneId;
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
    private CaracteristicaDonacionRepository caracteristicaDonacionRepository;

    public Donacion crearDonacion(RequestComunicarDonacionColectaModel request, Long idColecta, Long idParticular) {
        log.info(">> SERVICE: Se comenzo la creacion de donacion para la colecta: {}", idColecta);
        Colecta colecta = colectasRepository.findById(idColecta).get();
        Donacion donacionNueva = new Donacion();
        Producto producto = colecta.getProductos().
                   stream()
                   .filter(x -> x.getIdProducto() == request.getProductoId())
                   .findAny().get();
        List<CaracteristicaDonacion> lista = request.getCaracteristicas()
                   .stream()
                   .map(s -> CaracteristicaDonacion.armarCarateristica(s, idParticular))
                   .toList();
        Particular particular = particularesRepository.findById(idParticular).get();

        // Validations
        if(request.getCantidadOfrecida() > producto.getCantidadSolicitada()-producto.getCantidadRecibida() &&  producto.getCantidadSolicitada() > 0) {
            throw new DonacionCreationException("Error: Cantidad ofrecida mayor a la requerida.");
        }
        if(LocalDate.now(ZoneId.of("GMT-3")).isBefore(colecta.getFechaInicio())) {
            throw new DonacionCreationException("Colecta aun iniciada.");
        }
        if(LocalDate.now(ZoneId.of("GMT-3")).isAfter(colecta.getFechaFin())) {
            throw new DonacionCreationException("Colecta expirada.");
        }

        try{
            donacionNueva.setCantidadDonacion(request.getCantidadOfrecida());
            donacionNueva.setDescripcion(request.getMensaje());
            donacionNueva.setEstadoDonacion(EstadoDonacion.PENDIENTE);
            donacionNueva.setParticular(particular);
            donacionNueva.setProducto(producto);
            donacionNueva.setCaracteristicaDonacion(lista);
            donacionNueva.setFechaDonacion(LocalDate.now(ZoneId.of("GMT-3")));

            List<String> nombreImagenes = new ArrayList<>();
            for (int i = 0; i < request.getImagenes().size(); i++) {
                nombreImagenes.add(imageService.saveImage(request.getImagenes().get(i)));
            }

            donacionNueva.setImagenes(String.join("|", nombreImagenes));
            var entity = this.donacionesRepository.save(donacionNueva);
            log.info("<< Donacion creado con ID: {}", entity.getIdDonacion());
        } catch (Exception e){
            throw new DonacionCreationException("Error al crear la donacion: " + e.getMessage());
        }

        List<Donacion> listaDonaciones = colecta.getProductos().stream().flatMap(prod -> prod.getDonaciones().stream()).toList();

        log.info("<< Listado de donaciones originales de solciitud ID de donaciones: {}, cantidad original {}", idColecta, listaDonaciones.size());
        log.info("<< Colecta actualizada con ID de donaciones: {}", listaDonaciones.stream().map(Donacion::getIdDonacion).toList());
        return donacionNueva;
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

}
