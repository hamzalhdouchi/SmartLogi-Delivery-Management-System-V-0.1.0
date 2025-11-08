package com.smartlogi.smartlogi_v0_1_0.exception;

public class NotFoundException extends  RuntimeException{

    public NotFoundException(String element,String id) {
        super(element + " has this id : "+ id +" not found");
    }
}
