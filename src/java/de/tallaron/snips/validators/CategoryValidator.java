package de.tallaron.snips.validators;

import de.tallaron.snips.entities.Category;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("CategoryValidator")
public class CategoryValidator extends AbstractValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if(((Category)o).getName().length() > 32) {
            throw new ValidatorException(new FacesMessage(getResourceString("loca", "ERROR_SNIPPET_CATEGORY_TO_LONG")));
        }
        
    }
    
}
