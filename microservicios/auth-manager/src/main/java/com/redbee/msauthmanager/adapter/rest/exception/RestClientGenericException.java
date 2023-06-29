package com.redbee.msauthmanager.adapter.rest.exception;


import com.redbee.msauthmanager.config.ErrorCode;
import com.redbee.msauthmanager.config.GenericException;

public final class RestClientGenericException extends GenericException {

    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }

}
