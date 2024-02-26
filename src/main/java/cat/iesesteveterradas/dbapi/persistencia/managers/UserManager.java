package cat.iesesteveterradas.dbapi.persistencia.managers;


import cat.iesesteveterradas.dbapi.persistencia.taules.Usuaris;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class  UserManager {

    private UserManager() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

    /**
     * Tries to find an existing user with 'nickname' String parameter.
     * If not found creates it.
     *
     * @param nickname user nickname
     * @return found or created user
     */
    public static Usuaris findUser(String nickname) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris user = null;

        try {
            tx = session.beginTransaction();
            Query<Usuaris> query = session.createQuery("FROM User WHERE nickname = :nickname", Usuaris.class);
            query.setParameter("nickname", nickname);
            user = query.uniqueResult();

            if (user == null) {
                user = new Usuaris(nickname);
                session.merge(user);
                tx.commit();
            }

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();;
            LOGGER.error("Error trying to find user with nickname '{}'", nickname, e);
        } finally {
            session.close();
        }

        return user;
    }

    /**
     * Tries to find an existing user.
     * If not found creates it.
     *
     * @param user user to find
     * @return user found
     */
    public static Usuaris findUser(Usuaris user) {
        final String telephone = user.getTelephone();
        Transaction tx = null;

        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            Usuaris foundUser;
            tx = session.beginTransaction();
            Query<Usuaris> query = session.createQuery("FROM User WHERE telephone = :telephone", Usuaris.class);
            query.setParameter("telephone", telephone);
            foundUser = query.uniqueResult();

            if (foundUser == null) {
                session.merge(user);
                tx.commit();

            } else {
                return null;

            }

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Error finding user", e);

        }

        return user;
    }

    /**
     * Finds a user by its registered phone number for validation purposes
     *
     * @param telephone received telephone
     * @return found user
     */
    public static Usuaris findUserByTelephone(String telephone) {
        Transaction tx = null;
        Usuaris user = null;

        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<Usuaris> query = session.createQuery("FROM User WHERE telephone = :telephone", Usuaris.class);
            query.setParameter("telephone", telephone);
            user = query.uniqueResult();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Error finding user", e);
        }

        return user;
    }

    /**
     * Checks if a user exists
     *
     * @param user user to check
     * @return true if exists, else false
     */
    public static boolean userExists(Usuaris user) {
        Transaction tx = null;
        boolean exists = false;

        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            Usuaris foundUser;
            tx = session.beginTransaction();
            Query<Usuaris> query = session.createQuery("FROM Usuaris WHERE telephone = :telephone", Usuaris.class);
            query.setParameter("telephone", user.getTelephone());
            foundUser = query.uniqueResult();

            if (foundUser != null) {
                exists = true;
            }

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();

        }

        return exists;
    }

    public static void updateUser(Usuaris user) {
        Transaction tx = null;
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        try  {
            tx = session.beginTransaction();
            session.merge(user);


        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Error updating user", e);
        }

        finally {
            session.close();
        }
    }



}
