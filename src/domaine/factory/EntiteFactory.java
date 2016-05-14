package domaine.factory;

import core.AppContext;
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


public interface EntiteFactory {

  /**
   * Renvoie une instance de l'Entite correspondante à la classe en args.
   *
   * @param entiteClass - La classe de la DTO.
   * @return une instance de l'implementation de la DTO
   * @see AppContext#getInstanceOf(Class)
   */
  <I> I getEntite(Class<?> entiteClass);


  UserDto getUser();


  DepartementDto getDepartement();


  ChoixMobiliteDto getChoixMobilite();


  DemandeMobiliteDto getDemandeMobilite();


  ProgrammeDto getProgramme();


  TypeProgrammeDto getTypeProgramme();


  PartenaireDto getPartenaire();


  PaysDto getPays();


  UserInfo getUserInfo();


  AnnulationDto getAnnulation();


  DocumentDto getDocument();


  LogicielDto getLogiciel();

}
