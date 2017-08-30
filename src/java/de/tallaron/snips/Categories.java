package de.tallaron.snips;

import de.tallaron.snips.entities.Category;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class Categories {

    public static Category syncCategory(Category c, EntityManager em) {
        try {
            Query qc = em.createQuery("SELECT c FROM Category c WHERE c.name LIKE ?1");
            qc.setParameter(1, c.getName());
            Category temp = (Category) qc.getSingleResult();
            return temp;
        } catch (Exception e) {
            em.persist(c); //TODO: Dirty?!
            return c;
        }
    }

}
