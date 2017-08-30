package de.tallaron.snips;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SnipsLocales implements Serializable {

    private Map<String, Locale> availableLocales;
    private String currentLocale = Locale.GERMAN.toString();

    {
        availableLocales = new LinkedHashMap<>();
        availableLocales.put("English", Locale.ENGLISH);
        availableLocales.put("Deutsch", Locale.GERMAN);
    }

    public void setCurrentLocale(String newLocale) {
        this.currentLocale = newLocale;
        for (Entry<String, Locale> entry : availableLocales.entrySet()) {
            if (entry.getValue().toString().equals(newLocale)) {
                Locale.setDefault(entry.getValue());
                FacesContext.getCurrentInstance().getViewRoot().setLocale(entry.getValue());
            }
        }
    }

    public Map<String, Locale> getAvailableLocales() {
        return availableLocales;
    }

    public String getCurrentLocale() {
        return currentLocale;
    }

}
