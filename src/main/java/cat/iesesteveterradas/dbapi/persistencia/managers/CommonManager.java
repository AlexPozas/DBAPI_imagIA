package cat.iesesteveterradas.dbapi.persistencia.managers;


import cat.iesesteveterradas.dbapi.endpoints.UsuarisResource;
import cat.iesesteveterradas.dbapi.persistencia.taules.Peticions;
import cat.iesesteveterradas.dbapi.persistencia.taules.Usuaris;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CommonManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarisResource.class);
    private CommonManager() {

    }

    /**
     * Generates an AccessKey for desired user
     *
     * @param user user to generate access key into
     * @return access key
     */
    public static String generateAccessKey(Usuaris user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 12);

        Date currentDate = new Date();
        Date expirationDate = calendar.getTime();

        Algorithm algorithm = Algorithm.HMAC256(user.getTelephone());

        return JWT.create()
                .withIssuedAt(currentDate)
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    /**
     * Verifies the received access key
     *
     * @param user user that sent the key
     * @return decoded key
     * @throws JWTVerificationException if not valid throws exception
     */
    public static DecodedJWT verifyAccessKey(Usuaris user) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(user.getTelephone());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        return verifier.verify(user.getAccessKey());
    }

    /**
     * Returns an OK response
     *
     * @param data custom response data
     * @param status response status
     * @param message response message
     * @return built response
     */
    public static Response buildResponse(Response.Status status, JSONObject data, String message) {
        JSONObject responseBody = buildDefaultResponseBody(status.toString(), message);
        responseBody.put("data", data);

        return Response.status(status)
                .entity(responseBody.toString())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();

    }

    public static Usuaris insertRequest(Usuaris user) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            LOGGER.info("New user inserted: "+user.getNickname());
            return user;

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            ;
            LOGGER.error("Error trying to insert request from user  '{}'", user.getNickname(), e);
        } finally {
            session.close();
        }

        return null;
    }

    /**
     * Returns the default response body
     *
     * @param status response status
     * @param message response message
     * @return default response body
     */
    private static JSONObject buildDefaultResponseBody(String status, String message) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", status)
                .put("MESSAGE", message);

        return responseJSON;
    }


}