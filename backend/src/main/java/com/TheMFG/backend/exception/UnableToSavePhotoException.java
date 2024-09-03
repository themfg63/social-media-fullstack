package com.TheMFG.backend.exception;

public class UnableToSavePhotoException extends RuntimeException{
    public UnableToSavePhotoException(){
        super("Fotoğraf Kaydedilemedi!");
    }
}
