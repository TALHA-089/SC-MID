package com.cipherforge.algorithms;

import com.cipherforge.core.CipherAlgorithm;
import com.cipherforge.core.CipherResult;

public class CaesarCipher extends CipherAlgorithm {
    
    public CaesarCipher() {
        super("Caesar Cipher");
    }
    
    @Override
    public CipherResult encrypt(String plaintext, String key) {
        try {
            int shift = Integer.parseInt(key);
            StringBuilder result = new StringBuilder();
            
            for (int i = 0; i < plaintext.length(); i++) {
                char ch = plaintext.charAt(i);
                
                if (Character.isUpperCase(ch)) {
                    int shifted = ((ch - 'A' + shift) % 26);
                    if (shifted < 0) {
                        shifted += 26;
                    }
                    result.append((char) ('A' + shifted));
                } else if (Character.isLowerCase(ch)) {
                    int shifted = ((ch - 'a' + shift) % 26);
                    if (shifted < 0) {
                        shifted += 26;
                    }
                    result.append((char) ('a' + shifted));
                } else {
                    result.append(ch);
                }
            }
            
            return new CipherResult(result.toString(), name, key, true);
        } catch (NumberFormatException e) {
            return new CipherResult("", name, key, false);
        }
    }
}
