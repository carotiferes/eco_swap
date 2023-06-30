package com.redbee.msauthmanager.application.exception;


import com.redbee.msauthmanager.config.ErrorCode;
import com.redbee.msauthmanager.config.GenericException;

public class BusinessException extends GenericException {

    public BusinessException(ErrorCode errorCode){
        super(errorCode);
    }
}
