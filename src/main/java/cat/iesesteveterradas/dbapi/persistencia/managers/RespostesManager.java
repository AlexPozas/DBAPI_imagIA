package cat.iesesteveterradas.dbapi.persistencia.managers;


import cat.iesesteveterradas.dbapi.persistencia.taules.Peticions;
import cat.iesesteveterradas.dbapi.persistencia.taules.Respostes;
import cat.iesesteveterradas.dbapi.persistencia.taules.Usuaris;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class RespostesManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(cat.iesesteveterradas.dbapi.persistencia.managers.RespostesManager.class);

    /**
     * Tries to find an existing user with 'nickname' String parameter.
     * If not found creates it.
     *
     * @param request request to insert
     * @return Request
     */
    public static Respostes insertRespostes(Respostes request) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(request);
            tx.commit();
            LOGGER.info("New answer inserted from request '{}'", request.getPeticio().getId());
            return request;

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            ;
            LOGGER.error("Error trying to insert answer from request  '{}'", request.getPeticio().getId(), e);
        } finally {
            session.close();
        }

        return null;
    }



    public static Peticions findPeticio(long id) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Peticions user = null;

        try {
            tx = session.beginTransaction();
            Query<Peticions> query = session.createQuery("FROM Peticions WHERE id = :id", Peticions.class);
            query.setParameter("id", id);
            user = query.uniqueResult();

            if (user == null) {
                user = new Peticions(id);
                session.merge(user);
                tx.commit();
            }

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();;
            LOGGER.error("Error trying to find user with nickname '{}'", id, e);
        } finally {
            session.close();
        }

        return user;
    }

    

    /**
     *
     * @param images JSONArray of images to store
     * @param request Request entity
     */



}
