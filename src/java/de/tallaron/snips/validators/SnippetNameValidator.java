package de.tallaron.snips.validators;

import de.tallaron.snips.entities.Language;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("SnippetNameValidator")
public class SnippetNameValidator extends AbstractValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if(((String)o).length() > 32) {
            throw new ValidatorException(new FacesMessage(getResourceString("loca", "ERROR_SNIPPET_NAME_TO_LONG")));
        }
        
    }
    
}
