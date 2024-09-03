package com.TheMFG.backend.exception;

public class EmailFaildToSendException extends RuntimeException{
    public EmailFaildToSendException(){
        super("Email Gönderilirken Bir Hata Oluştu!");
    }
}
