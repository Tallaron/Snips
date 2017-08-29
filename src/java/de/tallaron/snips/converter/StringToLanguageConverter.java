
package de.tallaron.snips.converter;

import de.tallaron.snips.entities.Language;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("LanguageConverter")
public class StringToLanguageConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if(string != null && string.length() > 0) {
            Language lang = new Language();
            lang.setName(string);
            return lang;
        } return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o instanceof Language) {
            return ((Language) o).getName();
        } else {
            return "UNKNOWN";
        }
    }
    
}
