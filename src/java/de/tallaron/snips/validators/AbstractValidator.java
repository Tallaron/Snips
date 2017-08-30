package de.tallaron.snips.validators;

import javax.faces.context.FacesContext;

public abstract class AbstractValidator {
    
    public String getResourceString(String bundleVar, String bundleKey) {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getApplication().getResourceBundle(fc, bundleVar).getString(bundleKey);
    }
    
}
