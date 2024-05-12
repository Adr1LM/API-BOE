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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Autowired
    private UserService userService;


    private LocalDate fechaActual = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private String fechaFormateada = fechaActual.format(formatter);

    @Scheduled(cron = "0 * * * * *")
    public String obtenerBoeDelDia() {
        // Construir la URL del BOE del día actual
        String urlSeccion1 = "https://www.boe.es/boe/dias/" + fechaFormateada + "/index.php?s=1";
        String urlSeccion3 = "https://www.boe.es/boe/dias/" + fechaFormateada + "/index.php?s=3";

        // Crear cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Strings para almacenar el contenido HTML de las secciones 1 y 3
        String boeContentSeccion1 = null;
        String boeContentSeccion3 = null;

        try {
            // Enviar solicitud y obtener respuesta para la sección 1
            HttpResponse<String> responseSeccion1 = client.send(
                    HttpRequest.newBuilder().uri(URI.create(urlSeccion1)).build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            // Enviar solicitud y obtener respuesta para la sección 3
            HttpResponse<String> responseSeccion3 = client.send(
                    HttpRequest.newBuilder().uri(URI.create(urlSeccion3)).build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            // Verificar si la solicitud fue exitosa y tiene contenido para la sección 1
            if (responseSeccion1.statusCode() == 200 && responseSeccion1.body() != null) {
                boeContentSeccion1 = responseSeccion1.body();
            }

            // Verificar si la solicitud fue exitosa y tiene contenido para la sección 3
            if (responseSeccion3.statusCode() == 200 && responseSeccion3.body() != null) {
                boeContentSeccion3 = responseSeccion3.body();
            }

            // Procesar HTML para extraer texto puro de las secciones 1 y 3
            String textoPuroSeccion1 = (boeContentSeccion1 != null) ? extraerTextoPuro(boeContentSeccion1) : "No hay Seccion Primera.";
            String textoPuroSeccion3 = (boeContentSeccion3 != null) ? extraerTextoPuro(boeContentSeccion3) : "No hay Seccion Tercera.";

            // Combinar el texto puro de las secciones 1 y 3
            String textoPuroCompleto = textoPuroSeccion1 + "\n\n" + textoPuroSeccion3;

            System.out.println(textoPuroCompleto);

            //-------comprobar si ya esta registrado el Boe del día
            comprobarCambiosEnBoe(textoPuroCompleto);

            return textoPuroCompleto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void registrarBoeUser(Boe boe, List<User> usuarios) {
        // Crear y guardar un objeto BoeUser para cada usuario con sendNotification activo
        for (User usuario : usuarios) {
            // Verificar si el usuario tiene sendNotification activo
            if (usuario.isSendNotification()) {
                BoeUser boeUser = new BoeUser();
                boeUser.setBoe(boe);
                boeUser.setUser(usuario);
                boeUserRepo.save(boeUser);
            }
        }
    }


    public void comprobarCambiosEnBoe(String textoPuro) {

        System.out.println(textoPuro);
        String fragmentoTextoOriginal = textoPuro.substring(8, 21);

        System.out.println("Fragmento original de comprobarCambios: " + fragmentoTextoOriginal);

        String trampa = "trampa4"; //trampa para que al comprobar el ultimo boe, sea diferente
        // fragmentoTextoOriginal=trampa;

        Boe ultimoBoe = boeRepository.findTopByOrderByIdDesc();

        //System.out.println("Fragmento original del ultimo boe registrado: "+ ultimoBoe.getTituloBoe());


        if (ultimoBoe == null) {
            registrarBoe(textoPuro);
            System.out.println("ultimo boe es null");
        } else {
            //comprobar contenido del ultimo boe guardado con el obtenido ahora
            if (fragmentoTextoOriginal.equals(ultimoBoe.getTituloBoe())) {
                System.out.println("Este boe ya esta registrado");
            } else {

                registrarBoe(textoPuro);
            }
        }
    }


    private void registrarBoe(String textoPuro) {

        try {
            String resumen = resumirConChatGpt(textoPuro);
            String fragmentoTextoOriginal = textoPuro.substring(8, 21);


            System.out.println(fragmentoTextoOriginal);

            String fragmentoResumen = resumen.substring(5, 40);
            //Fecha y hora para registro del Boe
            DateTimeFormatter formateoRegistro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime fecha = LocalDateTime.now();
            String fechaRegistro = fecha.format(formateoRegistro);


            System.out.println(fragmentoResumen);
            System.out.println(fechaRegistro);


            String trampa = "trampa4";

            //fragmentoTextoOriginal=trampa;//trampa para que al comprobar el ultimo boe, sea diferente

            // Crear el objeto Boe
            Boe boe = new Boe();
            boe.setTituloBoe(fragmentoTextoOriginal);
            boe.setContenidoResumido(resumen);
            boe.setFechaBoe(fechaRegistro);

            boeRepository.save(boe);

            notificarSubscriptores(resumen);
            registrarBoeUser(boe, userRepository.findAll());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void notificarSubscriptores(String resumen) {

        List<User> usuarios = userRepository.findAll();
        for (User usuario : usuarios) {
            if (usuario.isSendNotification()) {
                String to = usuario.getEmail();
                String subject = "Nuevo Boletín Oficial disponible";
                String text = "Estimado " + usuario.getUsername() + ",\n\n Para Leer el Boe de hoy en profundidad, acceda a la pagina web:\nhttps://www.boe.es/boe/dias/" + fechaFormateada + "\n\n" + resumen;
                String signatureImagePath = "src/main/resources/boe.png";
                emailSender.sendEmailWithPdfAttachment(to, subject, text, signatureImagePath);

                System.out.println("Correo enviado a: " + usuario.getEmail());
            }
        }
    }

    private String extraerTextoPuro(String htmlContent) {
        // Parsear el contenido HTML utilizando Jsoup
        Document doc = Jsoup.parse(htmlContent);

        // Extraer el texto de todas las etiquetas <p> (párrafos) y <div> (divisiones)
        Element elementosTexto = doc.selectFirst("div.sumario");
        Element codigoBoe = doc.selectFirst("div.linkSumario");
        // Element elementosTexto = doc.getElementById("sec661");
        String texto = codigoBoe.text() + elementosTexto.text();
        System.out.println(texto);

        // Limitar la cantidad de texto extraído
        int maxTokens = 16385; // Establecer el límite máximo de tokens permitidos
        if (texto.length() > maxTokens) {
            texto = texto.substring(0, maxTokens);
        }
        return texto;
    }


    private String resumirConChatGpt(String texto) {
        try {
            String prompt = "Resume por apartados manteniendo una estructura (manteniendo la " +
                    "division entre 1.Disposiciones Generales y 3.Otras Disposiciones) y hazlo lo mas largo que puedas ";


            // Crear la solicitud a la API de OpenAI
            ChatGptRequest request = new ChatGptRequest(model, prompt + texto);

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

    public void deleteAllBoes() {

        boeRepository.deleteAll();
    }


    public void noRecibidos(Long userId) {
        //Metodo que implementa @Query de JPA
        List<Boe> boesNoRecibidos = boeUserRepo.findNotReceivedBoesByUserId(userId);

        //Metodo que implementa nuestra interfaz custom
        //List<Boe> boesNoRecibidos = boeUserRepo.customNoRecibidos(userId);

        System.out.println("Test para noRecibidos de BoeService\n" + boesNoRecibidos.toString());

        StringBuilder boesAntiguos = new StringBuilder();
        if (!boesNoRecibidos.isEmpty()) {
            for (Boe boe : boesNoRecibidos) {
                boesAntiguos.append(boe.toString());

            }
            User usuario = userService.getUserById(userId);


            String to = usuario.getEmail();
            String subject = "BOEs NO recibidos:";
            String text = "Estimado " + usuario.getUsername() + ",\n\n Aquí tienes tus BOEs no recibidos resumidos\n\n" + boesAntiguos;
            String signatureImagePath = "src/main/resources/boe.png";
            emailSender.sendEmailWithPdfAttachment(to, subject, text, signatureImagePath);


        }


    }

    public void enviarBoeSolicitado(Long userId, String fechaBoe) {

        System.out.println(fechaBoe);

        Boe boe = boeRepository.findByFechaBoe(fechaBoe);

        User usuario = userService.getUserById(userId);

        registrarBoeUser(boe, userRepository.findAll());

        // Envío de correo electrónico de confirmación

        String to = usuario.getEmail();
        String subject = "Aquí tienes tu BOE solicitado:";
        String text = "Estimado " + usuario.getUsername() + ",\n\n Para Leer el Boe de ese día, acceda a la pagina web:\nhttps://www.boe.es/boe/dias/" + String.format(fechaBoe, formatter) + "\n\n" + boe.toString();
        String signatureImagePath = "src/main/resources/boe.png";
        emailSender.sendEmailWithPdfAttachment(to, subject, text, signatureImagePath);

       /* String to = usuario.getEmail();
        String subject = "Tu Boe solicitado.";
        String text = "Hola " + usuario.getUsername() + ", tu boe solicitado:\n " + boe.toString();
        emailSender.sendEmail(to, subject, text);*/

    }

    public Boe enviarDto(Long id) {

        Boe boe = boeRepository.getById(id);
        return boe;



    }
}
