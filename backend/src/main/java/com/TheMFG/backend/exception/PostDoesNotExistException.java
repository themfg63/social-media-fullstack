package com.TheMFG.backend.exception;

public class PostDoesNotExistException extends RuntimeException{
    public PostDoesNotExistException(){
        super("İstenilen Gönderi Mevcut Değil!");
    }
}
