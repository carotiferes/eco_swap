package com.redbee.msauthmanager.adapter.rest.exception;


import com.redbee.msauthmanager.config.ErrorCode;
import com.redbee.msauthmanager.config.GenericException;

public final class NonTargetRestClientException extends GenericException {

    public NonTargetRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
