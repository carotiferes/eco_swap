package msUsers.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Colecta;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.logistica.enums.EstadoEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class DonacionService {

    @Autowired
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 0 * * ?") // Ejecuta la tarea todos los días a la medianoche
    @Transactional
    public void verificarDonacionesExpiradas() {

        log.info(">> Expiracion automatica de donaciones del día: {}", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        LocalDateTime fechaExpiracion = LocalDateTime.now().minusDays(3);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Donacion> query = criteriaBuilder.createQuery(Donacion.class);
        Root<Donacion> donacionRoot = query.from(Donacion.class);

        Predicate expiradaPredicate = criteriaBuilder.and(
                criteriaBuilder.notEqual(donacionRoot.get("estadoDonacion"), EstadoDonacion.EN_ESPERA),
                criteriaBuilder.lessThanOrEqualTo(donacionRoot.get("fechaDonacion"), fechaExpiracion)
        );

        query.where(expiradaPredicate);

        List<Donacion> donacionesExpiradas = entityManager.createQuery(query).getResultList();

        // Actualiza el estado de las donaciones expiradas
        donacionesExpiradas.forEach(d -> d.setEstadoDonacion(EstadoDonacion.EXPIRADA));
        donacionesExpiradas.forEach(entityManager::merge);
        log.info("<< Donaciones expiradas: {}", donacionesExpiradas.size());
    }

    public int getCantidadEnCaminoPorProducto(Long idProducto){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Donacion> query = cb.createQuery(Donacion.class);
        Root<Donacion> from = query.from(Donacion.class);
        Predicate predicate;

        predicate = cb.and(cb.equal(from.get("producto").get("idProducto"), idProducto));

        predicate = cb.and(predicate, cb.or(
                cb.equal(from.get("estadoDonacion"), EstadoDonacion.EN_ESPERA),
                cb.equal(from.get("estadoEnvio"), EstadoEnvio.POR_CONFIGURAR),
                cb.equal(from.get("estadoEnvio"), EstadoEnvio.ENVIADO),
                cb.equal(from.get("estadoEnvio"), EstadoEnvio.POR_DESPACHAR)
        ));

        query.where(predicate);
        List<Donacion> donaciones = entityManager.createQuery(query).getResultList();
        log.info("Cantidad en envio: {}", donaciones.stream().mapToInt(Donacion::getCantidadDonacion).sum());
        return donaciones.stream().mapToInt(Donacion::getCantidadDonacion).sum();
    }

    public Colecta getColectaPorIdDonacion(Long idDonacion){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Colecta> query = cb.createQuery(Colecta.class);
        Root<Colecta> from = query.from(Colecta.class);
        Join<Colecta, Producto> colectaJoin = from.join("productos");
        Join<Producto, Donacion> donacionJoin = colectaJoin.join("donaciones");
        Predicate predicate;

        predicate = cb.and(cb.equal(donacionJoin.get("idDonacion"), idDonacion));

        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
