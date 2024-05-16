package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.chatGpt.ChatGptRequest;
import com.paellasoft.CRUD.chatGpt.ChatGptResponse;
import com.paellasoft.CRUD.dto.BoeDTO;
import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.BoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "BoeController", description = "Controlador para la gestión de operaciones relacionadas con el BOE")
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
    private AuthService authService;

    @Operation(summary = "Chat con OpenAI para generar contenido", description = "Utiliza OpenAI para generar respuestas de chat basadas en el indicativo proporcionado")
    @ApiResponse(responseCode = "200", description = "Respuesta exitosa de OpenAI")
    @GetMapping("/chat")
    public String chat(@Parameter(description = "Indicativo para enviar a OpenAI") @RequestParam("prompt") String prompt) {
        ChatGptRequest request = new ChatGptRequest(model, prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Operation(summary = "Obtener un resumen del BOE del día actual", description = "Obtiene un resumen del BOE del día en curso")
    @ApiResponse(responseCode = "200", description = "Resumen del BOE obtenido exitosamente")
    @Transactional
    @PostMapping("/boe/resumen")
    public String obtenerResumenBoeDelDia() {
        return boeService.obtenerBoeDelDia();
    }

    @Operation(summary = "Eliminar todos los registros de BOE", description = "Elimina todos los registros de BOE de la base de datos")
    @ApiResponse(responseCode = "200", description = "Todos los registros de BOE han sido eliminados")
    @DeleteMapping("/boe/delete/all")
    public void deleteAllBoes() {
        boeService.deleteAllBoes();
    }

    @Operation(summary = "Recibir BOEs no recibidos", description = "Obtiene una lista de BOEs no recibidos y los envía al correo del usuario")
    @ApiResponse(responseCode = "200", description = "Lista de BOEs enviada al correo del usuario")
    @GetMapping("/boe/norecibidos")
    public ResponseEntity<String> recibirBoesNorecibidos(@Parameter(description = "ID de sesión para autenticación") @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(sessionId)) {
            boeService.noRecibidos(Long.parseLong(sessionId.split(":")[1]));
            return ResponseEntity.ok("Lista de BOEs mandada a tu correo");
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Solicitar un BOE específico por fecha", description = "Solicita un BOE específico basado en la fecha proporcionada")
    @ApiResponse(responseCode = "200", description = "BOE solicitado ha sido enviado al correo del usuario")
    @GetMapping("/boe/solicitar")
    public ResponseEntity<String> solicitarBoe(@Parameter(description = "Fecha del BOE a solicitar") @RequestParam("fechaBoe") String fechaBoe, @Parameter(description = "ID de sesión para autenticación") @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(sessionId)) {
            boeService.enviarBoeSolicitado(Long.parseLong(sessionId.split(":")[1]), fechaBoe);
            return ResponseEntity.ok("Lista de BOEs mandada a tu correo");
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Obtener BOE por ID", description = "Devuelve el DTO del BOE solicitado por ID")
    @ApiResponse(responseCode = "200", description = "Se retorna el DTO del BOE", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoeDTO.class))})
    @GetMapping("/boe/dto/{id}")
    public ResponseEntity<BoeDTO> obtenerBoePorId(@Parameter(description = "ID del BOE a recuperar") @PathVariable Long id) {
        Boe boe = boeService.enviarDto(id);
        if (boe != null) {
            return ResponseEntity.ok(BoeDTO.fromEntity(boe));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
