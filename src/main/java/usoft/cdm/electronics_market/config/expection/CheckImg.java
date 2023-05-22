package usoft.cdm.electronics_market.config.expection;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Constraint(validatedBy = ImgValidator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface CheckImg {

    String message() default "{CheckImg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
