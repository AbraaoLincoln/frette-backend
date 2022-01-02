package br.com.fretee.freteebackend.configuration;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmUtil {
    private String secret = "secret";

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }
}
