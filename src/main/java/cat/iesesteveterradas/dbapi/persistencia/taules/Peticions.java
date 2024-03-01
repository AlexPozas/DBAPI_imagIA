package cat.iesesteveterradas.dbapi.persistencia.taules;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import jakarta.persistence.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

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
        this.user = UserManager.findUser(data.getString("user"));
        this.prompt = data.getString("prompt");
        this.model = ModelManager.findModelByName("llava");


        try {
            this.imagePath =saveBase64Image(ja.getString(0),Paths.get(System.getProperty("user.dir")).toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
    
    public static String saveBase64Image(String base64Image, String relativePath) throws IOException {
        String destinationPath = "data/imagen/" + relativePath; // Construye la ruta completa

        // Verificar si la carpeta de destino existe
        Path destFolderPath = Paths.get(destinationPath);
        if (!Files.exists(destFolderPath)) {
            Files.createDirectories(destFolderPath);
        }

        // Listar archivos en el directorio destino
        File[] files = new File(destinationPath).listFiles();
        int nextImageNumber = 1;
        if (files != null) {
            // Encontrar el siguiente número de imagen disponible
            nextImageNumber = Arrays.stream(files)
                                    .filter(File::isFile)
                                    .map(File::getName)
                                    .filter(name -> name.matches("\\d+\\.jpg"))
                                    .map(name -> Integer.parseInt(name.split("\\.")[0]))
                                    .max(Integer::compareTo)
                                    .orElse(0) + 1;
        }

        // Construir el nombre del archivo de imagen
        String fileName =  nextImageNumber+ ".jpg";
        Path imagePath = Paths.get(destinationPath, fileName);

        // Decodificar el base64 y escribir la imagen en el archivo
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
            fos.write(imageBytes);
        }

        // Devolver el path relativo del archivo de imagen guardado
        return Paths.get("data/imagen", relativePath, fileName).toString();
    }

    
}
