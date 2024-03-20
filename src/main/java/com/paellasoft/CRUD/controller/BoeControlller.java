package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.chatGpt.ChatGptRequest;
import com.paellasoft.CRUD.chatGpt.ChatGptResponse;
import com.paellasoft.CRUD.service.BoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class BoeControlller {

    @Autowired
    private BoeService boeService;
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {

        ChatGptRequest request = new ChatGptRequest(model, prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Transactional
    @PostMapping("/boe/resumen")
    public String obtenerResumenBoeDelDia() {
        return boeService.obtenerBoeDelDia();
    }



    @PostMapping("/boe/suscribir")
    public ResponseEntity<String> suscribirUsuario(@RequestParam("userId") Long userId, @RequestParam("boeId") Long boeId) {
        try {
            boeService.suscribirUsuario(userId, boeId);
            return ResponseEntity.ok("El usuario se ha suscrito correctamente al Boletín Oficial del Estado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al suscribir al usuario al Boletín Oficial del Estado.");
        }
    }


}