package com.cipherforge.core;

public abstract class CipherAlgorithm {
    protected String name;
    
    public CipherAlgorithm(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract CipherResult encrypt(String plaintext, String key);
}
