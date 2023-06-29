package com.redbee.msauthmanager.adapter.database;

import com.redbee.msauthmanager.adapter.database.model.UsuarioModel;
import com.redbee.msauthmanager.adapter.database.repository.PerfilesRepository;
import com.redbee.msauthmanager.application.exception.BusinessException;
import com.redbee.msauthmanager.config.ErrorCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;

@Slf4j
@Repository
public class UsuariosAdapter {

    @Autowired
    private PerfilesRepository perfilesRepository;

    @SneakyThrows
    public void guardarUsuario(UsuarioModel nuevoUser) {
        try{
            perfilesRepository.save(nuevoUser);
        } catch (ConstraintViolationException e) {
            log.error("Error durante creacion del usuario: {}", e.getMessage());
            throw new BusinessException(ErrorCode.CREAR_USER_ERROR);
        }

    }

    @SneakyThrows
    public UsuarioModel obtenerUser(String username) {
        return perfilesRepository.findByUsername(username);
    }

    @SneakyThrows
    public UsuarioModel obtenerUserXEmail(String email) {
        return perfilesRepository.findByEmail(email);
    }

}
