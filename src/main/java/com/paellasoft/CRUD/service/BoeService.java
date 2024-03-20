package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.chatGpt.ChatGptRequest;
import com.paellasoft.CRUD.chatGpt.ChatGptResponse;
import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.entity.BoeUser;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.mail.EmailSender;
import com.paellasoft.CRUD.repository.IBoeRepository;
import com.paellasoft.CRUD.repository.IBoeUser;
import com.paellasoft.CRUD.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Component
@Transactional
public class BoeService {
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    @Autowired
    private RestTemplate template;

    @Autowired
    private IBoeRepository boeRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBoeUser boeUserRepo;
    @Autowired
    private EmailSender emailSender;


    @Scheduled(cron = "0 * * * * *")
    public String obtenerBoeDelDia() {

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Formatear la fecha en el formato esperado por la URL del BOE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String fechaFormateada = fechaActual.format(formatter);
        // Construir la URL del BOE del día actual
        String url = "https://www.boe.es/boe/dias/" + fechaFormateada + "/index.php?s=1";

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
                String resumen = resumirConChatGpt(textoPuro);

                String fragmentoTexoOriginal = textoPuro.substring(5, 40);

                fragmentoTexoOriginal = "hola es una prueba 5: robocop";

                String fragmentoTexoResumen = resumen.substring(5, 40);

                System.out.println(fragmentoTexoOriginal);
                System.out.println(fragmentoTexoResumen);
                System.out.println(fechaActual);
                System.out.println(fechaFormateada);

                //Fecha y hora para registro del Boe
                DateTimeFormatter formateoRegistro = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
                LocalDateTime fechaRegistro = LocalDateTime.now();
                String  fechaBoe =  fechaRegistro.format(formateoRegistro);

                fechaActual.format(formatter);
                System.out.println(fechaBoe);

                // Crear el objeto Boe
                Boe boe = new Boe();
                boe.setContenidoOriginal(fragmentoTexoOriginal);
                boe.setContenidoResumido(fragmentoTexoResumen);
                boe.setFechaBoe(fechaBoe);

                //-------comprobar si ya esta registrado y guardarlo
                comprobarCambiosEnBoe(boe);
                return resumen;


            } else {
                // Manejar errores de solicitud HTTP
                System.out.println("Error al obtener el BOE del día: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }




    public void comprobarCambiosEnBoe(Boe boe) {
        //Ultimo Boe registrado
        Boe ultimoBoe = boeRepository.findTopByOrderByFechaBoeDesc();
        //comprobar contenido con el obtenido ahora
        if(boe.getContenidoOriginal().equals(ultimoBoe.getContenidoOriginal())){
            System.out.println("Este boe ya esta registrado");
        }else{

            boeRepository.save(boe);
            // Obtener la lista de usuarios suscritos
            List<BoeUser> suscriptores = boeUserRepo.findByBoe(ultimoBoe);


            if (suscriptores.isEmpty()) {
                System.out.println("No hay suscriptores para el último Boletín Oficial.");
            } else {
                System.out.println("Suscriptores para el último Boletín Oficial:");
                for (BoeUser suscriptor : suscriptores) {
                    System.out.println("Usuario: " + suscriptor.getUser().getUsername() + ", Correo: " + suscriptor.getUser().getEmail());
                }
            }

            //Enviar mail de notificacion
            for (BoeUser suscriptor : suscriptores) {
                User usuario = suscriptor.getUser();
                String to = usuario.getEmail();
                String subject = "Nuevo Boletín Oficial disponible";
                String text = "Estimado " + usuario.getUsername() + ",\n\nSe ha detectado un nuevo Boletín Oficial. Puedes revisarlo en el sitio web.";
                emailSender.sendEmail(to, subject, text);
            }



        }

    }

    private String extraerTextoPuro(String htmlContent) {
        // Parsear el contenido HTML utilizando Jsoup
        Document doc = Jsoup.parse(htmlContent);

        // Extraer el texto de todas las etiquetas <p> (párrafos) y <div> (divisiones)
        Element elementosTexto = doc.selectFirst("div.sumario");

        //Elements elementosTexto = doc.select(".sumario");

        // Element elementosTexto = doc.getElementById("sec661");
        String texto = elementosTexto.text();

        // Limitar la cantidad de texto extraído
        int maxTokens = 16385; // Establecer el límite máximo de tokens permitidos
        if (texto.length() > maxTokens) {
            texto = texto.substring(0, maxTokens);    }

        return texto;
    }




    private String resumirConChatGpt(String texto) {
        try {
            // Crear la solicitud a la API de OpenAI
            ChatGptRequest request = new ChatGptRequest(model, "Resume a la mitad lo destacable: "+ texto);

            // Realizar la solicitud a la API de OpenAI
            ChatGptResponse response = template.postForObject(apiUrl, request, ChatGptResponse.class);

            // Extraer el resumen del texto de la respuesta
            String resumen = response.getChoices().get(0).getMessage().getContent();

            // Retornar el resumen
            return resumen;
        } catch (Exception e) {
            // Manejar excepciones
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public void suscribirUsuario(Long userId, Long boeId) {
        // Obtener el usuario y el boletín oficial correspondientes
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Boe> optionalBoe = boeRepository.findById(boeId);

        // Verificar si el usuario y el boletín oficial existen
        if (optionalUser.isPresent() && optionalBoe.isPresent()) {
            User user = optionalUser.get();
            Boe boe = optionalBoe.get();

            // Verificar si el usuario ya está suscrito al boletín oficial
            List<Boe> userSubscriptions = user.getSubscribedBoes();
            if (userSubscriptions.contains(boe)) {
                throw new RuntimeException("El usuario ya está suscrito a este Boletín Oficial.");
            }

            // Crear la suscripción del usuario al boletín oficial
            BoeUser boeUser = new BoeUser();
            boeUser.setUser(user);
            boeUser.setBoe(boe);

            // Guardar la suscripción en la base de datos
            boeUserRepo.save(boeUser);
        } else {
            throw new RuntimeException("El usuario o el Boletín Oficial especificados no existen.");
        }
    }






}
