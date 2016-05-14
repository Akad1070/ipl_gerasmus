package dal.dao.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
/**
 * Decrit la colonne en DB de cette table.
 * @author Akaad
 */
public @interface DbEntityColumn {

  /**
   * @return Le nom de la colonne en DB.
   */
  String value();

}
