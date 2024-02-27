package cat.iesesteveterradas.dbapi.persistencia.managers;


import cat.iesesteveterradas.dbapi.persistencia.taules.Peticions;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class RequestManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(cat.iesesteveterradas.dbapi.persistencia.managers.RequestManager.class);

    /**
     * Tries to find an existing user with 'nickname' String parameter.
     * If not found creates it.
     *
     * @param request request to insert
     * @return Request
     */
    public static Peticions insertRequest(Peticions request) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(request);
            tx.commit();
            LOGGER.info("New request inserted from user '{}'", request.getUser().getNickname());
            return request;

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            ;
            LOGGER.error("Error trying to insert request from user  '{}'", request.getUser().getNickname(), e);
        } finally {
            session.close();
        }

        return null;
    }

    public static boolean updateRequest(Peticions request) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.merge(request);
            tx.commit();
            LOGGER.info("Updated image_path colum from request '{}'", request.getId());
            return true;

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Error trying to update image_path column from request '{}'", request.getId());

        } finally {
            session.close();
        }

        return false;
    }

    /**
     *
     * @param images JSONArray of images to store
     * @param request Request entity
     */



}
