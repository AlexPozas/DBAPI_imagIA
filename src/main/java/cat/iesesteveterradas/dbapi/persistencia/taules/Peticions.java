package cat.iesesteveterradas.dbapi.persistencia.taules;


import jakarta.persistence.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.managers.ModelManager;
import cat.iesesteveterradas.dbapi.persistencia.managers.UserManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Peticions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")
    private long id;

    @Column(nullable = false)
    private String prompt;

    @Column(name = "image", length = 2083)
    private String imagePath;

    @Column(name = "request_date")
    private String requestDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuaris user;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    public Peticions() {

    }

    public Peticions(JSONObject data) {
        JSONArray ja = new JSONArray(data.getJSONArray("images"));
        this.user = UserManager.findUser("admin");
        this.prompt = data.getString("prompt");
        this.model = ModelManager.findModelByName("llava");


        this.imagePath =guardarImagenDesdeBase64(ja.getString(0));

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter sqlDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.requestDate = currentDate.format(sqlDateFormat);
    }

    public Peticions(String prompt) {
        this.prompt = prompt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Usuaris getUser() {
        return user;
    }

    public void setUser(Usuaris user) {
        this.user = user;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", prompt='" + prompt + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", user=" + user +
                ", model=" + model +
                '}';
    }
    
    public static String guardarImagenDesdeBase64(String base64String) {
        try {
            // Decodificar el string base64 en un array de bytes
            byte[] imageData = Base64.getDecoder().decode(base64String);

            // Generar un nombre de archivo único
            String fileName = "imagen_" + System.currentTimeMillis() + ".png";

            // Ruta donde se guardará la imagen
            Path filePath = Paths.get("DBAPI_imagIA\\data", fileName);

            // Crear directorio si no existe
            Files.createDirectories(filePath.getParent());

            // Escribir los bytes en un archivo
            FileOutputStream fos = new FileOutputStream(filePath.toString());
            fos.write(imageData);
            fos.close();

            // Devolver la ruta del archivo guardado
            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
