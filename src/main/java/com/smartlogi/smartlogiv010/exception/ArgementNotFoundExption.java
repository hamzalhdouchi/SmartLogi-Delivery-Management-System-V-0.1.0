package com.smartlogi.smartlogiv010.exception;

public class ArgementNotFoundExption extends RuntimeException{

    public ArgementNotFoundExption(String arrgement,String message) {
        super(message + arrgement + "Not found");
    }
}
