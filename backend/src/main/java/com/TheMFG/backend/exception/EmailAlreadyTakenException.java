package com.TheMFG.backend.exception;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(){
        super("Girilen Email Adresi Kullanımda!");
    }
}
