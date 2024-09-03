package com.TheMFG.backend.exception;

public class UnableToCreatePostException extends RuntimeException{
    public UnableToCreatePostException(){
        super("Gönderi Şu an da Oluşturulamıyor!");
    }
}
