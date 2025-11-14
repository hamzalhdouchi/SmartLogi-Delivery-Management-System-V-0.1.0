package com.smartlogi.smartlogiv010.exception;

public class NotFoundException extends  RuntimeException{

    public NotFoundException(String element,String id) {
        super(element + " has this id : "+ id +" not found");
    }
}
