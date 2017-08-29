/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tallaron.snips;

import de.tallaron.snips.entities.Category;
import de.tallaron.snips.entities.Language;
import de.tallaron.snips.entities.Snippet;
import de.tallaron.snips.entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
public class Controller {

    private User user;
    private Map userNav = null;
    private Snippet snippet;
    private EntityManager em;
    private Exception ex = null;

    public Controller() {
        user = new User();
        snippet = new Snippet();
    }

    // PAGES
    public String home() {
        return "index";
    }
    
    

    // CALLS
    public String showRegForm() {
        user = new User();
        return "register";
    }

    public String newSnippet() {
        snippet = new Snippet();
        return "edit_snippet";
    }

    public String editSnippet() {
        return "edit_snippet";
    }

    public void loadSnippet() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(params.keySet().contains("id")) {
            em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
            try {
                em.getTransaction().begin();
                snippet = em.find(Snippet.class, Long.valueOf(params.get("id")));
                em.getTransaction().commit();
            } catch (Exception e) {
                ex = e;
            } finally {
                em.close();
            }
        }
    }

    public String login() {
        em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM User u WHERE u.name LIKE ?1 AND u.password LIKE ?2");
            query.setParameter(1, user.getName());
            query.setParameter(2, user.getPassword());
            user = (User) query.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception ex) {
            this.ex = ex;
            return "error";
        } finally {
            em.close();
        }
        prepareNav();
        user.setLoggedIn(true);
        return "home";
    }

    public String logout() {
        user = new User();
        return "home";
    }

    private void prepareNav() {
        Map<Language, Map<Category, List<Snippet>>> nav = new HashMap<>();
        for(Snippet s : user.getSnippets()) {
            if (!nav.containsKey(s.getLanguage())) // create language 'key' if not exist
                nav.put(s.getLanguage(), new HashMap<>());
            
            if (!nav.get(s.getLanguage()).containsKey(s.getCategory())) // create category 'key' if not exist
                nav.get(s.getLanguage()).put(s.getCategory(), new ArrayList<>());

            nav.get(s.getLanguage()).get(s.getCategory()).add(s);
        }
        setUserNav(nav);
    }

    
    
    // USER DB MANIPULATION
    public String saveUser() {
        em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        if (user.getId() == null) {
            return createUser();
        } else {
            return updateUser();
        }
    }

    public String createUser() {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            user.setLoggedIn(true);
            return "home";
        } catch (Exception ex) {
            this.ex = ex;
            return "error";
        } finally {
            em.close();
        }
    }

    public String updateUser() {
        return "home";
    }

    
    
    // SNIPPET DB MANIPULATION
    public String saveSnippet() {
        if (!syncUser()) return "error";
        em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        if (snippet.getId() == null || snippet.getId() == 0L) {
            return createSnippet();
        } else {
            return updateSnippet();
        }
    }

    public String createSnippet() {
        try {
            em.getTransaction().begin();
            syncCategory();
            syncLanguage();
            em.persist(snippet);
            user.addSnippet(snippet);
            em.getTransaction().commit();
        } catch (Exception ex) {
            this.ex = ex;
            return "error";
        } finally {
            em.close();
        }
        prepareNav();
        return "show_snippet";
    }

    public String updateSnippet() {
        try {
            em.getTransaction().begin();
            syncCategory();
            syncLanguage();
            em.persist(em.merge(snippet));
            em.getTransaction().commit();
        } catch (Exception ex) {
            this.ex = ex;
            return "error";
        } finally {
            em.close();
        }
        prepareNav();
        return "show_snippet";
    }

    private void syncCategory() {
        try {
            Query qc = em.createQuery("SELECT c FROM Category c WHERE c.name LIKE ?1");
            qc.setParameter(1, snippet.getCategory().getName());
            Category c = (Category) qc.getSingleResult();
            if (c != null) {
                snippet.setCategory(c);
            }
        } catch (Exception e) {
            em.persist(snippet.getCategory()); //TODO: Dirty?!
        }
    }

    private void syncLanguage() {
        try {
            Query ql = em.createQuery("SELECT l FROM Language l WHERE l.name LIKE ?1");
            ql.setParameter(1, snippet.getLanguage().getName());
            Language l = (Language) ql.getSingleResult();
            if (l != null) {
                snippet.setLanguage(l);
            }
        } catch (Exception e) {
            em.persist(snippet.getLanguage()); //TODO: Dirty?!
        }
    }

    private boolean syncUser() {
        if (user != null && user.getId() != null && user.getId() != 0L) {
            snippet.setUser(user);
            return true;
        } else {
            ex = new Exception("Invalid User");
            return false;
        }
    }

    public String deleteSnippet() {
        em = Persistence.createEntityManagerFactory("snipsPU").createEntityManager();
        try {
            em.getTransaction().begin();
            user.removeSnippet(snippet); // no reload necessary
            em.remove(em.merge(snippet));
            em.getTransaction().commit();
        } catch (Exception e) {
            ex = e;
            return "error";
        } finally {
            em.close();
        }
        return "home";
    }

    
    
    // HELPER
    public int countSnippetsInLanguage(Map<Category, List<Snippet>> m) {
        int sum = 0;
        for (Map.Entry<Category, List<Snippet>> s : m.entrySet()) {
            sum += s.getValue().size();
        }
        return sum;
    }
    
    
    
    // GETTER & SETTER
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public Map getUserNav() {
        return userNav;
    }

    public void setUserNav(Map userNav) {
        this.userNav = userNav;
    }

}
