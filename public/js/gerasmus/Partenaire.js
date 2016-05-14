
// Recoie en param une instance d'Events qui ne sera accessible ke dans tt Base
// Pour permettre une discussion entre les différentes couches (parties)
var Partenaire = (function(Events){

	var pat = {};

	Events.PART_AJOUT_OK 	= 'gerasmus.partenaire.ajouté';
	Events.PART_DETAILS		= 'gerasmus.partenaire.details';
	Events.PART_MAJ			= 'gerasmus.partenaire.maj';



/*******************************************************************************************
								Partie MODEL
*******************************************************************************************/
	pat.Model = function(){
		Base.Model.call(this); // equivaut à super
	};
	pat.Model.heriteDe(Base.Model);


	pat.Model.prototype.ajouterPartenaire = function(inputs){
		this.lancerReqAJAX('POST',null,inputs)
            .then(function (response){
                Events.pub(Events.PAT_AJOUT_OK,response);
            });
	};


	pat.Model.prototype.getPartenaireDetails = function(data){
		this.lancerReqAJAX('POST',null,data).then(function (response) {
			Events.pub(Events.PAT_DETAILS,response['partenaire']);
		});
	};

	pat.Model.prototype.listerPartenaires = function(){
		this.lancerReqAJAX('POST').then(function (response) {
			Events.pub(Events.PAT_LIST,response['partenaires']);
		});
	};





/*******************************************************************************************
								Partie CONTROLLER
*******************************************************************************************/

	pat.Controller = function(model){
		Base.Controller.call(this,model); // equivaut à super
	};
	pat.Controller.heriteDe(Base.Controller);




	pat.Controller.prototype.ajouterPartenaire = function(data){
		this._model.ajouterPartenaire(data);
		Events.sub(Events.PAT_AJOUT_OK, function afterAjoutPartenaire(resp){
			Gerasmus.Router.redirect('/partenaire/list');
			Gerasmus.Vue.notification('success',resp['msg']);
		});
	};


	pat.Controller.prototype.getPartenaireDetails = function(pid){
		this._model.getPartenaireDetails({'pat_id':pid});

	};



	pat.Controller.prototype.listerPartenaires = function(){
		this._model.listerPartenaires();

	};





/*******************************************************************************************
								Partie VUE
*******************************************************************************************/

	pat.Vue = function (ctrler){
		Base.Vue.call(this,ctrler);
	};
	pat.Vue.heriteDe(Base.Vue);

	function creerPartenaireCard (partenaire){
		var CARD = '<div class="contact-box panel">'+
						'<a class="pat-details-link" title="Clic pour plus de détails" >'+
							'<div class="col-lg-12">'+
								'<span class="pull-right pat-visibilite" title="Visible pour tous">'+
								'<i class="fa fa-eye"></i>'+
								'</span>'+
								'<h3><strong class="pat-nom"></strong></h3>'+
								'<p class="departements-ipl"></p>'+
								'<address class="pat-contact">'+
									'<i class="fa fa-home"></i> <span class="pat-adresse"></span><br>'+
									'<i class="fa fa-map-marker"></i> <span class="pat-ville"> </span>&nbsp;(<strong class="pat-pays"> </strong>) <br>'+
									'<abbr title="Mail">@:</abbr> <span class="pat-mail"></span><br>'+
								'</address>'+
							'</div>'+
							'<div class="clearfix"></div>'+
						'</a></div>';
		var $card = $(CARD);
		if(!partenaire.visible){
			$card.find('.pat-visibilite').attr('title','Invisible - Non selectionnable').html('<i class="fa fa-eye-slash"></i>');
		}
		$card.find('.pat-details-link').attr('href','#/partenaire/details/'+partenaire.id);
		$card.find('.pat-nom').text(partenaire.nomCompvar || partenaire.nomLegal);
		for(var idx in partenaire.iplDepartements){
			var dep = partenaire.iplDepartements[idx];
			var label = Gerasmus.Vue.creerLabelSelonCodeDepartement(dep.code,dep.nom);
			$card.find('.departements-ipl').append($(label));
		}
		$card.find('.pat-adresse').text(partenaire.adresse || 'Non défini');
		$card.find('.pat-ville').text(partenaire.ville || 'Non défini');
		$card.find('.pat-pays').text(partenaire.pays.nom);
		$card.find('.pat-mail').text(partenaire.mail);
		if(partenaire.telephone){
			$card.find('.pat-contact').append($('<abbr title="Tel"  class="fa fa-phone"> </abbr> '+ partenaire.telephone +'<br>'));
		}
		if(partenaire.siteWeb){
			$card.find('.pat-contact').append($('<abbr title="Site WEB" class="fa fa-globe"></abbr> '+ partenaire.siteWeb +'<br>'));
		}

		//console.table(partenaire);
		return $card;
	}



	pat.Vue.prototype.ajout = function() {
		this.afficher('partenaire','ajout',this,function(done,$vue){
			if(!done) return;

			var self = this;

			this.getCtrler().getDepartements();
//			Events.pub('gerasmus.partenaire.departement',{});
			this.getCtrler().getOrganisations();
			this.getCtrler().getPays();

			$vue.find('.btn-partenaire-ajouter').click(function(e) {
				var inputs = self.collecterFormulaire();
				if(inputs){
					console.debug(inputs);
					self._ctrl.ajouterPartenaire(inputs);
					Events.sub(Events.PAT_AJOUT_OK,self, function afterAjoutPartenaire2(response){
						self.viderFormulaire();
						self.getCtrler().listerPartenaires()
					});
				}
			});
		});
	};


	pat.Vue.prototype.details = function (pid) {
		this.afficher('partenaire','details');

		Events.sub(Events.PART_DETAILS,this, function afficherDetails(partenaire){
			this.get$VueActu().find(".pat-departements").empty();
			var $list_dep = this.get$VueActu().find(".pat-departements");
			for(var idx in partenaire.iplDepartements){
				var dep = partenaire.iplDepartements[idx];
				var label = Gerasmus.Vue.creerLabelSelonCodeDepartement(dep.code,dep.nom);
				$list_dep.append($(label).addClass("label label-primary")).append('&nbsp;');
			}
			if(partenaire.visible){
				this.get$VueActu().find(".btn-maj-part-visible").html('<i class="fa fa-eye-slash"></i>');
			}else{
				this.get$VueActu().find(".btn-maj-part-visible").html('<i class="fa fa-eye"></i>');
			}
			this.get$VueActu().find(".pat-mail").attr('href', 'mailto:'+partenaire.mail).text(partenaire.mail);
			this.get$VueActu().find(".pat-nom").text(partenaire.nomCompvar || partenaire.nomLegal);
			this.get$VueActu().find(".pat-type").text(partenaire.typeOrganisation || 'Non défini');
			this.get$VueActu().find(".pat-adresse").text(partenaire.adresse || 'Non défini');
			if(partenaire.region){
				this.get$VueActu().find(".pat-region").html( 'à&nbsp;'+partenaire.region);
			}
			this.get$VueActu().find(".pat-ville").text(partenaire.ville || 'Non défini');
			this.get$VueActu().find(".pat-pays").html($('<b>').text(partenaire.pays.nom));
			this.get$VueActu().find(".pat-tel").text(partenaire.telephone || 'Non défini');

			var $lien = this.get$VueActu().find(".pat-site-web");
			if(partenaire.siteWeb){
				$lien.attr('href',partenaire.siteWeb);
			}

			$lien = this.get$VueActu().find(".pat-createur");
			if(partenaire.createur){
				$lien.text(partenaire.createur.nom +  partenaire.createur.prenom);
				$lien.attr('href', '#/user/details/'+ partenaire.createur.id);
			}else{
				$lien.replaceWith("<b>Partenaire elligible !<b>");
			}
		});

		this._ctrl.getPartenaireDetails(pid);

	};


	pat.Vue.prototype.list = function (){

		Events.sub(Events.PAT_LIST, function afficherListingPartenaires(partenaires){
			var $vue = Gerasmus.Context.getVueActu().find('.part-list-dep');
			$vue.empty();
			if(Gerasmus.Util.isEmpty(partenaires)){
				$vue.append($('<h1 class="text-center">').text('Aucun partenaire présents par départements'));
				return;
			}

			for(var idx in partenaires){
				var $card = $('<div class="col-md-5">').append(creerPartenaireCard(partenaires[idx]));
				$vue.append($card)
			}
		});

		this.afficher('partenaire','list',this,function (){
			this._ctrl.listerPartenaires();
		});
	};


	pat.Vue.prototype.maj = function (pid) {


	};





	return pat;

})(Gerasmus.Events); // IIFE
