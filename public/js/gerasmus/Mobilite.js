
// Recoie en param une instance d'Events qui ne sera accessible ke dans tt Base
// Pour permettre une discussion entre les différentes couches (parties)
var Mobilite = (function(Events){

	var mobi = {};

/*******************************************************************************************
								Partie MODEL
*******************************************************************************************/
	mobi.Model = function(){
		Base.Model.call(this); // equivaut à super

	};
	mobi.Model.heriteDe(Base.Model);


    mobi.Model.prototype.getPartenaires = function (uid){
        this.lancerReqAJAX('POST','req=partenaires&uid='+uid).then(function (response){
            Events.pub(Events.LIST_PARTENAIRES_NOM,response['partenaires']);
        });
    };

    mobi.Model.prototype.getDocsSignes = function (uid){
        this.lancerReqAJAX('POST','req=docs_signes&uid='+uid).then(function (response){
            Events.pub(Events.LIST_DOCS_SIGNES,response['docs_signes']);
        });
    };

    mobi.Model.prototype.getMotifs = function (uid){
        this.lancerReqAJAX('POST','req=motifs').then(function (response){
            Events.pub(Events.LIST_MOTIFS,response['motifs']);
        });
    };

    mobi.Model.prototype.ajouterMobilites = function(choix){
    	this.lancerReqAJAX('POST',null,choix).then(function (response){
    		Events.pub(Events.MOBI_AJOUT_OK,response['msg']);
    	});
    };

    mobi.Model.prototype.annulerMobilite = function(choix){
    	this.lancerReqAJAX('POST',null,choix).then(function (response){
    		Events.pub(Events.MOBI_ANNUL_OK,response['msg']);
    	});
    };

    mobi.Model.prototype.getAutreList = function(data){
    	return this.lancerReqAJAX('POST',null,data);
    };

    mobi.Model.prototype.listerDemandes = function(data){
    	this.lancerReqAJAX('POST',null,data).then(function (response){
    		Events.pub(Events.MOBI_LIST_OK,response['mobilites']);
    	});
    };


    mobi.Model.prototype.recupererDetailsMobilites = function(data){
    	this.lancerReqAJAX('POST',null,data).then(function (response){
    		Events.pub(Events.MOBI_DETAILS_OK,response['mobilites']);
    	});
    };

   mobi.Model.prototype.confirmerMobilite = function(data){
   	this.lancerReqAJAX('POST',null,data).then(function (response){
   		Events.pub(Events.MOBI_CONFIRM_OK,response['msg']);
   	});
   };

//    mobi.Model.prototype.indiquerPayement = function(choix){
//    	this.lancerReqAJAX('POST',null,choix).then(function (response){
//    		Events.pub(Events.MOBI_PAYEMENT_OK,response['msg']);
//    	});
//    };

//  mobi.Model.prototype.indiquerPayementSolde = function(choix){
//	this.lancerReqAJAX('POST',null,choix).then(function (response){
//		Events.pub(Events.MOBI_PAYEMENT_SOLDE_OK,response['msg']);
//	});
//};


/*******************************************************************************************
								Partie CONTROLLER
*******************************************************************************************/

	mobi.Controller = function(model){
		Base.Controller.call(this,model); // equivaut à super
	};
	mobi.Controller.heriteDe(Base.Controller);


    mobi.Controller.prototype.getPartenaires = function (){
        this._model.getPartenaires();
    };

    mobi.Controller.prototype.getMotifs = function (){
        this._model.getMotifs();
    };

    mobi.Controller.prototype.getDocsSignes = function (){
        this._model.getDocsSignes();
    };

    mobi.Controller.prototype.ajouterChoixMobilites = function(datas){
    	this._model.ajouterMobilites(datas);
   		Events.sub(Events.MOBI_AJOUT_OK, function afficherMsg(msg){
			Gerasmus.Vue.notification('success',msg);
			Gerasmus.Router.redirect('/mobilite/list');
		});
    };

    mobi.Controller.prototype.annulerMobilte = function(datas){
    	this._model.annulerMobilite(datas);
   		Events.sub(Events.MOBI_ANNUL_OK, function afficherMsg(msg){
			Gerasmus.Vue.notification('success',msg);
		});
    };

    mobi.Controller.prototype.listerDemandes = function(deps){
    	this._model.listerDemandes({'departements' : deps});
    };

    mobi.Controller.prototype.recupererDetailsMobilites = function(demandeID){
    	this._model.recupererDetailsMobilites({'mobilite_id':demandeID});
    };



    mobi.Controller.prototype.getAutreList = function (type) {
		this._model.getAutreList({'autre_list':type}).then(function (response){
			switch (type) {
				case 'docs_restants':
					Events.pub(Events.LIST_DOCS_RESTANTS, response['documents_restants']);
					break;
				case 'logis_restants':
					Events.pub(Events.LIST_LOGIS_RESTANTS, response['logiciels_restants']);
					break;
				default:
					// statements_def
					break;
			}
		});
    };

  //TODO
   mobi.Controller.prototype.confirmerMobilite = function(demandeID,mobiPref){
   	this._model.confirmerMobilite({'choix_id':demandeID,'preference_id':mobiPref,'type':'confirmer'});
  		Events.sub(Events.MOBI_CONFIRM_OK, function afficherMsg(msg){
			Gerasmus.Vue.notification('success',msg);
			Gerasmus.Router.redirect('/mobilite/list');
		});
   };

//    mobi.Controller.prototype.indiquerPayement = function(datas){
//    	this._model.indiquerPayement(datas);
//   		Events.sub(Events.MOBI_PAYEMENT_OK, function afficherMsg(msg){
//			Gerasmus.Vue.notification('success',msg);
//		});
//    };

//    mobi.Controller.prototype.indiquerPayementSolde = function(datas){
//    	this._model.indiquerPayementSolde(datas);
//   		Events.sub(Events.MOBI_PAYEMENT_SOLDE_OK, function afficherMsg(msg){
//			Gerasmus.Vue.notification('success',msg);
//		});
//    };

/*******************************************************************************************
								Partie VUE
*******************************************************************************************/

	mobi.Vue = function (ctrl){
		Base.Vue.call(this,ctrl);
		mobi.Vue.NB_MAX_CHOIX = 10;
		this.nbChoix = 1;

		this._ctrl.getEtatsMobilite();
	};
	mobi.Vue.heriteDe(Base.Vue);


	// Kan je recois une list de partenaires pour mobilites
	Events.sub(Events.LIST_PARTENAIRES_NOM, function remplirSelectPartenaire(partenaires){
		var $select = $('select[name="partenaire"]');
		$select.empty(); // S'il y a des <option>, ils degagent
        Gerasmus.Context.getVueActu().find('select[name="partenaire"]').append($('<option>').val('').html('Aucun'));
        for(var idx in partenaires){
            var pat = partenaires[idx];
            var $opt =$('<option>').val(pat.id).html(pat.nomCompvar || pat.nomLegal );
            $select.append($opt);
        }
        $select.trigger("chosen:updated");
	});

	// Kan je recois la liste des programmes
	Events.sub(Events.LIST_PROGS, function remplirSelectProgrammes(programmes){
		var $select = $('select[name="programme"]');
		$select.empty(); // S'il y a des <option>, ils degagent
        for(var idx in programmes){
            var $opt =$('<option>').val(programmes[idx].id).html(programmes[idx].nom);
            $select.append($opt);
        }
        $select.trigger("chosen:updated");
	});

	// Kan je recois la liste des types de programmes
	Events.sub(Events.LIST_TYPES_PROG, function remplirSelectProgrammes(types_prog){
		var $select = $('select[name="type_programme"]');
		$select.empty(); // S'il y a des <option>, ils degagent
        for(var idx in types_prog){
            var $opt = $('<option>').val(types_prog[idx].id).html(types_prog[idx].nom);
            $select.append($opt);
        }
        $select.trigger("chosen:updated");
	});

	//kan je recois la liste des motifs d'annulation.
	Events.sub(Events.LIST_MOTIFS, function remplirSelectMotifs(motifs){
		var $select = $('select[name="motif"]');
		$select.empty();
		for(var idx in motifs){
            var $opt = $('<option>').val(motifs[idx].id).html(motifs[idx].motif);
            $select.append($opt);
        }
        $select.trigger("chosen:updated");
	});





	function creerPanelPourDemande(demande,estProf){
		var PANEL = '<div class="panel">'+
				'<div class="panel-heading"><h3 class="panel-title"><a data-toggle="collapse"></a></h3></div>'+
				'<div class="panel-collapse collapse"><div class="panel-body"></div></div>'+
			'</div>';
		var $panel = $(PANEL);
		var panelHref = 'demande_'+demande.id;
		var etud = demande.etudiant.nom.toUpperCase() + ' '+ demande.etudiant.prenom;
		var titre = 'Demande #'+demande.id+' introduite par '+ etud +' pour l\'année '+demande.anneeAcademique;
		var $panelTitle = $('<a data-toggle="collapse">').attr('href','#'+panelHref).text(titre);
		$panel.find('.panel-title').append($panelTitle); // Je rajouter le titre du panel
		$panel.find('.collapse').attr('id',panelHref) // Pour le clic dessus
		$panel.find('.panel-body').html(creerTablePourChoixMobilites(demande.choixMobilites,demande,estProf)); // La table des choix de mobilites
		$panel.find('.panel-body').append($('<a>').attr('href','#/mobilite/details/'+demande.id).text('Plus de détails sur la demande'));

		return $panel.trigger('footable_initialize');
	}


	function creerTablePourChoixMobilites(choixMobilites,demande,estProf){
		var TABLE_HEADER = '<thead>'+
								'<tr>'+
									'<th data-toggle="true">N°</th><th>Etat</th><th data-hide="tablet">Destination</th><th>Introduit le</th><th>Pour</th><th>Type</th><th data-hide="all">Actions</th>'+
								'</tr>'+
							'</thead>';

		var $tableMobis = $('<table class="table footable table-striped">').append($(TABLE_HEADER));
		var $contenu = $('<tbody>');
		$tableMobis = $tableMobis.append($contenu);
		for(var idx in choixMobilites){
			var mobi = choixMobilites[idx];
			var row  = $('<tr>');
			var $linkDetails = $('<a>').attr({
				'href':'#/mobilite/details/'+mobi.numCandidature+'/'+mobi.numPreference,
				'title' : 'Plus de détails sur cette mobilité'
			}).text('#'+mobi.numCandidature+' - '+mobi.numPreference);
			row.append($('<td>').html($linkDetails));
			row.append($('<td>').html(Gerasmus.Vue.creerLabelSelonEtat(mobi.etat)));
			row.append($('<td>').text(mobi.localite || 'Non défini'));
			row.append($('<td>').text(Gerasmus.Util.formatDate(demande.dateIntroduction)));
			var quadri = ((mobi.quadri === 1) ? '1er' : '2eme');
			row.append($('<td>').text(quadri + ' quadrimestre'));
			var type = mobi.programme.nom + ' - '+ mobi.typeProgramme.nom;
			row.append($('<td>').text(type));
			var $btnsActions = $('<td>');
			if(estProf){
				$btnsActions.append($linkDetails.clone(true).addClass('btn btn-outline btn-success').html('<i class="fa fa-info-circle"></i>'));
			}
			if(!mobi.confirme && !mobi.annule){
				$btnsActions.append($('<a class="btn btn-outline  btn-info">').attr({
					'href':'#/mobilite/maj/'+demande.id+'/'+mobi.numPreference+'/confirmer',
					'title' : 'Confirmer mobilité'
				}).html('<i class="fa fa-check"></i>'));
			}

			$btnsActions.append($('<a class="btn btn-outline btn-warning">').attr({
				'href':'#/mobilite/annuler/'+demande.id+'/'+mobi.numPreference,
				'title' : 'Annuler cette mobilté'
			}).html('<i class="fa fa-times"></i>'));

			row.append($btnsActions);
			$contenu.append(row);
		}
		$tableMobis.footable();
		$tableMobis.trigger('footable_resize');
		return $tableMobis;
	}

	function calcValeurSelonEtat (etat){
		switch(etat){
			case 'INTRO' :
				return 10;
			case 'CONFIRME' :
				return 30;
			case "PREPA" :
				return 50;
			default :
				return 75;

		}
	}


	function creerPanelPourChoixMobilites (mobilite){
		var $panel = $('<dl class="dl-horizontal">');
		var $ligne ;

		var valeur = calcValeurSelonEtat(mobilite.etat);
		var label = (!mobilite.annule)  ? Gerasmus.Vue.determninerTypeSelonEtat(mobilite.etat) : 'danger';
		$ligne = $('<dd>').append($('<label class="progress-bar progress-bar-striped active progress-bar-'+label+'"+>')
							.css('width', valeur +'%')
							.attr({'aria-valuenow': valeur},{'aria-valuemax':100},{'aria-valuemin':0},{'role':'progressbar'})
							.text(mobilite.etat)
				);
		$panel.append($('<dt>').html("Etat : ")).append($ligne);

		var annee_acad = Number(mobilite.demande.anneeAcademique);
		$ligne = $('<dd>').append($('<label>').text(annee_acad + ' - ' + (annee_acad + 1) ));
		$panel.append($('<dt>').html("Année academique : ")).append($ligne);

		$ligne = $('<dd>').append($('<label>').text(mobilite.programme.nom + ' en '+mobilite.typeProgramme.nom));
		$panel.append($('<dt>').html("Type : ")).append($ligne);

		var quadri = ((mobi.quadri === 1) ? '1er' : '2eme');
		$ligne = $('<dd>').append($('<label>').text(quadri));
		$panel.append($('<dt>').html("Quadrimestre : ")).append($ligne);


		var nomPart = 'Non défini';
		if(mobilite.partenaire){
			nomPart = mobilite.partenaire.nomCompvar || mobilite.partenaire.nomLegal ;
		}
		$ligne = $('<dd>').append($('<label>').text(nomPart));
		$panel.append($('<dt>').html("Partenaire : ")).append($ligne);

		$ligne = $('<dd>').append($('<label>').text(mobilite.localite || 'Non défini'));
		$panel.append($('<dt>').html("Destination : ")).append($ligne);


		$ligne = $('<dd>').append($('<label>').text(Gerasmus.Util.formatDate(mobilite.demande.dateIntroduction)));
		$panel.append($('<dt>').html("Introduit le : ")).append($ligne);

		if(mobilite.annule){
			$ligne = $('<dd>').append($('<label>').text(mobilite.motifAnnulation.motif));
			$panel.append($('<dt>').html("Est annulé : ")).append($ligne);
		}

		return $panel;
	}



	function creerPanelPourEncodages(mobilite){
		var $panel = $('<dl class="dl-horizontal">');


		return $panel;
	}

	//TODO
	function creerPanelPourPayement(mobilite){
		var $panel = $('<dl class="dl-horizontal">');



		return $panel;
	}








	mobi.Vue.prototype.ajout = function (){
		var that = this;
		this.afficher('mobilite','ajout', function (done,vue){
			if(!done) return;

			vue.unbind('keyup');

			that.remplirSelectAnneAcademique();

			that._ctrl.getPartenaires();
			that._ctrl.getPays();
			that._ctrl.getProgrammes();
			that._ctrl.getTypesProgramme();

            // Permet le deplacement des box

			$('#mobi-choix-sortable').sortable({
				'handle' : '#mobi-choix-sortable',
				'tolerance' : 'pointer',
				'forcePlaceholderSize': true,
            	'opacity': 0.8
			});


			// Veut suprimer un choix de mobi
			$('.close-link').click(function () {
				if(that.nbChoix > 1){
					$('#modal_confirm_supp_choix').modal('show');
					var $btn = $(this);
					$('#btn_supp_choix_ok').click(function() {
						--that.nbChoix;
						$btn.closest('.choix').fadeOut('fast', function() {
							$(this).remove();
						});
					});
				}
			});

            // Veut rajotuer encore un choix de plus
			$('.btn-plus-choix-mobi').click(function(event) {
				if(that.nbChoix == mobi.Vue.NB_MAX_CHOIX)
					return ; // Max. ke 10 choix en plus de celle deja présent.
				var $choixActu = $('.choix :first');
				var annee_acad = $choixActu.find('select[name="annee_academique"]').val();
				$choixActu.find('select').chosen('destroy').val();

				var $clonedChoix = $choixActu.clone(true,true).addClass('animated fadeIn');
                if($clonedChoix.length > 0){
                	$clonedChoix.find('select[name="annee_academique"]').val(annee_acad);
                    $clonedChoix.find('select').chosen({width: "100%"});
					$choixActu.after($clonedChoix);
					that.nbChoix++;
                }
                $choixActu.find('select').chosen({width: "100%"});
			});

            // Il a fini, il veut mnt sauver ces choix
			$('.btn-choix-ajouter').click(function(e) {
				e.preventDefault();
				if(that._ctrl.isProf()){
					that.ajouter(); // C'est un prof, on ajout direct'
				}else{
				    // Afficher mnt la box de confirmation
					$('#modal_confirm_ajouter_choix').modal('show');
				}

			});

            // Il est sur la box de confirmation,
            // il confirme l'ajout des choix
			$('#btn_choix_ajouter_ok').click(function() {
				that.ajouter();
			});

		});
	};


	mobi.Vue.prototype.ajouter = function(){
		if(this.get$VueActu()==null){
			Gerasmus.Router.redirect('/mobilite/ajout');
			return;
		}
		var demande = {
			'annee_academique': '',
			'nb_choix'	: 0
		};
		var that = this;
		this.get$VueActu().find('form').each(function(idx, form){
			demande[idx] = that.collecterFormulaire($(form));
			demande['annee_academique'] = demande[idx]['annee_academique'];
		});

		demande['nb_choix'] = this.nbChoix+"";

		this._ctrl.ajouterChoixMobilites(demande);
	};

	mobi.Vue.prototype.index = mobi.Vue.prototype.list;
	mobi.Vue.prototype.list = function (){
		var self = this;
		// Ke faire kan je recois la liste des demandes de mobis pour listing
		Events.sub(Events.MOBI_LIST_OK,this, function afficherDemandes(list){
			//console.table(demandes);
			var $vue =  Gerasmus.Context.getVueActu().find('.mobi-list-annee');
			$vue.empty();
			if(Gerasmus.Util.isArrayEmpty(list)){
				$vue.append($('<h2>').text('Aucune mobilité introduite pour l\'année courante dans votre département !'));
				return;
			}

			Object.keys(list).forEach(function(i){
				var demande = list[i];
				if(Array.isArray(demande)){
					$vue.append($('<h2 class="text-left">').text(i));
					demande.forEach(function (dem){
						$vue.append(creerPanelPourDemande(dem,self._ctrl.isProf()).addClass('panel-info'));
					});
				}else{
					$vue.append(creerPanelPourDemande(demande));
					$vue.find('.panel:last').addClass( ((Number(i) % 2) === 0) ? 'panel-info' : 'panel-success');
					$vue.find('.collapse:first').addClass('in');
				}

			});

		});


		this.afficher('mobilite','list', this, function (done,$vue) {
			if(!this.getCtrler().isProf()){
				this.getCtrler().listerDemandes();
				return ;
			}
			this.getCtrler().getDepartements();


			$('.btn-mobi-lister').click(function (){
				var deps = self.get$VueActu().find('select[name="departements_ipl"]').val();
				if(deps){
					self.getCtrler().listerDemandes(deps);
				}else{
					Gerasmus.Vue.notification('info','Veuillez d\'abord selectionner le(s) departement(s) pour lister');
				}
			})
		});


	};



	mobi.Vue.prototype.maj = function(demId,mobPref,type){
		if(!this.get$VueActu() || !demId || !mobPref){
			Gerasmus.Router.redirect('/mobilite/list');
			return;
		}

		//var $modalForMajMobi = $('.modal-maj-mobi');
		Gerasmus.Context.setNomVueFuture('mobilite','maj');
		switch(type){
			case 'confirm':
				this._ctrl.confirmerMobilite(demId,mobPref);
			return;
			case 'docs':

			return;
			case 'logis':
			case 'logiciels':


			return;
			case 'payements':


			return;
		}
		//$modalForMajMobi.find('')
		//$modalForMajMobi.modal('show');


	};

	mobi.Vue.prototype.details = function (demID,mobPref){
		if(!demID){
			Gerasmus.Router.redirect('accueil');
			return;
		}


		Events.sub(Events.MOBI_DETAILS_OK, this,function afficherDetails(mobilites){
			var $vue  = Gerasmus.Context.getVueActu().find('.panel_mobi_details');
			var self = this;

			$vue.empty();

			for(var idx in mobilites){
				var mobi = mobilites[idx];
				var type = (!mobi.annule)  ? Gerasmus.Vue.determninerTypeSelonEtat(mobi.etat) : 'danger';
				var $panel = $('<div class="panel panel-'+type+'">');
				var $panelBody = $('<div class="panel-body">');
				$panelBody.append(creerPanelPourChoixMobilites(mobi));
				if(mobi.confirme){ // Si mobi dja confirme, alors affiche les trucs d'encodage
					$panelBody.append(creerPanelPourEncodages(mobi));
				}

				$panel.append($panelBody);

				if(!mobi.annule){
					var href,$link ;
					var $btnsFooter = $('<div class="row">');
					href = '#/mobilite/maj/'+mobi.id + '/'+mobi.numPreference;

					if(!mobi.confirme){ // Affiche le btn pour confirmer
						if(!mobi.partenaire || self._ctrl.isProf()){
							$link = $('<a class="btn btn-primary pull-left">').attr({
								'href': href +'/confirm',
								'title' : 'Confirmer mobilite'
							}).html('<i class="fa fa-check fa-lg"></i>');
							$btnsFooter.append($('<div class="col-md-2 pull-left">').html($link));
						}
					}else{
						if(this._ctrl.isProf()){
							$link = $('<a class="btn btn-success">').attr({
								'href'	: href+'/logiciels'
								,'title' :'Confirmer encodage logiciel'
							}).html('<i class="fa fa-desktop fa-lg"></i>');
							$btnsFooter.append($('<div class="col-md-2 col-lg-offset-1">').html($link));


							$link = $('<a class="btn btn-success">').attr({
								'href'	: href+'/docs'
								,'title' :'Confirmer signature documents'
							}).html('<i class="fa fa-file-text fa-lg"></i>');
							$btnsFooter.append($('<div class="col-md-2 col-lg-offset-1">').html($link));


							$link = $('<a class="btn btn-info">').attr({
								'href': href+'/payements',
								'title' : 'Indiquer payements'
							}).html('<i class="fa fa-money fa-lg"></i>');
							$btnsFooter.append($('<div class="col-md-2 ">').html($link));
						}
					}



					$link = $('<a class="btn btn-warning pull-right btn-annuler-mobi">').attr({
						'href'	: '#/mobilite/annuler/'+mobi.id + '/'+mobi.numPreference
						,'title' :'Confirmer signature documents'
					}).html('<i class="fa fa-times fa-lg"></i>');
					$btnsFooter.append($('<div class="col-md-2 pull-right">').append($link));

					$panel.append($('<div class="panel-footer">').html($btnsFooter));
				}


				$vue.append($panel);
			}
		});

		this.afficher('mobilite','details',this, function (done, $vue){
			this._ctrl.recupererDetailsMobilites(demID);
		});
	};


	mobi.Vue.prototype.annuler = function(demId,mobPref){
		var self = this;

		var gererAnnuler = function ($modal,demId,mobPref){
			var mobiId = demId;
			var mobiPref = mobPref;

			if(self._ctrl.isProf()){
				self._ctrl.getMotifs();
			}

			$modal.on('hidden.bs.modal', function () {
				Gerasmus.Router.redirect('/mobilite/details/'+mobiId);
			}).modal('show');

			$('.btn_mobi_annul_ok').click(function (e){
				var inputs = self.collecterFormulaire();
				console.table(inputs);
				if(inputs){
					Gerasmus.Context.setNomVueFuture('mobilite','annuler');
					inputs['mobilite_id'] = mobiId;
					inputs['mobilite_preference'] = mobiPref;
					self._ctrl.annulerMobilte(inputs);
					Gerasmus.Router.redirect('/mobilite/list');
				}else{
					Gerasmus.Vue.notification("warning", "Une motivation est obligatoire");
				}
			});
		};



		// Au cas, ou j'arrive ici par un URL et non pas par un bouton
		if(!this.get$VueActu()){
			// Affiche d'abord les details sur la mobilite
			this.details(demId,mobPref);

			// Ke faire si j'ai demandé les details kan il veut annuler
			Events.sub(Events.MOBI_DETAILS_OK, this,function annulerMobilte(){
				var mobiId = demId;
				var mobiPref = mobPref;

				gererAnnuler(Gerasmus.Context.getVueActu().find('#modal_confirm_annul_mobi'),mobiId,mobiPref);

			},true); // TRUE : pour ne pas s'exec chak fois je demande des détails sur une mobi
		}else{
			gererAnnuler(Gerasmus.Context.getVueActu().find('#modal_confirm_annul_mobi'),demId,mobPref);
		}

	};





	return mobi;
})(Gerasmus.Events); // IIFE
