package com.paellasoft.CRUD.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Service
public class BoeService {

    private static final String OPENAI_API_KEY = "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";


    public String obtenerBoeDelDia(String fecha) {
        String url = "https://www.boe.es/boe/dias/2024/03/15";

        // Crear cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crear solicitud HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la solicitud fue exitosa (código de estado 200)
            if (response.statusCode() == 200) {
                // Extraer el contenido HTML de la respuesta
                String htmlContent = response.body();
                // Aquí puedes procesar el contenido HTML para extraer el BOE del día
                return htmlContent;
            } else {
                // Manejar errores de solicitud HTTP
                System.out.println("Error al obtener el BOE del día: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            // Manejar excepciones de red u otros errores
            e.printStackTrace();
            return null;
        }
    }






}
