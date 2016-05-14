package dal.dao.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
/**
 * Décrit la PK en DB pour l'Entity.
 * @author Akaad
 */
public @interface DbEntityPk {

  /**
   * @return <b>TRUE</b> si la PK est composé de plusieurs PK.
   */
  boolean compose() default false;

  /**
   * @return Le nom de la PK en DB pour cet Entity.
   */
  String value() default "id";

}
