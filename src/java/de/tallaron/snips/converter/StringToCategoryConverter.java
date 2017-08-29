package de.tallaron.snips.converter;

import de.tallaron.snips.entities.Category;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("CategoryConverter")
public class StringToCategoryConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (string != null && string.length() > 0) {
            Category cat = new Category();
            cat.setName(string);
            return cat;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o instanceof Category) {
            return ((Category) o).getName();
        } return "UNKNOWN";
    }

}
