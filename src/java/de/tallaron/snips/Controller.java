package de.tallaron.snips;

import de.tallaron.snips.entities.Category;
import de.tallaron.snips.entities.Language;
import de.tallaron.snips.entities.Snippet;
import de.tallaron.snips.entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class Controller {

    private User user;
    private Map userNav = null;
    private Snippet snippet;
    private Exception ex = null;
    
    public Controller() {
        user = new User();
        snippet = new Snippet();
        
    }

    
    public boolean validateLanguage(Language lang) {
        return lang.getName().length() < 32;
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
        setSnippet(Snippets.load(snippet));
    }

    public String login() {
        try {
            setUser(Users.login(user));
        } catch(Exception e) {
            ex = e;
            return "error";
        }
        prepareNav();
        return "home";
    }

    public void logout() {
        user = new User();
        snippet = new Snippet();
        userNav = null;
    }

    
    
    // USER DB MANIPULATION
    public String saveUser() {
        try {
            setUser(Users.save(user));
        } catch(Exception e) {
            ex = e;
            return "error";
        }
        prepareNav();
        return "home";
    }

    
    
    // SNIPPET DB MANIPULATION
    public String saveSnippet() {
        if (!Users.isValidUser(user)) return "error"; // check if the user is valid
        try {
            snippet.setUser(user);
            user.addSnippet(Snippets.save(snippet));
            prepareNav();
        } catch(Exception e) {
            ex = e;
            return "error";
        } return "show_snippet";
    }

    public String deleteSnippet() {
        try {
            user.removeSnippet(Snippets.delete(snippet));
            prepareNav();
        } catch(Exception e) {
            ex = e;
            return "error";
        } return "home";
    }

    
    
    // HELPER
    public int countSnippetsInLanguage(Map<Category, List<Snippet>> m) {
        int sum = 0;
        for (Map.Entry<Category, List<Snippet>> s : m.entrySet()) {
            sum += s.getValue().size();
        }
        return sum;
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

    
    
    // GETTER & SETTER
    public User getUser() {
        return user;
    }

    public boolean setUser(User user) {
        if(user != null) {
            this.user = user;
            return true;
        } else {
            this.user = new User();
            return false;
        }
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
        this.snippet = snippet != null ? snippet : new Snippet();
    }

    public Map getUserNav() {
        return userNav;
    }

    public void setUserNav(Map userNav) {
        this.userNav = userNav;
    }

}
