package com.TheMFG.backend.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){
        super("Kullanıcı Adı veya Şifre Yanlış!");
    }
}
