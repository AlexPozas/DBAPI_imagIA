package cat.iesesteveterradas.dbapi.persistencia.taules;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.managers.RespostesManager;
import cat.iesesteveterradas.dbapi.persistencia.managers.UserManager;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Respostes {
    private static final Logger LOGGER = LoggerFactory.getLogger(cat.iesesteveterradas.dbapi.persistencia.managers.RespostesManager.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resposta_id")
    private Long id;

    

    @Column(name = "resposta", nullable = false)
    private String resposta;

    
    @ManyToOne
    @JoinColumn(name = "peticio_id")
    private Peticions peticio;



    
    public Respostes( String resposta, Peticions peticio) {
        
        this.resposta = resposta;
        this.peticio = peticio;
    }


    public Respostes(JSONObject data) {
        LOGGER.info("aqui esta1: "+data.toString().length());
        LOGGER.info("aqui esta2: "+data.toString());
        this.resposta = data.getString("resposta");
        this.peticio =RespostesManager.findPeticio(data.getInt("id")) ;
       
       
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getResposta() {
        return resposta;
    }


    public void setResposta(String resposta) {
        this.resposta = resposta;
    }


    public Peticions getPeticio() {
        return peticio;
    }


    public void setPeticio(Peticions peticio) {
        this.peticio = peticio;
    }




    @Override
    public String toString() {
        return "Respostes [id=" + id + ", resposta=" + resposta + ", peticio=" + peticio + "]";
    }
    
    




    
}
