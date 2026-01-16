package com.galletas.tienda.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Objects; // Para validación segura de nulos

@Service
public class TasaService {

    private final double tasaManual = 55.50;

    public double obtenerTasaActual() {
        try {
            String url = "https://ve.dolarapi.com/v1/dolares/oficial";
            RestTemplate restTemplate = new RestTemplate();

            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };

            // Mantenemos Objects.requireNonNull para eliminar la advertencia de 'Null type
            // safety'
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    url,
                    Objects.requireNonNull(HttpMethod.GET),
                    null,
                    responseType);

            Map<String, Object> response = responseEntity.getBody();

            if (response != null && response.get("promedio") != null) {
                // Convertimos de forma segura a Double para evitar errores de tipo
                return Double.parseDouble(response.get("promedio").toString());
            }
        } catch (Exception e) {
            System.err.println("Error consultando tasa BCV: " + e.getMessage());
        }
        // Si la API falla o hay error, usamos la tasa de respaldo
        return tasaManual;
    }
}