package cat.iesesteveterradas.dbapi.endpoints;

import org.json.JSONArray;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.managers.RequestManager;
import cat.iesesteveterradas.dbapi.persistencia.taules.Peticions;

import java.io.IOException;
import java.util.Map;

import static cat.iesesteveterradas.dbapi.persistencia.taules.Peticions.saveBase64Image;

@Path("/request")
public class PeticionsResource {

    @POST
    @Path("/insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertRequest(String data) {

        boolean inserted;
        Peticions request;

        JSONObject requestJson = new JSONObject(data);
        request = new Peticions(requestJson);
        request = RequestManager.insertRequest(request);

        inserted = request != null;

        if (!inserted) {
            return Response.serverError().build();
        }

        try {
            saveBase64Image(new JSONArray(requestJson.getJSONArray("images")).getString(0),"//data/image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Response.accepted().build();
    }
}