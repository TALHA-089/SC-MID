package com.cipherforge.core;

public class CipherResult {
    private String ciphertext;
    private String algorithm;
    private String key;
    private boolean success;
    
    public CipherResult(String ciphertext, String algorithm, String key, boolean success) {
        this.ciphertext = ciphertext;
        this.algorithm = algorithm;
        this.key = key;
        this.success = success;
    }
    
    public String getCiphertext() {
        return ciphertext;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
    
    public String getKey() {
        return key;
    }
    
    public boolean isSuccess() {
        return success;
    }
}
