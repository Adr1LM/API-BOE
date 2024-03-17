package com.paellasoft.CRUD.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
public class BoeService {
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public String obtenerBoeDelDia() {
        // URL para obtener el BOE del día
        String url = "https://www.boe.es/boe/dias/2024/03/15/";

        // Crear cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crear solicitud HTTP GET para obtener el BOE
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la solicitud fue exitosa (código de estado 200)
            if (response.statusCode() == 200) {
                // Extraer el contenido HTML del BOE
                String boeContent = response.body();


                String htmlContent = response.body();

                // Procesar HTML para extraer texto puro
                String textoPuro = extraerTextoPuro(htmlContent);
                System.out.println(textoPuro);

                // Resumir el texto utilizando la API de OpenAI
                String resumen = resumirConOpenAI(textoPuro);
                return resumen;
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

    private String extraerTextoPuro(String htmlContent) {
        // Parsear el contenido HTML utilizando Jsoup
        Document doc = Jsoup.parse(htmlContent);

        // Extraer el texto de todas las etiquetas <p> (párrafos) y <div> (divisiones)
        //Elements elementosTexto = doc.select("p, div");

        //Elements elementosTexto = doc.select(".sumario");

        Element elementosTexto = doc.getElementById("sec661");
        String texto = elementosTexto.text();



        /*StringBuilder textoPuroBuilder = new StringBuilder();
        for (Element elemento : elementosTexto) {
            textoPuroBuilder.append(elemento.text()).append(" ");
        }

        // Retornar el texto puro extraído
        return textoPuroBuilder.toString();*/
        return texto;
    }

    private String resumirConOpenAI(String texto) {
        try {
            // Configurar la solicitud HTTP POST para la API de OpenAI
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString("{\"prompt\": \"" + texto + "\", \"max_tokens\": 50}"))
                    .build();
            System.out.println(OPENAI_API_KEY);
            System.out.println(request);

            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            if (response.statusCode() == 200) {
                // Extraer el resumen de la respuesta
                String resumen = response.body();
                // Procesar y retornar el resumen según sea necesario
                System.out.println(resumen);
                return resumen;
            } else {
                // Manejar errores de la solicitud HTTP
                System.out.println("Error al resumir el texto: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            // Manejar excepciones
            e.printStackTrace();
            return null;
        }
    }
}
