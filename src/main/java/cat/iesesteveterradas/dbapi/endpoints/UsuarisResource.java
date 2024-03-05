package cat.iesesteveterradas.dbapi.endpoints;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.managers.CommonManager;
import cat.iesesteveterradas.dbapi.persistencia.managers.UserManager;
import cat.iesesteveterradas.dbapi.persistencia.taules.Usuaris;


@Path("/user")
public class UsuarisResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarisResource.class);
    private static final String ACCESS_KEY = "access_key";
    private static final String NICKNAME = "nickname";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String EMAIL = "email";

    /**
     * Endpoint for user registration
     *
     * @param data JSON containing new user data
     * @return response with registration status
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String data) {
        LOGGER.info("User registration request received");

        Usuaris user;
        JSONObject responseData = new JSONObject();
        JSONObject requestJson = new JSONObject(data);
        user = new Usuaris(requestJson);

        //if (UserManager.findUser(user) == null) {
           // LOGGER.info("User already exists");

           // responseData.put(NICKNAME, user.getNickname())
                 //   .put(PHONE_NUMBER, user.getTelephone())
                   // .put(EMAIL, user.getEmail());

           // return CommonManager.buildResponse(
                  //  Response.Status.CONFLICT,
                   // responseData,
                // "User already exists");
        //}
        CommonManager.insertRequest(user);
        LOGGER.info("User successfully registered");


        return CommonManager.buildResponse(
                Response.Status.OK,
                responseData,
                "User successfully registered");
    }

    /**
     * Endpoint for user validation
     *
     * @param data request body
     * @return server response
     */
    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUser(String data) {
        LOGGER.info("Received new user validation request");

        JSONObject responseData = new JSONObject();
        JSONObject requestJson = new JSONObject(data);
        Usuaris user = UserManager.findUserByTelephone(requestJson.getString(PHONE_NUMBER));

        if (user == null) {
            return CommonManager.buildResponse(
                    Response.Status.BAD_REQUEST,
                    responseData,
                    "the phone number is not registered");
        }

        LOGGER.info("User validated");
    


        responseData.put(ACCESS_KEY, user.getAccessKey())
                .put(NICKNAME, user.getNickname())
                .put(PHONE_NUMBER, user.getTelephone())
                .put(EMAIL, user.getEmail());

        return CommonManager.buildResponse(
                Response.Status.OK,
                responseData,
                "User successfully validated");
    }
}