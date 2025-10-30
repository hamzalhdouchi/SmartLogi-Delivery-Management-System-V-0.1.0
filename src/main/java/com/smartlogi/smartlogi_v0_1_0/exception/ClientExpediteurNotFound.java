package com.smartlogi.smartlogi_v0_1_0.exception;

public class ClientExpediteurNotFound extends  RuntimeException{

    public ClientExpediteurNotFound(String id) {
        super("client expediteur has this id : "+ id +" not found");
    }
}
