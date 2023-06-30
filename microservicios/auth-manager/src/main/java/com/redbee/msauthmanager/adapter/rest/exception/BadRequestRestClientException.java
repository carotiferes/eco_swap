package com.redbee.msauthmanager.adapter.rest.exception;


import com.redbee.msauthmanager.config.ErrorCode;
import com.redbee.msauthmanager.config.GenericException;

public final class BadRequestRestClientException extends GenericException {

    public BadRequestRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
