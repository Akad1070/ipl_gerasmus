package domaine.factory;

import core.AppContext;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.bizz.Annulation;
import domaine.bizz.DemandeMobilite;
import domaine.bizz.Document;
import domaine.bizz.Logiciel;
import domaine.bizz.Pays;
import domaine.bizz.Programme;
import domaine.bizz.TypeProgramme;
import domaine.bizz.UserInfo;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import util.AppUtil;


public class EntiteFactoryImpl implements EntiteFactory {
  @Injecter
  private AppContext context;


  @Override
  @SuppressWarnings("unchecked")
  public <I> I getEntite(Class<?> cls) {
    AppUtil.checkObject(cls);
    return (I) context.getInstanceOf(cls);
  }

  @Override
  public UserDto getUser() {
    return context.getInstanceOf(UserDto.class);
  }

  @Override
  public UserInfo getUserInfo() {
    return context.getInstanceOf(UserInfo.class);
  }

  @Override
  public DepartementDto getDepartement() {
    return context.getInstanceOf(DepartementDto.class);
  }

  @Override
  public ChoixMobiliteDto getChoixMobilite() {
    return context.getInstanceOf(ChoixMobiliteDto.class);
  }


  @Override
  public DemandeMobiliteDto getDemandeMobilite() {
    return context.getInstanceOf(DemandeMobilite.class);
  }


  @Override
  public ProgrammeDto getProgramme() {
    return context.getInstanceOf(Programme.class);
  }


  @Override
  public TypeProgrammeDto getTypeProgramme() {
    return context.getInstanceOf(TypeProgramme.class);
  }


  @Override
  public PartenaireDto getPartenaire() {
    return context.getInstanceOf(PartenaireDto.class);
  }


  @Override
  public PaysDto getPays() {
    return context.getInstanceOf(Pays.class);
  }

  @Override
  public AnnulationDto getAnnulation() {
    return context.getInstanceOf(Annulation.class);
  }

  @Override
  public DocumentDto getDocument() {
    return context.getInstanceOf(Document.class);
  }


  @Override
  public LogicielDto getLogiciel() {
    return context.getInstanceOf(Logiciel.class);
  }





}
