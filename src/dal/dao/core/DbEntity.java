package dal.dao.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface DbEntity {

  /**
   * @return Le nom de la table en Db pour cet Entity.
   */
  String table();

  /**
   * @return Le schema dans lequel se trouve l'Entity.
   */
  String schema() default "gerasmus";



}
