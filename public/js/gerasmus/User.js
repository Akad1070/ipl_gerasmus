
// Recoie en param une instance d'Events qui ne sera accessible ke dans tt Base
// Pour permettre une discussion entre les différentes couches (parties)
var User = (function(Events){

	var user = {};

/*******************************************************************************************
								Partie MODEL
*******************************************************************************************/
	user.Model = function(){
		Base.Model.call(this); // equivaut à super

	};
	user.Model.heriteDe(Base.Model);


    user.Model.prototype.getUserInfos = function (data){
        this.lancerReqAJAX('POST',null,data).then(function (response){
            var evtName = (data && data.user_id) ? Events.USER_DETAILS : Events.USER_DETAILS_PERSO;
            response.user.type = response['user.type'];
            Events.pub(Events.USER_DETAILS,response['user'],response['infos'],response['mobilites']);
        });
    };





/*******************************************************************************************
								Partie CONTROLLER
*******************************************************************************************/

	user.Controller = function(mod){
		Base.Controller.call(this,mod); // equivaut à super
	};
	user.Controller.heriteDe(Base.Controller);

	user.Controller.prototype.getUserInfos = function (uid){
		this._model.getUserInfos({'user_id':uid});
	};






/*******************************************************************************************
								Partie VUE
*******************************************************************************************/

	user.Vue = function (ctrler){
		Base.Vue.call(this,ctrler);
	};
	user.Vue.heriteDe(Base.Vue);




	function remplirPanelUser(user){
		var $panel = Gerasmus.Context.getVue("user","details");
		var type = ((user.prof) ? 'Professeur' : 'Etudiant');
		var label = Gerasmus.Vue.creerLabelSelonTypeUser(user.type);
		$panel.find('.user-type').html(label);
        $panel.find('.user-prenom').text(user.prenom);
        $panel.find('.user-nom').text(user.nom.toUpperCase());
        $panel.find('.user-pseudo').text(user.pseudo);
        $panel.find('.user-mail').text(user.mail).attr('href','mailto:'+user.mail);
        label = Gerasmus.Vue.creerLabelSelonCodeDepartement(user.departement.code,user.departement.nom);
        $panel.find('.user-dep').html(label);
        var dte_inscrip = Gerasmus.Util.formatDate(user.dateInscription);
        $panel.find('.user-date-inscrip').text(dte_inscrip);
	}


	function remplirTabInfos(user_infos){
		//var btn_completer = '<a href="#" data-toggle="modal" class="btn btn-outline btn-info btn-xs pull-right">Completer</a>';
		var $vue_infos = Gerasmus.Context.getVueActu().find('#tab-details-infos');

		$vue_infos.find('[name="civilite"]').val(user_infos.civilite);
		$vue_infos.find('[name="civilite"]').trigger("chosen:updated");

		$vue_infos.find('.info-nation').val(user_infos.nationalite);
		$vue_infos.find('.info-adresse').val(user_infos.adresse);
		$vue_infos.find('.info-tel').val(user_infos.tel);
		$vue_infos.find('.info-mail').val(user_infos.email);
		$vue_infos.find('.info-bank').val(user_infos.compteBancaire);
		$vue_infos.find('.info-bank_titu').val(user_infos.titulaire);
		$vue_infos.find('.info-bank_nom').val(user_infos.banque);
		$vue_infos.find('.info-code_bic').val(user_infos.bic);

		$vue_infos.find('.info-nb_reussi').val(user_infos.nombreAnneeReussie);
		$vue_infos.find('.info-nation').val(user_infos.nationalite);
		$vue_infos.find('.info-nation').val(user_infos.nationalite);

		var $tabInfos = Gerasmus.Context.getVue("user","details").find("#tab-details-infos");
		//$tabInfos.empty().append($(btn_completer));
		$tabInfos.append($vue_infos);
	}


	function remplirTabDemandes (user_demandes){
		console.debug(user_demandes);
		//var $vueDemandes = Gerasmus.Context.getVue('user','details').find('#tab-details-demandes');
	}


	user.Vue.prototype.index	= user.Vue.prototype.details;
	user.Vue.prototype.details 	= function (uid){
		var that = this;

		this.afficher("user","details",function (done,$vue) {
			if(!done) return;

			Events.sub(Events.USER_DETAILS,this, function gererDetailsUser(user,user_infos,user_demandes){
				remplirPanelUser(user);

				// Si c'est pas son profil && le details est sur un prof
				if(!that._ctrl.isCurrentUser(user.id) && user.prof){
					$('.btn-user-info-completer').hide();
				}else{
					$('.btn-user-info-completer , .btn-user-info-completer-ok').show().attr('href', '#/user/completer/'+user.id);;

				}
				// Check si ce profil est pr 1 prof
				if(user.prof){
					$('.btn-as-user').hide();
					return;
				}

				if(user_infos){
					remplirTabInfos(user_infos);
				}
				if(user_demandes){
					remplirTabDemandes(user_demandes);
				}

				$('.btn-as-user').click(function (evt){
					console.debug("As "+ user.nom);
				}).show();

				// Chope la vue pr completerInfo
				var $modalUserInfo = Gerasmus.Context.getVue('user','completer');
				// Chope la tab pour les info de l'User
				var $tabInfos = $vue.find("#tab-details-user-infos");
				// Affiche la vue et le rajoute dans la tab
				//$tabInfos.empty();	
				$tabInfos.append($modalUserInfo.show().find('.panel-body').clone(true));


			});// Sub pr Events
		}); // afficher vue

		this.getCtrler().getUserInfos(uid);
	};


	user.Vue.prototype.completer = function(uid){
		var that = this;
		if(!this.get$VueActu()){
			Gerasmus.Router.redirect('/user/details/'+uid);
			return ;
		}
		var userInfos = this.collecterFormulaire();
		var $vue = Gerasmus.Context.getVue('user','completer');
		$vue.find('input').removeAttr('readonly');

		$vue.find('input, select').each(function (idx,input) {
			$(this).val(userInfos[input.name]);
		});
		$vue.find('.modal').on('hidden.bs.modal', function () {
			Gerasmus.Router.redirect('/user/details/'+uid);
		}).modal('show');

		$('.btn-user-info-completer-ok').click(function(evtName) {
			userInfos = that.collecterFormulaire($('.form-user-info'));
			if(userInfos){
				console.table(userInfos);
			}else{
				Gerasmus.Vue.notification('warning','Veuillez completer quelque chose');
			}
		});






	};



	user.Vue.prototype.avatar = function majAvatar (uid){
		/*
		function getBase64Image(imgProfil) {
		    var canvas = document.createElement("canvas");
		    canvas.width = imgProfil.clientWidth;
		    canvas.height = imgProfil.clientHeight;
		    var ctx = canvas.getContext("2d");
		    ctx.drawImage(imgProfil, 0, 0);
		    var dataURL = canvas.toDataURL("image/png");
		    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
		}
		*/
	};






	return user;

})(Gerasmus.Events); // IIFE
