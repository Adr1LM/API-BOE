package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.chatGpt.ChatGptRequest;
import com.paellasoft.CRUD.chatGpt.ChatGptResponse;
import com.paellasoft.CRUD.dto.BoeDTO;
import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.BoeService;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

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


    @DeleteMapping("/boe/delete/all")
    public void  DeleteALLBoes(){
        boeService.deleteAllBoes();
    }

    @GetMapping("/boe/norecibidos")
    public ResponseEntity<String>  recibirBoesNorecibidos (@RequestParam("userId") Long userId,
                                                           @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(userId, sessionId)){
            try {

                boeService.noRecibidos(userId);
                System.out.println("No recibidos en controller");
                return ResponseEntity.ok("Lista de BOEs mandada a tu correo");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al solicitar boes antiguos");
            }
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized access");
        }
    }



        @GetMapping("/boe/dto/{id}")
        public ResponseEntity<BoeDTO> obtenerBoePorId(@PathVariable Long id) {
            Boe boe = boeService.enviarDto(id);
            BoeDTO dto = BoeDTO.fromEntity(boe);


            if (dto != null) {
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.notFound().build();
        }



    @GetMapping("/boe/solicitar")
    public ResponseEntity<String>  solicitarBoe (@RequestParam("userId") Long userId,@RequestParam("fechaBoe") String fechaBoe,
                                                           @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(userId, sessionId)){
            try {

                boeService.enviarBoeSolicitado(userId, fechaBoe);

                return ResponseEntity.ok("Lista de BOEs mandada a tu correo");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al solicitar boes antiguos :"+e.getMessage());

            }
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized access");
        }
    }

}