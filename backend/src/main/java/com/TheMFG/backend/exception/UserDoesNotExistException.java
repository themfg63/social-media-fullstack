package com.TheMFG.backend.exception;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(){
        super("Aradığınız Kullanıcı Mevut Değil!");
    }
}
