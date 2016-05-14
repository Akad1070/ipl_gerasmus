package core.injecteur;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import util.AppUtil;
import core.AppContext;
import core.AppContext.Layer;
import core.config.AppConfig;
import core.exceptions.FatalException;

/**
 * Gere les injections de dépendances pour toute l'Application.
 */
public class InjecteurDependance {

  /**
   * Annotation pour indiquer la dependance de l'attribut. Doit être injecter.
   *
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
  public @interface Injecter {
  }


  /**
   * Annotation pour indiquer que le type en question ne doit pas être mis en cache.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public @interface NoCache {
  }


  private Map<Class<?>, Object> instanceCache = new ConcurrentHashMap<Class<?>, Object>();


  /**
   * Initiale le le systeme d'injection selon le contexte .
   *
   * @param app - Le contexte actuel de l'Application.
   */
  public InjecteurDependance(AppContext app) {
    app.getLayerLogger(Layer.INJECTEUR);

    this.addDependencyInstance(InjecteurDependance.class, this);
    this.addDependencyInstance(AppContext.class, app);
  }


  /**
   * Ajoute dans le cache ou non l'instance de la Class. Se sert de la class comme key pour retenir
   * son instance.
   *
   * @param dependant - La dependance .class
   * @param depInstance - Son instance.
   * @return true si la dependance est ajoutée. FALSE si déja présent.
   */
  public <A> boolean addDependencyInstance(Class<A> dependant, A depInstance) {
    final Class<? extends A> implementation = this.getImplementationClassOf(dependant);
    // Check si la class ne doit est pas être en cache
    if (implementation.getAnnotation(NoCache.class) != null) {
      return false;
    }
    // ? Cette class est dja dans le cache ?
    if (this.instanceCache.containsKey(implementation)) {
      return false;
    }
    this.instanceCache.put(implementation, depInstance);
    return true;
  }

  /**
   * Enleve du cache une instance de la class dépendante.
   *
   * @param dependant - La class de l'instance.
   * @return false si aucune n'est présente.
   */
  public <A> boolean removeFromCache(Class<A> dependant) {
    final Class<? extends A> implementation = this.getImplementationClassOf(dependant);
    if (!this.instanceCache.containsKey(implementation)) {
      return false;
    }
    this.instanceCache.remove(implementation);
    return true;
  }

  /**
   * Creation des instances d'une dépendance et de ses dépendances annotées. Pour cela, il se sert
   * du .properties en effectuant un matching Dependance => Implementation.
   *
   * @param dependance - La classe à instancer contenant les dépendances
   * @return l'instance de la dépendance du même type que la Dépendance.
   * @throws FatalException L'injection à echouer.
   */
  @SuppressWarnings("unchecked")
  public <A> A buildDependency(final Class<A> dependance) {
    final Class<? extends A> implClass = this.getImplementationClassOf(dependance);
    try {
      // Check si dja en cache
      A depInstance = (A) this.instanceCache.get(implClass);
      if (depInstance != null) {
        return depInstance;
      }
      // Check si la classe peut être instancier
      if (!Modifier.isAbstract(implClass.getModifiers())) {
        // Créer l'instance de l'implementation de la dépendance
        depInstance = implClass.newInstance();

        // Check si peut être mis en cache
        if (implClass.getAnnotation(NoCache.class) == null) {
          this.instanceCache.put(implClass, depInstance);
        }
        // Mnt check les attributs
        this.injectByAnnotedField(depInstance);
      }

      return depInstance;
    } catch (InstantiationException | IllegalAccessException exception) {
      throw new FatalException("Could not instantiate " + dependance.getCanonicalName(), exception);
    } catch (ClassCastException exception) {
      throw new FatalException(dependance.getCanonicalName()
          + " has for declared instanciable class '" + implClass.getCanonicalName()
          + "' but that class doesn't implement/extends" + " the dependency.", exception);
    }
  }


  /**
   * Chope le chemin vers l'implementation de la dépendance dans .prop. <br>
   * Ensuite, retourne <b>juste</b> cette class.
   *
   * @param dependType - La classe de la dependance
   * @param <A> - Le type de la classe
   */
  @SuppressWarnings("unchecked")
  private <A> Class<A> getImplementationClassOf(Class<A> dependType) {
    if (!dependType.isInterface()) {
      return dependType;
    }
    try {
      String pathForImpl = AppConfig.getValueOf(dependType.getSimpleName());
      return (Class<A>) Class.forName(pathForImpl);
    } catch (IllegalArgumentException | ClassNotFoundException exception) {
      throw new FatalException("Could not fetch '" + dependType.getSimpleName()
          + "' implementation.", exception);
    }
  }

  /**
   * Parcours les attibuts annotés {@link Injecter} de l'instance et injecte(set) les dépendances
   * necessaires.
   *
   * @param depInstance - L'instance de la dépendance à injecter.
   * @throws FatalException L'injection à echouer.
   */
  public void injectByAnnotedField(Object depInstance) {
    Class<?> clazzInstance = depInstance.getClass();

    // Field[] fields = clazzInstance.getDeclaredFields();
    List<Field> fields = AppUtil.getAllFieldsFrom(clazzInstance);
    for (Field field : fields) {
      // N''est pas annotée, fais rien continue de boucler
      if (field.getAnnotation(Injecter.class) == null) {
        continue;
      }
      // Met l'attribut en accessible pour modif
      field.setAccessible(true);
      try {
        field.set(depInstance, this.buildDependency(field.getType()));
      } catch (Exception exception) {
        throw new FatalException("Populating '" + clazzInstance.getCanonicalName() + "' failed",
            exception);
      }
    }
  }



}
