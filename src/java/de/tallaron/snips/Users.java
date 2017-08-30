package de.tallaron.snips;

import de.tallaron.snips.entities.User;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Users {

    public static User login(User user) throws Exception {
        EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM User u WHERE u.name LIKE ?1 AND u.password LIKE ?2");
            query.setParameter(1, user.getName());
            query.setParameter(2, user.getPassword());
            user = (User) query.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new Exception("Error while loading user!");
        } finally {
            em.close();
        }
        user.setLoggedIn(true);
        return user;
    }


    public static User save(User user) throws Exception {
        return user.getId() == null ? create(user) : update(user);
    }

    private static User create(User user) throws Exception {
        EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new Exception("Error while creating new user!");
        } finally {
            em.close();
        }
        user.setLoggedIn(true);
        return user;
    }

    private static User update(User user) { // currently not in use
        return user;
    }


    public static boolean isValidUser(User user) {
        return (user != null && user.getId() != null && user.getId() != 0L);
    }


    
}
