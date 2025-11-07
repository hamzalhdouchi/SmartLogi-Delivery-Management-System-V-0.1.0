package com.smartlogi.smartlogi_v0_1_0.exception;

public class ArgementNotFoundExption extends RuntimeException{

    public ArgementNotFoundExption(String arrgement,String message) {
        super(message + arrgement + "Not found");
    }
}
