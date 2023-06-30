package com.redbee.msauthmanager.adapter.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Slf4j
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(name = "s_usuario")
    private String username;

    @Column(name = "s_mail")
    private String email;

    @Column(name = "s_password")
    private String password;

    @Column(name = "s_salt")
    private String salt;

    @Column(name = "n_intentos")
    private int intentos;

    @Column(name = "b_bloqueado")
    private int bloqueado;

    @Column(name = "b_baja_logica")
    private int baja_logica;

    public void aumentarIntentos() {
        if(intentos==3) {
            log.info("Usuario {} superó 3 intentos y será bloqueado", getUsername());
            this.setBloqueado(1);
        }
        int actualizado = intentos+1;
        log.info("Usuario {} aumentó número de intentos en a {}", getUsername(), actualizado);
        this.setIntentos(actualizado);
    }
}
