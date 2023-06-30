package com.redbee.msauthmanager.adapter.database.repository;

import com.redbee.msauthmanager.adapter.database.model.UsuarioModel;
import org.springframework.data.repository.CrudRepository;

public interface PerfilesRepository extends CrudRepository<UsuarioModel, Long> {

    UsuarioModel findByUsername(String username);

    UsuarioModel findByEmail(String email);
}
