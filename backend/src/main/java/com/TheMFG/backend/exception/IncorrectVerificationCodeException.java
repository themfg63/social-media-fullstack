package com.TheMFG.backend.exception;

public class IncorrectVerificationCodeException extends RuntimeException{
    public IncorrectVerificationCodeException(){
        super("Doğrulama Kodu Yanlış!");
    }
}
