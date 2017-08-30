package de.tallaron.snips;

import de.tallaron.snips.entities.Language;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class Languages {

    public static Language syncLanguage(Language ln, EntityManager em) {
        try {
            Query ql = em.createQuery("SELECT l FROM Language l WHERE l.name LIKE ?1");
            ql.setParameter(1, ln.getName());
            Language temp = (Language) ql.getSingleResult();
            return temp;
        } catch (Exception e) {
            em.persist(ln); //TODO: Dirty?!
            return ln;
        }
    }

}
