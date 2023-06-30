package com.redbee.msauthmanager.adapter.rest.exception;


import com.redbee.msauthmanager.config.ErrorCode;
import com.redbee.msauthmanager.config.GenericException;

public class ConflictRestClientException extends GenericException {
    public ConflictRestClientException(ErrorCode ec) {
        super(ec);
    }
}
