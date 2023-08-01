package msUsers.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Part;
import msUsers.domain.entities.CaracteristicaDonacion;
import msUsers.domain.entities.Particular;
import msUsers.domain.repositories.ParticularRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.repositories.DonacionesRepository;
import msUsers.domain.requests.donaciones.RequestCambiarEstadoDonacion;
import msUsers.domain.responses.ResponseUpdateEntity;

@ExtendWith(MockitoExtension.class)
public class DonacionControllerTest{

    @Mock
    private DonacionesRepository donacionesRepository;
    @Mock
    private ParticularRepository particularRepository;


    @InjectMocks
    private DonacionController donacionController;

    @Test
    public void cambiarEstadoValid() {
        // Arrange
        Long idDonacion = 1L;
        RequestCambiarEstadoDonacion request = new RequestCambiarEstadoDonacion();
        request.setNuevoEstado("PENDIENTE");

        Donacion donacion = new Donacion();
        donacion.setEstadoDonacion(EstadoDonacion.APROBADA);

        when(donacionesRepository.findById(idDonacion)).thenReturn(Optional.of(donacion));

        // Act
        ResponseEntity<ResponseUpdateEntity> response = donacionController.cambiarEstadoDonacion(idDonacion, request);

        // Assert
        verify(donacionesRepository, times(1)).findById(idDonacion);
        verify(donacionesRepository, times(1)).save(any(Donacion.class));
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getStatus().equals(HttpStatus.OK.name());
        assert response.getBody().getDescripcion().contains("APROBADA");
        assert response.getBody().getDescripcion().contains("PENDIENTE");
    }

    @Test
    public void cambiarEstadoInvalid() {
        // Arrange
        Long idDonacion = 1L;
        RequestCambiarEstadoDonacion request = new RequestCambiarEstadoDonacion();
        request.setNuevoEstado("INVALIDO");

        Donacion donacion = new Donacion();
        donacion.setEstadoDonacion(EstadoDonacion.APROBADA);

        when(donacionesRepository.findById(idDonacion)).thenReturn(Optional.of(donacion));

        // Act
        ResponseEntity<ResponseUpdateEntity> response = donacionController.cambiarEstadoDonacion(idDonacion, request);

        // Assert
        verify(donacionesRepository, times(1)).findById(idDonacion);
        verify(donacionesRepository, never()).save(any(Donacion.class));
        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().getStatus().equals(HttpStatus.BAD_REQUEST.name());
        assert response.getBody().getDescripcion().contains("INVALIDO");
    }

    @Test
    public void listValid(){
        Long idDonacion = 1L;
        Long idParticular = 2L;
        List<CaracteristicaDonacion> caracteristicaDonacionList = new ArrayList<>();
        List<String> listaDonacion1 = new ArrayList<>();
        listaDonacion1.add("Marrón");

        Donacion donacion1 = new Donacion();
        donacion1.setEstadoDonacion(EstadoDonacion.APROBADA);
        donacion1.setCantidadDonacion(2);
        donacion1.setCaracteristicaDonacion(listaDonacion1.stream().map(s -> CaracteristicaDonacion.armarCarateristica(s, idParticular)).toList());
        donacion1.setDescripcion("Campera talle XL");
        donacion1.setImagenes("imagen.jpg");
        donacion1.setParticular(new Particular());

        Donacion donacion2 = new Donacion();
        donacion1.setEstadoDonacion(EstadoDonacion.EN_ESPERA);
        donacion1.setCantidadDonacion(14);
        donacion1.setCaracteristicaDonacion(listaDonacion1.stream().map(s -> CaracteristicaDonacion.armarCarateristica(s, idParticular)).toList());
        donacion1.setDescripcion("Manta large");
        donacion1.setImagenes("imagen2.jpg");
        donacion1.setParticular(new Particular());

        List<Donacion> donaciones = new ArrayList<>();
        donaciones.add(donacion1);
        donaciones.add(donacion2);

        Particular particular = new Particular();
        particular.setNombre("Micaela");
        particular.setApellido("Romero");
        particular.setDni("25739291");
        particular.setDonaciones(donaciones);
        particular.setIdParticular(idParticular);
        particular.setFechaNacimiento(LocalDate.of(1997, 11, 25));

        when(particularRepository.findById(idParticular)).thenReturn(Optional.of(particular));

        // Act
        ResponseEntity<List<Donacion>> response = donacionController.listarDonaciones(idParticular);

        // Assert
        verify(particularRepository, times(1)).findById(idParticular);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().size() == 2;
        assert response.getBody().contains(donacion1);
        assert response.getBody().contains(donacion2);
    }

    @Test
    public void listInvalid(){
        Long idParticular = 1L;

        when(particularRepository.findById(idParticular)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            donacionController.listarDonaciones(idParticular);
        });

        // Verify
        verify(particularRepository, times(1)).findById(idParticular);
        verifyNoMoreInteractions(particularRepository);
    }

}
