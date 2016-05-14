package domaine.ucc;

import core.config.AppConfig;
import core.exceptions.BizzException;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.Injecter;
import dal.dao.core.DaoFactoryImpl;
import dal.dao.interfaces.DocumentDao;
import dal.dao.interfaces.UserInfoDao;
import dal.service.DalService;
import domaine.bizz.interfaces.AnnulationBizz;
import domaine.bizz.interfaces.DemandeMobiliteBizz;
import domaine.bizz.interfaces.PartenaireBizz;
import domaine.bizz.interfaces.UserInfoBizz;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import domaine.factory.EntiteFactory;
import domaine.ucc.interfaces.EtudiantUcc;
import util.AppUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EtudiantUccImpl implements EtudiantUcc {

  @Injecter
  protected EntiteFactory dtoFactory;

  @Injecter
  protected DaoFactoryImpl daoFactory;

  @Injecter
  protected DalService dalbs;



  // *************************************** UC Mobilité ************************************** //

  // @Override
  @Override
  public ChoixMobiliteDto changerEtat(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    Etat etat = mobilite.getEtat();

    try {
      // S'il s'agit d'indiquer une demande de payement.
      if (etat.equals(Etat.DEMANDE_PAYEMENT) || etat.equals(Etat.DEMANDE_PAYEMENT_SOLDE)) {
        // Vérifier que l'étudiant concerné par la mobilité à ajouter un compte bancaire.
        if (!daoFactory.getUserInfoDao().compteBancaireNotNull(mobilite)) {
          throw new BizzException(
              "Impossible de confirmer la demande de payement car l'étudiant concerné "
                  + "par la mobilité n'a pas indiquer son compte bancaire.");
        }

      }

      ChoixMobiliteDto mobiliteRet = daoFactory.getChoixMobiliteDao().updateEtat(mobilite);

      if (mobiliteRet == null) {
        throw new BizzException(AppConfig.getAppValueOf("VERSION_INVALID"));
      }
      return mobiliteRet;
    } catch (FatalException exception) {
      throw new BizzException("Le changement d'état de la mobilité à échoué.");
    }
  }

  @Override
  public boolean declarerChoix(DemandeMobiliteDto demande) {
    AppUtil.checkObject(demande);
    AppUtil.checkObject(demande.getEtudiant());
    AppUtil.checkObject(demande.getChoixMobilites());



    if (daoFactory.getUserDao().findById(demande.getEtudiant().getId()) == null) {
      throw new BizzException("L'utilisateur spécifié n'existe pas !");
    }

    if (daoFactory.getDemandeMobiliteDao().findByAnneeAndStudent(demande) != null) {
      throw new BizzException(
          "Vous avez déja introduit une demande pour l'année " + demande.getAnneeAcademique());
    }
    DemandeMobiliteBizz demandeBizz = (DemandeMobiliteBizz) demande;
    demande.setDateIntroduction(LocalDate.now());
    demandeBizz.checkBeforeInsert();
    dalbs.startTransaction();

    try {
      daoFactory.getDemandeMobiliteDao().insererDemande(demandeBizz);
      for (ChoixMobiliteDto choix : demande.getChoixMobilites()) {
        AppUtil.checkObject(choix);
        choix.setDemande(demandeBizz);
        choix.setEtat(Etat.INTRO);
        daoFactory.getChoixMobiliteDao().insererMobilite(choix);
      }
      return dalbs.commit();
    } catch (FatalException exception) {
      dalbs.rollback();
      throw new BizzException("Erreur lors de l'insertion de l'insertion des choix de mobilités.");
    }

  }


  @Override
  public PartenaireDto ajouterPartenaire(UserDto createur, PartenaireDto partenaire) {
    AppUtil.checkObjects(partenaire, createur);
    AppUtil.checkObject(partenaire.getIplDepartements());

    PartenaireBizz partenaireBizz = (PartenaireBizz) partenaire;

    if (daoFactory.getUserDao().findById(createur.getId()) == null) {
      throw new BizzException("L'utilisateur spécifié n'existe pas !");
    }
    if (!createur.isProf()) {
      partenaireBizz.setCreateur(createur);
    }
    partenaireBizz.checkBizz();

    dalbs.startTransaction();

    try {
      partenaire = daoFactory.getPartenaireDao().inserer(partenaire);
      partenaireBizz.getIplDepartements().stream().forEach(departement -> {
        daoFactory.getPartenaireDao().lierADepartement(partenaireBizz, departement);
      });
      dalbs.commit();
      return partenaire;
    } catch (FatalException exception) {
      dalbs.rollback();
      throw new BizzException("L'ajout du partenaire à échoué.");
    }

  }


  @Override
  public ChoixMobiliteDto annulerChoixMobilite(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    AppUtil.checkObject(mobilite.getMotifAnnulation());
    AppUtil.checkObject(mobilite.getMotifAnnulation().getCreateur());

    if (daoFactory.getUserDao()
        .findById(mobilite.getMotifAnnulation().getCreateur().getId()) == null) {
      throw new BizzException("Le créateur de l'annulation n'existe pas !");
    }
    try {
      // Si c'est un étudiant qui veux annuler.
      if (!mobilite.getMotifAnnulation().getCreateur().isProf()) {
        DemandeMobiliteDto dem = dtoFactory.getDemandeMobilite();
        dem.setId(mobilite.getNumCandidature());

        if (daoFactory.getDemandeMobiliteDao().findById(dem.getId()) == null) {
          throw new BizzException(
              "Ce choix de mobilité n'est relié à aucune demande de mobilité !");
        }
        // On test si l'étudiant qui veut annuler est bien celui qui à fait la demande.
        if (daoFactory.getUserDao().findByDemandeMobilite(dem).getId() != mobilite
            .getMotifAnnulation().getCreateur().getId()) {
          throw new BizzException("Vous n'êtes pas le créateur de cette candidature de mobilité");
        }
      }

      dalbs.startTransaction();
      AnnulationBizz annulBizz = (AnnulationBizz) mobilite.getMotifAnnulation();
      annulBizz.checkBizz();
      daoFactory.getAnnulationDao().insererAnnulation(annulBizz);

      if ((mobilite = daoFactory.getChoixMobiliteDao().annulerChoixMobilite(mobilite)) == null) {
        dalbs.rollback();
        throw new BizzException(AppConfig.getAppValueOf("VERSION_INVALID"));
      }
    } catch (FatalException exception) {
      dalbs.rollback();
      throw new BizzException("Erreur lors de l'annulation de la mobilité.");
    }
    dalbs.commit();
    mobilite.incNumeroVersion();
    return mobilite;
  }

  @Override
  public ChoixMobiliteDto confirmerPartenaire(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    AppUtil.checkObject(mobilite.getPartenaire());

    PartenaireDto partenaire = mobilite.getPartenaire();
    DemandeMobiliteDto demande = dtoFactory.getDemandeMobilite();
    demande.setId(mobilite.getNumCandidature());
    demande.setEtudiant(mobilite.getDemande().getEtudiant());

    UserDto etudiant = daoFactory.getUserDao().findByDemandeMobilite(demande);

    if (daoFactory.getChoixMobiliteDao().hasMobiliteConfirmed(etudiant)) {
      throw new BizzException(
          "L'étudiant ne peux avoir qu'une seule mobilité confirmée par année académique");
    }

    mobilite = daoFactory.getChoixMobiliteDao().findById(mobilite.getId());


    PartenaireDto partenaireDb =
        daoFactory.getPartenaireDao().findById(mobilite.getPartenaire().getId());

    if (!mobilite.getEtat().equals(Etat.INTRO)) {
      throw new BizzException("Cette mobilité à déjà été confirmé.");
    }

    mobilite.setEtat(Etat.CONFIRME);

    if (partenaireDb != null) {
      // Cas partenaire existant.
      if (partenaire.estSelectionable()) {
        return changerEtat(mobilite);
      } else {
        throw new BizzException("Impossible de confirmer la mobilité pour ce partenaire.");
      }
    } else {
      // Cas d'un nouveau partenaire.
      dalbs.startTransaction();
      // TOO
      partenaire = ajouterPartenaire(mobilite.getDemande().getEtudiant(), partenaire);
      mobilite.setPartenaire(partenaire);
      changerEtat(mobilite);
      dalbs.commit();
    }
    return mobilite;
  }

  // TODO Daveid
  // Faut pas plutôt renvoyer des ChoixMobi ?
  // Ou alors une seul demande ?
  // Vu qu'on liste pour l'année académique courante ?
  @Override
  public List<DemandeMobiliteDto> listerMobilitesPourEtud(UserDto etudiant) {
    AppUtil.checkObject(etudiant, "L'étudiant ne peut pas être null");

    List<DemandeMobiliteDto> list = daoFactory.getDemandeMobiliteDao().findByStudent(etudiant);
    final UserDto demEtud = daoFactory.getUserDao().findById(etudiant.getId());
    demEtud.setDepartement(daoFactory.getDepartementDao().findByUser(demEtud));
    list.stream().forEach(dmd -> {
      dmd.setEtudiant(demEtud);
      dmd.setChoixMobilites(daoFactory.getChoixMobiliteDao().listerMobiliteDuneDemande(dmd));
      dmd.getChoixMobilites().forEach(mob -> {
        PartenaireDto part = daoFactory.getPartenaireDao().findByMobilite(mob);
        if (part != null) {
          mob.setPartenaire(part);
        }
        mob.setProgramme(daoFactory.getProgrammeDao().findByMobilite(mob));
        mob.setTypeProgramme(daoFactory.getTypeProgrammeDao().findByMobilite(mob));

      });
    });
    return list;
  }


  @Override
  public List<PartenaireDto> listerPartenairesPourEtudiant(UserDto etudiant) {
    AppUtil.checkObject(etudiant, "L'etudiant ne peut pas être null");
    return daoFactory.getPartenaireDao().listerPartenairesSelectionnables(etudiant);
  }

  // ************************** UC Encoder Informations Personelles *************************** //


  @Override
  public UserInfoDto encoderInformationsPersonnelles(UserInfoDto infos) {

    AppUtil.checkObject(infos);
    AppUtil.checkObject(infos.getUtilisateur());

    if (!daoFactory.getChoixMobiliteDao().hasMobiliteConfirmed((infos.getUtilisateur()))) {
      throw new BizzException("L'étudiant doit avoir une mobilité confimée pour pouvoir "
          + "encoder ses informations personnelles");
    }

    UserInfoBizz userInfoBizz = (UserInfoBizz) infos;

    userInfoBizz.checkBeforeInsert();

    UserInfoDao dao = daoFactory.getUserInfoDao();

    try {
      // Test pour savoir si on doit faire un INSERT ou un UPDATE.
      if (dao.findById(infos.getUtilisateur().getId()) == null) {
        infos = dao.insererInfo(infos);
      } else {
        infos = dao.update(infos);
        if (infos == null) {
          throw new BizzException(AppConfig.getAppValueOf("VERSION_INVALID"));
        }
      }
    } catch (FatalException exception) {
      throw new BizzException("L'encodage des informations personnelles à échoué.");
    }
    return infos;
  }



  // *************************************** UC Recherche ************************************* //

  @Override
  public List<ChoixMobiliteDto> rechercherMobilite(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite, "La mobilite n'est pas défini");
    List<ChoixMobiliteDto> mobisFound =
        daoFactory.getChoixMobiliteDao().findMobilityByAnneeAndEtat(mobilite);
    if (mobisFound == null) {
      return mobisFound;
    }
    remplirMobilites(mobisFound.stream());
    return mobisFound;
  }


  protected void remplirMobilites(Stream<ChoixMobiliteDto> mobisFound) {
    mobisFound.forEach(m -> {
      m.setDemande(daoFactory.getDemandeMobiliteDao().findById(m.getId()));
      m.getDemande().setChoixMobilites(
          daoFactory.getChoixMobiliteDao().listerMobiliteDuneDemande(m.getDemande()));
      m.getDemande().getChoixMobilites().forEach(mob -> {
        PartenaireDto part = daoFactory.getPartenaireDao().findByMobilite(mob);
        if (part != null) {
          mob.setPartenaire(part);
        }
        mob.setProgramme(daoFactory.getProgrammeDao().findByMobilite(mob));
        mob.setTypeProgramme(daoFactory.getTypeProgrammeDao().findByMobilite(mob));
      });
      m.getDemande().setEtudiant(daoFactory.getUserDao().findByDemandeMobilite(m.getDemande()));
      m.getDemande().getEtudiant()
          .setDepartement(daoFactory.getDepartementDao().findByUser(m.getDemande().getEtudiant()));
      // m.setLocalite(daoFactory.getPaysDao().findByMobilite(m));
      PartenaireDto part = daoFactory.getPartenaireDao().findByMobilite(m);
      if (part != null) {
        m.setPartenaire(part);
      }
      if (m.isAnnule()) {
        m.setMotifAnnulation(daoFactory.getAnnulationDao().findByMobilite(m));
      }
      m.setProgramme(daoFactory.getProgrammeDao().findByMobilite(m));
      m.setTypeProgramme(daoFactory.getTypeProgrammeDao().findByMobilite(m));
      if (m.isConfirme()) {
        m.setEtatsDocumentsSignes(listerEtatDocuments(m));
        m.setEtatsLogicielsEncodes(listerEtatLogicielsEncodes(m));
      }
    });
  }


  @Override
  public List<ChoixMobiliteDto> recupererMobilitesPourDemande(DemandeMobiliteDto demande) {
    AppUtil.checkObject(demande, "La demande n'est pas défini");
    List<ChoixMobiliteDto> list =
        daoFactory.getChoixMobiliteDao().listerMobiliteDuneDemande(demande);
    remplirMobilites(list.stream());
    return list;
  }


  @Override
  public List<UserDto> rechercherUser(UserDto user) {
    AppUtil.checkObject(user, "L'utilisateur recherché ne peut être null");
    List<UserDto> usersFound = daoFactory.getUserDao().findByName(user);

    if (usersFound == null) {
      return usersFound;
    }
    usersFound.stream()
        .forEach(u -> u.setDepartement(daoFactory.getDepartementDao().findByUser(u)));
    return usersFound;
  }


  @Override
  public UserDto recupererUserParId(UserDto user) {
    AppUtil.checkObject(user, "L'utlisateur n'est pas défini");
    user = daoFactory.getUserDao().findById(user.getId());
    if (user == null) {
      throw new BizzException(AppConfig.getAppValueOf("USER_INCONNU"));
    }
    user.setDepartement(daoFactory.getDepartementDao().findByUser(user));
    return user;
  }


  @Override
  public UserInfoDto recupererUserInfoParUId(UserDto user) {
    AppUtil.checkObject(user, "L'utlisateur n'est pas défini");
    return daoFactory.getUserInfoDao().findById(user.getId());
  }


  @Override
  public PartenaireDto recupererPartenaireParId(PartenaireDto partenaire) {
    AppUtil.checkObject(partenaire, "Le partenaire n'est pas défini");
    partenaire = daoFactory.getPartenaireDao().findById(partenaire.getId());
    if (partenaire == null) {
      return partenaire;
    }
    partenaire.setIplDepartements(daoFactory.getDepartementDao().findByPartenaire(partenaire));
    partenaire.setPays(daoFactory.getPaysDao().findByPartenaire(partenaire));
    UserDto createur = daoFactory.getUserDao().findByPartenaire(partenaire);
    if (createur != null) {
      partenaire.setCreateur(createur);
    }
    return partenaire;
  }



  @Override
  public List<PartenaireDto> rechercherPartenaire(PartenaireDto partenaire) {
    AppUtil.checkObjects(partenaire);

    List<PartenaireDto> patsFound = daoFactory.getPartenaireDao().rechercher(partenaire);
    if (patsFound == null) {
      return patsFound;
    }
    patsFound.forEach(pat -> {
      pat.setIplDepartements(daoFactory.getDepartementDao().findByPartenaire(pat));
      pat.setPays(daoFactory.getPaysDao().findByPartenaire(pat));
      UserDto createur = daoFactory.getUserDao().findByPartenaire(pat);
      if (createur != null) {
        pat.setCreateur(createur);
      }
    });
    return patsFound;
  }



  // **************************************** Lister ****************************************** //

  @Override
  public List<DepartementDto> listerDepartements() {
    return daoFactory.getDepartementDao().findAll();
  }


  @Override
  public List<PaysDto> listerPays() {
    return daoFactory.getPaysDao().findAll().stream()
        .sorted((p1, p2) -> p1.getNom().compareToIgnoreCase(p2.getNom()))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProgrammeDto> listerProgrammes() {
    return daoFactory.getProgrammeDao().findAll();
  }


  @Override
  public List<TypeProgrammeDto> listerTypesProgramme() {
    return daoFactory.getTypeProgrammeDao().findAll();
  }

  @Override
  public List<Integer> listerAnneesAcademiqueDejaIntroduites() {
    return daoFactory.getDemandeMobiliteDao().findAllAnnee();
  }


  @Override
  public Map<DocumentDto, Boolean> listerEtatDocuments(ChoixMobiliteDto mobi) {
    AppUtil.checkObject(mobi);
    DocumentDao dao = daoFactory.getDocumentDao();
    List<DocumentDto> docRempli = dao.getListeDocumentsRemplis(mobi);
    if (docRempli != null) {
      return dao.getListeDocumentsARemplir(mobi).stream()
          .collect(Collectors.toMap(Function.identity(), doc -> docRempli.contains(doc)));
    }
    return Collections.emptyMap();
  }


  @Override
  public Map<LogicielDto, Boolean> listerEtatLogicielsEncodes(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);

    List<LogicielDto> logicielsEncodes = daoFactory.getLogicielDao().logicielsEncodes(mobilite);

    return daoFactory.getLogicielDao().logicielsAEncoder(mobilite).stream()
        .collect(Collectors.toMap(Function.identity(), log -> logicielsEncodes.contains(log)));
  }

}
