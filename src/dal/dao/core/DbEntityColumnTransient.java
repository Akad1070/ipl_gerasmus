package dal.dao.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sert à marquer un attribut 'transient'. Un champ qui ne sert pas dans la DB.
 *
 * @author Akaad
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface DbEntityColumnTransient {
}
