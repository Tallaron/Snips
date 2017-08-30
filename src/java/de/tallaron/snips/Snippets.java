package de.tallaron.snips;

import de.tallaron.snips.entities.Snippet;
import de.tallaron.snips.exceptions.SnipsCategoryIsNullException;
import de.tallaron.snips.exceptions.SnipsLanguageIsNullException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Snippets {
    
    public static Snippet load(Snippet snippet) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(params.keySet().contains("id")) {
            EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
            try {
                em.getTransaction().begin();
                snippet = em.find(Snippet.class, Long.valueOf(params.get("id")));
                em.getTransaction().commit();
            } catch (Exception e) {
                return null;
            } finally {
                em.close();
            }
        } return snippet;
    }
    
    
        
    public static Snippet save(Snippet snippet) 
            throws Exception, SnipsLanguageIsNullException, SnipsCategoryIsNullException {
        return (snippet.getId() == null || snippet.getId() == 0L) ? create(snippet) : update(snippet);
    }

    
    
    private static Snippet create(Snippet snippet) throws Exception {
        EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            snippet.setLanguage(Languages.syncLanguage(snippet.getLanguage(), em));
            snippet.setCategory(Categories.syncCategory(snippet.getCategory(), em));
            em.persist(snippet);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
        return snippet;
    }

    
    
    private static Snippet update(Snippet snippet) 
            throws Exception, SnipsLanguageIsNullException, SnipsCategoryIsNullException {
        EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            snippet.setLanguage(Languages.syncLanguage(snippet.getLanguage(), em));
            snippet.setCategory(Categories.syncCategory(snippet.getCategory(), em));
            em.persist(em.merge(snippet)); // merge and persist snippet to db
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
        return snippet;
    }

    
    
    public static Snippet delete(Snippet snippet) throws Exception {
        EntityManager em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(snippet));
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new Exception("Error while deleting snippet!");
        } finally {
            em.close();
        }
        return snippet;
    }
    
}
