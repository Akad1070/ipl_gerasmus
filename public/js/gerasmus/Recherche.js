
// Recoie en param une instance d'Events qui ne sera accessible ke dans tt Base
// Pour permettre une discussion entre les différentes couches (parties)
var Recherche = (function(Events){

	var search = {};

/*******************************************************************************************
								Partie MODEL
*******************************************************************************************/
	search.Model = function(){
		Base.Model.call(this); // equivaut à super

	};
	search.Model.heriteDe(Base.Model);


	search.Model.prototype.effectuerRecherche = function (inputs){
		this.lancerReqAJAX('POST','',inputs).then(function (response) {
			Events.pub(Events.SEARCH_OK, response);
		})
	};




/*******************************************************************************************
								Partie CONTROLLER
*******************************************************************************************/

	search.Controller = function(mod){
		Base.Controller.call(this,mod); // equivaut à super
		this.search_type= '';
	};
	search.Controller.heriteDe(Base.Controller);

	search.Controller.prototype.effectuerRecherche = function(inputs){
		this._model.effectuerRecherche(inputs);
		this.search_type = inputs.type;
		Events.sub(Events.SEARCH_OK, this, function redirigerResponseSearch (response){
			Gerasmus.Vue.notification('info',response['msg']);
			Events.pub(Events.SEARCH_OK+this.search_type,response['resultat']);
		});
	};




/*******************************************************************************************
								Partie VUE
*******************************************************************************************/

	search.Vue = function (ctrler){
		Base.Vue.call(this,ctrler);
	};
	search.Vue.heriteDe(Base.Vue);



	Events.sub(Events.VUE_CHANGEMENT+'.recherche.user', function configFooTable(){
		Gerasmus.getVueActu().bind('footable_filtering', function (e) {
            var dep = $('#search-filter-user-departement').find(':selected').text();
            if (dep && dep.length > 0 && dep !== 'all') {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + dep : dep;
                e.clear = !e.filter;
            }
        });
	});



	search.Vue.prototype.index = function (search_all_object){
		var self = this;

		this.afficher("recherche",'index',function (done,$vue){
			if(!done) return;

			$vue.find('.search-on').click(function (e){
				var $btn = $(e.target);
				var robjet = $vue.find('#search_all_obj').val();
				if(robjet){
					$('.search-erreur').text('');
					console.debug($btn.attr('href')+robjet);
					Gerasmus.Router.redirect($btn.attr('href')+robjet)
				}else{
					$('.search-erreur').text('Veuillez entrez quelque chose comme Objet de recherche');
				}
			});
		});


	};




	search.Vue.prototype.user = function(){
		var self = this;
		var BTNS_ACTIONS = '<div class="prof-actions"><a class="btn btn-sm btn-primary btn_user_info">Completer informations</a><a class="btn btn-sm btn-warning btn_user_alias">Usurper identité <span> </a></div>';

		this.afficher('recherche','user',this,function(done,$vue){
			if(!done) return;
			self.getCtrler().getDepartements();

			$vue.find('.btn-search').click(function(event) {
				var inputs = self.collecterFormulaire();

				if(!Gerasmus.Util.isEmpty(inputs)){
					$('.search-erreur').text('');
					$('.search-obj').text(inputs.nom + ' ' + inputs.prenom);
					inputs.type = 'user';
					self.getCtrler().effectuerRecherche(inputs);
				}else{
					$('.search-erreur').text('Veuillez entrez quelque chose pour effectuer la recherche');
				}
			});

			$('#search-filter-user-departement').change(function (e) {
				var dep = $(this).find("option:selected").text();
				$('.search-user-result').trigger('footable_filter', {filter: dep});
			});
		});


		Events.sub(Events.SEARCH_USER_OK, function afficherResultat(resultat) {
			var $footable = $(Gerasmus.Context.getVueActu()).find('.footable').data('footable');
			$('.search-user-result').empty(); // Vide la table des resultats
			$('.search-user-nb-result').text( (resultat) ? resultat.length : 0);

			for( var idx in resultat){
				var user = resultat[idx];
				var row = $('<tr>');
				row.append($('<td>').text(user.nom.toUpperCase() +' ' +user.prenom));
				row.append($('<td>').append($('<a>').attr('href','#/user/details/'+user.id).text(user.mail)));
				row.append($('<td>').text(user.departement.nom));
				if(self._ctrl.isProf()){
					var $btn_actions = $(BTNS_ACTIONS);
					$btn_actions.find('.btn_user_alias').attr('href','#/user/ajout/for/user/'+user.id);
					$btn_actions.find('.btn_user_info').attr('href','#/user/details/'+user.id);
					row.append($('<td>').append($btn_actions));
				}
				$footable.appendRow(row);
			}

			$(Gerasmus.Context.getVueActu()).find('.footable').trigger('footable_resize');
		});
	};



	search.Vue.prototype.partenaire = function(){
		var self = this;

		Events.sub(Events.SEARCH_PAT_OK, this, function afficherResultat(resultat) {
			var $result_container = $(Gerasmus.Context.getVueActu()).find('.search-result');
			$result_container.empty();
			var nbResult = (resultat) ? resultat.length : 0;
			$('.search-nb-result').text( nbResult);
			for(var idx in resultat){
				var pat = resultat[idx];
				var adresseComplet = pat['adresse'] + ' ' + pat['codePostal'] + ' à '+ pat['ville'] + ' ('+pat.pays.nom +')';
				var $lien = $('<a>').attr('href', '#/partenaire/details/'+pat.id).text(pat['nomComplet'] || pat['nomLegal']);
				var $res_pat = $('<div class="well">').append($('<h3 class="result-pat-nom">').append($lien));
				$res_pat.append($('<div class="result-pat-adresse">').text(adresseComplet));
				$result_container.append(($('<div class="col-md-5">').append($res_pat)));
			}
		});


		this.afficher('recherche','partenaire', function afficherResultat(done,$vue){
			if(!done) return;

			self.getCtrler().getPays();

			$vue.find('.btn-search').click(function(event) {
				var inputs = self.collecterFormulaire();

				if(!Gerasmus.Util.isEmpty(inputs)){
					$('.search-erreur').text('');
					$('.search-obj').text(inputs.nom_legal + ' ' + inputs.pays+ ' ' + inputs.ville);
					inputs.type = 'partenaire';
					self.getCtrler().effectuerRecherche(inputs);
				}else{
					$('.search-erreur').text('Veuillez entrez quelque chose pour effectuer la recherche');
				}
			});
		});

	};




	search.Vue.prototype.mobilite = function(){
		var self = this;

		var BTNS_ACTIONS = '<div class="prof-actions">'+
									'<a class="btn btn-sm btn-primary btn_mobi_details">Details</a>'+
									'<a class="btn btn-sm btn-info btn_mobi_confirm_logs">Confirmer encodage logiciel</a>'+
									'<a class="btn btn-sm btn-info btn_mobi_confirm_docs">Confirmer signature documents</a>'+
									'<a class="btn btn-sm btn-info btn_mobi_indiquer_payement">Indiquer payement</a>'+
									'<a class="btn btn-sm btn-warning btn_mobi_annuler pull-right">Annuler</a>'+
							'</div>';

		Events.sub(Events.SEARCH_MOBI_OK, this, function afficherResultat(resultat) {
			var self = this;
			var $footable = $(Gerasmus.Context.getVueActu()).find('.footable').data('footable');
			$('.search-mobi-result').empty();
			$('.search-nb-result').text( (resultat) ? resultat.length : 0);

			for( var idx in resultat){
				var mobi = resultat[idx];
				// console.debug(mobi);
				var row = $('<tr>');
				row.append($('<td>').text(mobi.demande.anneeAcademique));
				var etud = mobi.demande.etudiant;
				row.append($('<td>').text(etud.nom.toUpperCase() +' ' + etud.prenom));
				row.append($('<td>').text(etud.departement.nom));
				row.append($('<td>').text(mobi.programme.nom));
				row.append($('<td>').text(mobi.typeProgramme.nom));
				var nomPart = "Non défini";
				if(mobi.partenaire){
					nomPart = mobi.partenaire.nomCompvar || mobi.partenaire.nomLegal ;
				}
				row.append($('<td>').text(nomPart));
				row.append($('<td >').append($(Gerasmus.Vue.creerLabelSelonEtat(mobi.etat))));
				if(this._ctrl.isProf() && !mobi.annule){
					var $btn_actions = $(BTNS_ACTIONS);
					$btn_actions.find('.btn_mobi_details').attr('href','#/mobilite/details/'+mobi.id+'/'+mobi.numPreference);
					$btn_actions.find('.btn_mobi_confirm_docs').attr('href','#/mobilite/maj/'+mobi.id+'/'+mobi.numPreference+'/docs');
					$btn_actions.find('.btn_mobi_confirm_logs').attr('href','#/mobilite/maj/'+mobi.id+'/'+mobi.numPreference+'/logiciels');
					$btn_actions.find('.btn_mobi_indiquer_payement').attr('href','#/mobilite/maj/'+mobi.id+'/'+mobi.numPreference+'/payements');
					$btn_actions.find('.btn_mobi_annuler').attr('href','#/mobilite/annuler/'+mobi.id+'/'+mobi.numPreference);
					row.append($('<td>').append($btn_actions));
				}
				$footable.appendRow(row);
			}
			$(Gerasmus.Context.getVueActu()).find('.footable').trigger('footable_resize');
		});


		this.afficher('recherche','mobilite', self, function afficherResultat(done,$vue){
			if(!done) return;

			this._ctrl.getAnnesAcademique();
			this._ctrl.getEtatsMobilite();
			this._ctrl.getDepartements();

			$vue.find('.btn-search').click(function(event) {
				var inputs = self.collecterFormulaire();

				if(!Gerasmus.Util.isEmpty(inputs)){
					$('.search-erreur').text('');
					$('.search-obj').text(inputs.mobi_annee_academique + ' ' + inputs.mobi_etat);
					inputs.type = 'mobilite';
					self.getCtrler().effectuerRecherche(inputs);
				}else{
					$('.search-erreur').text('Veuillez entrez quelque chose pour effectuer la recherche');
				}
			});
		});

	};




	return search;

})(Gerasmus.Events); // IIFE
