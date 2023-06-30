package com.redbee.msauthmanager.config;

public enum ErrorCode {

    BAD_REQUEST(105,"105", "La request esta mal formateada", "MS-SEED-BAD_REQUEST"),
    INVALID_PARAMETERS_ERROR(110,"110", "{}", "MS-AUTH-MANAGER-INVALID_PARAMETERS"),
    WEB_CLIENT_GENERIC(103,"103", "Unexpected rest client error", "MS-AUTH-MANAGER-INTERNAL_SERVER_ERROR"),
    INTERNAL_ERROR(108,"108","Internal Error","MS-AUTH-MANAGER-INTERNAL_ERROR"),
    JWT_ERROR(110, "110", "Error durante la creación del JWT", "MS-AUTH-MANAGER-JWT"),
    CREAR_USER_ERROR(112, "112","Error durante la creación del usuario", "MS-AUTH-MANAGER-CREACION-USER"),
    LOGIN_ERROR(112, "112","Error durante el login del usuario", "MS-AUTH-MANAGER-CREACION-USER"),
    EMAIL_O_USER_YA_EXISTENTES(112, "112","Nombre de usuario o Email ya existe", "MS-AUTH-MANAGER-CREACION-USER"),
    INTENTO_ERROR(113, "113","Error durante el login del usuario. Intente nuevamente","MS-AUTH-MANAGER-INTENTO-LOGIN"),
    BLOQUEO_ERROR(114, "114","El usuario fue bloqueado, recupera su cuenta ingresando nueva contraseña", "MS-AUTH-MANAGER-BLOQUEO-LOGIN");
    private final int value;
    private final String status;
    private final String mensaje;
    private final String code;

    ErrorCode(int value,String status, String mensaje, String code) {
        this.value = value;
        this.status = status;
        this.mensaje = mensaje;
        this.code = code;
    }

    public int getValue() {
        return this.value;
    }

    public  String getStatus(){return this.status;}

    public String getMensaje() {
        return this.mensaje;
    }

    public String getCode() {
        return this.code;
    }
}