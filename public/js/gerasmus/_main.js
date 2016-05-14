
$(function() {
	'use strict';
	// J'initialise le Context avec le nom de l'app, l'url du serveur, etc ...
	Gerasmus.Context.init({
		'appNom':'Gerasmus',
		'url':'api/gerasmus/v1',
		'container' :$('.wrapper-content'),
		'menu' :$('.menu-lateral'),
		'navBar':$('.breadcrumb'),
		'reqAJAX': {
			'waitTimeLimit'	: 700,
			'onStart' 	: function(msg){
				console.log('ReqAJAX : ' + msg)
				//$('.modal-req-loader').modal('show');
				//$('.req-spinner').show();
			},
			'onComplete': function(){
				//$('.modal-req-loader').modal('hide');
				//$('.req-spinner').hide();
			}
		}
	});


	// Je cree ma BaseVue ki a besoin d'un BaseCtrler et lui-même d'un BaseModel
	var ctrl = new Quidam.Controller(new Quidam.Model());
	var quidamVue = new Quidam.Vue(ctrl);

	// Rajoute toutes les routes et la vue Responsable de cette route
	// Possible de marquer des params pour cette route /user/:uid
	//	Ou :uid serait l'id de l'user  ou /user/:upseudo pour son pseudo
	Gerasmus.Router.when('/quidam/connexion',quidamVue,'connexion');
	Gerasmus.Router.when('/quidam/inscription',quidamVue,'inscription');
	Gerasmus.Router.when(['/quidam/connecter','/quidam/inscrire'],quidamVue);
	Gerasmus.Router.when(['/quidam/out','/quidam/deconnecter'],quidamVue,quidamVue.deconnecter,'out');
	Gerasmus.Router.when([' ','/base/','/base/accueil'],quidamVue,quidamVue.accueil,'accueil')

	Gerasmus.Router.connect('/','/base/accueil');
	Gerasmus.Router.connect('deconnecter','/quidam/out');
	Gerasmus.Router.connect('connecter','/quidam/connecter');
	Gerasmus.Router.connect('inscrire','/quidam/inscrire');

	/***************** Pour toutes les routes concernant la vue Utilisateur ********************/
	var userVue = new User.Vue(new User.Controller(new User.Model()));

	Gerasmus.Router.when('/user/',userVue, userVue.details,'profil');
	Gerasmus.Router.when('/user/details/:uid',userVue, userVue.details);
	Gerasmus.Router.when('/user/completer/:uid',userVue,userVue.completer);
	Gerasmus.Router.when('/user/avatar/:uid',userVue, userVue.avatar);


	/***************** Pour toutes les routes concernant la vue Recherche ********************/
	var searchV = new Recherche.Vue(new Recherche.Controller(new Recherche.Model()));

	Gerasmus.Router.when(['/recherche/all', '/recherche/'],searchV.index,'recherche');
	Gerasmus.Router.when(['/recherche/user', '/recherche/mobilite', '/recherche/partenaire'],searchV);


	/***************** Pour toutes les routes menant vers la Vue Partenaire ********************/
	ctrl = new Partenaire.Controller(new Partenaire.Model());
	var patVue = new Partenaire.Vue(ctrl);

	Gerasmus.Router.when(['/partenaire/','/partenaire/list','/partenaire/ajout','/partenaire/details/:pid'],patVue);
	Gerasmus.Router.when(['/partenaire/maj/:pid'],patVue);


	/***************** Toutes les routes pour la Vue Mobilite ********************/
	var mobiV = new Mobilite.Vue(new Mobilite.Controller(new Mobilite.Model()));

	Gerasmus.Router.when('/mobilite/',mobiV);
	Gerasmus.Router.when(['/mobilite/list','/mobilite/ajout','/mobilite/ajouter','/mobilite/annuler/:demid/:mobinum'],mobiV);
	Gerasmus.Router.when(['/mobilite/details/:demid','/mobilite/details/:demid/:mobinum'],mobiV,mobiV.details);
	Gerasmus.Router.when(['/mobilite/maj/:demid','/mobilite/maj/:demid/:mobinum','/mobilite/maj/:demid/:mobinum/:type'],mobiV);

	//Gerasmus.Router.connect('mobi/:muid',new Gerasmus.Route())



	// Mnt, lancement du l'app :-p
	Gerasmus.Router.init();

});