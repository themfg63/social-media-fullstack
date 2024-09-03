package com.TheMFG.backend.exception;

public class UnableToResolvePhotoException extends RuntimeException{
    public UnableToResolvePhotoException(){
        super("Aradığınız Fotoğraf Bulunamadı");
    }
}
