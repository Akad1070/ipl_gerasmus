package domaine.ucc;

import core.config.AppConfig;
import core.exceptions.BizzException;
import core.exceptions.DalException;
import dal.dao.interfaces.DocumentDao;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.DocumentDto.Genre;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import domaine.ucc.interfaces.ProfesseurUcc;
import util.AppUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProfesseurUccImpl extends EtudiantUccImpl implements ProfesseurUcc {



  // *************************************** UC Documents ************************************* //

  @Override
  public List<DocumentDto> encoderSignatureDocuments(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    AppUtil.checkObject(mobilite.getDocumentsSignes());

    DocumentDao docDao = daoFactory.getDocumentDao();
    Etat etat = mobilite.getEtat();

    dalbs.startTransaction();

    for (DocumentDto doc : mobilite.getDocumentsSignes()) {
      doc.setMobilite(mobilite);
      if (!docDao.ajouterDocumentRempli(doc)) {
        dalbs.rollback();
        break;
};

    }

    List<DocumentDto> docARemplir = docDao.getListeDocumentsARemplir(mobilite);
    List<DocumentDto> docRemplis = docDao.getListeDocumentsRemplis(mobilite);

    boolean docDepartTousRemplis = true;

    // Si tous les documents sont remplis.
    if (docARemplir.size() == docRemplis.size()) {
      mobilite.setEtat(Etat.A_PAYER_SOLDE);
    } else {
      for (DocumentDto doc : docARemplir) {
        if (doc.getGenre().equals(Genre.DEPART)) {
          if (!docRemplis.contains(doc)) {
            docDepartTousRemplis = false;
          }
        }
      }

      // Si il faut changer l'état de la mobilité.
      if (!etat.equals(Etat.DEMANDE_PAYEMENT)) {
        if (docDepartTousRemplis) {
          mobilite.setEtat(Etat.A_PAYER);
        } else {
          mobilite.setEtat(Etat.PREPA);
        }

      }
    }

    changerEtat(mobilite);
    dalbs.commit();

    return docRemplis;
  }


  // *************************************** UC Mobilité ************************************** //


  @Override
  public List<DemandeMobiliteDto> listerDemandeMobilites(List<DepartementDto> departements) {
    AppUtil.checkObject(departements);

    List<DemandeMobiliteDto> list = daoFactory.getDemandeMobiliteDao().listerDemande(departements);

    list.stream().forEach(dem -> {
      dem.setEtudiant(daoFactory.getUserDao().findByDemandeMobilite(dem));
      // Kel étudiant a intro ca
      UserDto demEtud = dem.getEtudiant();
      // Mnt me faut son departement
      demEtud.setDepartement(daoFactory.getDepartementDao().findByUser(demEtud));
      // Je remplis les choix de cette demandes des DTO nécessaires.

      dem.setChoixMobilites(daoFactory.getChoixMobiliteDao().listerMobiliteDuneDemande(dem));
      dem.getChoixMobilites().forEach(mob -> {
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



  // ********************************** UC Encodage Logiciel ********************************** //

  @Override
  public boolean gererEncodageLogiciels(ChoixMobiliteDto mobilite) {
    AppUtil.checkObjects(mobilite);
    AppUtil.checkObjects(mobilite.getLogiciels());

    List<LogicielDto> dejaEncodes = daoFactory.getLogicielDao().logicielsEncodes(mobilite);

    dalbs.startTransaction();
    try {
      mobilite.getLogiciels().forEach(log -> {
        if (dejaEncodes.contains(log)) {
          // Si l'encodage dans le logiciel avait déjà été confirmé alors on l'annule.
          daoFactory.getLogicielDao().annulerEncodageLogiciel(log, mobilite);
        } else {
          // Sinon on confirme l'encodage du logiciel.
          daoFactory.getLogicielDao().confirmerEncodageLogiciel(log, mobilite);
        }
      });
    } catch (DalException exception) {
      dalbs.rollback();
    }
    return dalbs.commit();
  }



  // *************************************** UC Listing ************************************** //



  @Override
  public Map<ChoixMobiliteDto, Integer> listerDemandeDePayement(int annee) {
    AppUtil.checkPositive(annee);

    return daoFactory.getChoixMobiliteDao().listerMobiliteAvecDemandePayement(annee).stream()
        .collect(Collectors.toMap(Function.identity(),
            mob -> (mob.getEtat().equals(Etat.DEMANDE_PAYEMENT_SOLDE) ? 2 : 1)));

  }

  @Override
  public List<AnnulationDto> listerMotifsAnnulations() {
    return daoFactory.getAnnulationDao().listerAnnulationPourProf();
  }

  @Override
  public PartenaireDto changerVisibiliterPartenaire(PartenaireDto part) {
    AppUtil.checkObject(part);


    // Si on désire cacher le partenaire, il faut vérifier qu'il n'est lié à aucune mobilité.
    if (part.isVisible()) {
      if (daoFactory.getChoixMobiliteDao().isLinked(part)) {
        throw new BizzException(
            "Impossible de cacher le partenaire car il est lié à une ou plusieurs mobilités.");
      }
    }
    part.setVisible(!part.isVisible());

    PartenaireDto partRecu = daoFactory.getPartenaireDao().changerVisibilite(part);

    if (partRecu == null) {
      throw new BizzException(AppConfig.getValueOf("VERSION_INVALID"));
    }
    return partRecu;
  }

  @Override
  public List<PartenaireDto> listerPartenairesSelectionnablesPourTous() {
    List<PartenaireDto> list =
        daoFactory.getPartenaireDao().listerPartenairesSelectionnablesPourEtudiants();
    list.forEach(p -> p.setPays(daoFactory.getPaysDao().findByPartenaire(p)));
    return list;
  }

}
