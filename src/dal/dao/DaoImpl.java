package dal.dao;

import core.config.AppConfig;
import core.exceptions.DalException;
import core.injecteur.InjecteurDependance.Injecter;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityColumnTransient;
import dal.dao.core.DbEntityFk;
import dal.dao.core.DbEntityPk;
import dal.dao.interfaces.Dao;
import dal.service.DalBackendService;
import domaine.factory.EntiteFactory;
import util.AppUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoCache
public abstract class DaoImpl<E> implements Dao<E> {

  // Contient la connexion vers la DB
  @Injecter
  protected DalBackendService dalbs;

  // Sert à creer directement une Entite contenant le resultat de la Query.
  @Injecter
  protected EntiteFactory factory;

  // Pour pouvoir retenir le PrepareStatement. Pas vraiment indispensable
  protected PreparedStatement ps;

  // Le class Bizz correspond au Dto en <E>
  private Class<E> entity;
  private Map<Field, String> mapBdColumnNameByFieldName;
  // /private Map<String, Field> mapBdFieldsByColumnName;
  private List<Field> entityFields; // Les attributs de l'Entity

  protected String schema;
  protected String table;

  // private String queryForInsert, queryForUpdate;

  /**
   * Configurer le DAO Generik avant utilisation.
   */
  public DaoImpl() {
    super();
    this.configDbData();
  }



  // @Override
  // public E inserer(E entite) {
  // this.ps =
  // this.dalbs.createPrepareStatement(this.queryForInsert, Statement.RETURN_GENERATED_KEYS);
  //
  // return null;
  // }

  @Override
  public E findById(int id) {
    try {
      String query = String.format(AppConfig.getValueOf("r_id"), this.table);
      this.ps = this.dalbs.createPrepareStatement(query, 0);
      this.ps.setInt(1, id);
      try (ResultSet rs = this.ps.executeQuery()) {
        if (rs.next()) {
          return this.fillEntityWithResult(rs);
        }
      }
    } catch (SQLException exception) {
      throw new DalException(exception);
    }
    return null;
  }


  @Override
  public List<E> findAll() {
    List<E> list = null;
    try {
      String query = String.format(AppConfig.getValueOf("r_all"), this.table);
      this.ps = this.dalbs.createPrepareStatement(query, 0);
      try (ResultSet res = this.ps.executeQuery()) {
        list = new ArrayList<>(res.getMetaData().getColumnCount());
        while (res.next()) {
          list.add(this.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new DalException(exception);
    }
    return list;
  }

  /*
   * @Override public E update() { return null; }
   */

  @SuppressWarnings("unchecked")
  private void configDbData() {
    try {
      // Recupere le type de Dto passée pour le Generik
      ParameterizedType generikSuper = (ParameterizedType) this.getClass().getGenericSuperclass();
      this.entity = (Class<E>) generikSuper.getActualTypeArguments()[0];
      // Va chercher la classe Bizz pour cette DTO
      this.entity = (Class<E>) Class.forName(AppConfig.getValueOf(this.entity.getSimpleName()));
      // Recup tt & filtre les fields
      this.entityFields =
          AppUtil
          .getAllFieldsFrom(this.entity)
          .stream()
          .filter( // Petit check pour degager les attributs static || final ou marqué Transient
              fd -> (!Modifier.isFinal(fd.getModifiers())
                  && !Modifier.isStatic(fd.getModifiers()) && !fd
                  .isAnnotationPresent(DbEntityColumnTransient.class) && !fd
                  .isAnnotationPresent(DbEntityFk.class)))
                  .collect(Collectors.toList());

      this.gatherEntityColumnsFromFields();

      // Recupere l'annotation sur l'Entity
      DbEntity dbAnnot = this.entity.getAnnotation(DbEntity.class);
      if (dbAnnot == null) {
        throw new DalException(String.format("Missing the annotation for '%s' to be a DBEntity",
            this.entity.getCanonicalName()));
      }
      this.schema = dbAnnot.schema(); // Recup le nom du schéma
      this.table = this.schema + "." + dbAnnot.table(); // Definis le nom de la table

      // this.queryForInsert = this.createQueryForInsert(); // Je crée déja la query pour l'insert
      // pour ce type de Dao
      // this.queryForUpdate = this.createQueryForUpdate(); // Je fais de même pour l'update
    } catch (ClassNotFoundException exception) {
      throw new DalException("Could not fetch '" + this.entity.getSimpleName() + "' impl.",
          exception);
    }
  }


  private String createQueryForInsert() {
    Collection<String> colsNm = this.mapBdColumnNameByFieldName.values();
    String cols = colsNm.toString().replace('[', '(').replace(']', ')');
    String vals = String.join(",", Collections.nCopies(colsNm.size(), "?"));
    return String.format("INSERT %s.%s%s VALUES (%s)", this.schema, this.table, cols, vals)
        .toString();
  }

  private String createQueryForUpdate() {

    return null;
  }


  /**
   * Recuperer le nom des colonnes selon l'attribut.
   */
  private void gatherEntityColumnsFromFields() {
    this.mapBdColumnNameByFieldName =
        this.entityFields.stream().collect(Collectors.toMap(Function.identity(), f -> {
          String dbColName = f.getName(); // Par-default, le nom de l'attribut est le nom del
          // ? Est-ce ke cet attribut est une PK ?
          DbEntityPk dbPk = f.getAnnotation(DbEntityPk.class);
          if (dbPk != null) {
            dbColName = dbPk.value();
          }
          // ? Est-ce ke cet attribut est annoté comme une EnityColumn ?
          DbEntityColumn dbCol = f.getAnnotation(DbEntityColumn.class);
          if (dbCol != null) {
            dbColName = dbCol.value();
          }
          f.setAccessible(true); // Met l'attribut en accessible pour modif
          return dbColName;
        }));

  }


  /**
   * Remplis une Entity avec le {@link ResultSet}.
   *
   * @param res - Le {@link ResultSet} du {@link PreparedStatement}.
   * @return Une Entity avec ses attributs correspondant au champ remplis.
   */
  protected <I> E fillEntityWithResult(ResultSet res) {
    try {
      E entityImpl = this.factory.getEntite(this.entity);
      for (Field field : this.entityFields) {
        String colName = this.mapBdColumnNameByFieldName.get(field);
        Object value = res.getObject(colName);
        // ? Est-ce ke cette attribut est une LocalDate ?
        if (field.getType().equals(LocalDate.class)) {
          field.set(entityImpl, res.getDate(colName).toLocalDate());
          continue;
        }
        // ? Est-ce ke cette attribut est un Enum ?
        if (field.getType().isEnum() && value != null) {
          // J'appelle ma methode valueOf sur cet Enum et je lui passe la value
          field.set(entityImpl,
              field.getType().getMethod("valueOf", String.class).invoke(null, value));
          continue;
        }

        // ? Est-ce ke cette attribut est un FK ?
        if (field.isAnnotationPresent(DbEntityFk.class)) {
          // TODO Create method which depending on the type of this field,
          // Create the type' Entity
          // Go fetch the Db data SELECT .... WHERE ....
          // Make it recursively if this type possess also a FK
          continue;
        }

        // Si viens ici, alors c'est juste un Integer ou String
        field.set(entityImpl, value);
      } // for entityFields
      return entityImpl;
    } catch (Exception exception) {
      throw new DalException("Couldn't fill the entity.", exception);
    }
  }

}
