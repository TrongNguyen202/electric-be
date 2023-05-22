package usoft.cdm.electronics_market.config.expection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImgValidator implements ConstraintValidator<CheckImg, String> {

    @Override
    public boolean isValid(String img, ConstraintValidatorContext context) {
        if (img.substring(img.lastIndexOf(".")).equalsIgnoreCase(".png") || img.substring(img.lastIndexOf(".")).equalsIgnoreCase(".jpg")) {
            return true;
        }
        return false;
    }
}
