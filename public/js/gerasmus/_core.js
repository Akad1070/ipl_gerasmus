
var Gerasmus = (function(){

	// Cree namespace principal
	var gerasmus = {
		'Util' 		: {},
		'Route'		: {},
		'Router'	: {},
		'Events'	: {},
		'Context'	: {},
		'Model'		: {},
		'Vue'		: {},
		'Controller': {}
	};


    // Je n'ai pas trouvé de meilleur moyen plus simple de faire l'héritage
    // sans toucher au prototype de function..

	/**
	 * Mecanisme d'heritage d'un prototype dans une autre.
	 *
	 * @param {function} classParent Constructeur function de qui on herite (Animal).
	 */
	Function.prototype.heriteDe = function (classParent) {
		this.prototype = Object.create(classParent.prototype);
		this.prototype.constructor = this;
		this.fusion(this.prototype,classParent.prototype);
	};


	// Permet de 'mixer' tous les functions presentes dans src -> dest
	Function.prototype.fusion = function (dest, src){
		var names = Object.keys(src);
		// Je foreach la dest ki recoit la src
		for(var idx in names){
			var name = names[idx];
			// ? Check si c'est bien fonction
			if(src.hasOwnProperty(name)	){
				dest[name] = src[name];
			}
		}

		// Je passe les prop de dest dans la fct src
		return dest;
	};



	function objIsEmpty (obj){
		return (Object.keys(obj).length === 0);
	}

	gerasmus.Util.isEmpty = objIsEmpty;


	gerasmus.Util.isArrayEmpty = function (arr){
		return (!arr || (Array.isArray(arr) && arr.length === 0) );
	};



	gerasmus.Util.formatDate  = function(dte){
		return dte.dayOfMonth+'-' + dte.monthValue + '-' + dte.year;
	};








/***********************************************************************************
		Router pour rediriger selon l'url ou windows.location
************************************************************************************/


	gerasmus.Route = function (pattern){
		// Si y'as un espace au début ou fin
		// if(pattern)	pattern = pattern.trim();
		this._route = {
			'pattern'	: {	'url' : pattern.trim(), 'regEx' : null },
			'vue'		: '',
			'action'	: ''
			// 'params'	: []
		};

		this.convertToRegex();
	};

	gerasmus.Route.prototype = {
		getChemin 	: function (){ 	return this._route['pattern']['url']; },
		getRegex 	: function (){ 	return this._route['pattern']['regEx']; },
		getNomVue	: function (){ 	return this._route['vue'];},
		getNomAction: function (){ 	return this._route['action'];},
		// getParams	: function (){ 	return this._route['params'];},
		isEquals	: function (rt){ return this.getChemin()=== rt.getChemin();}	,

		setVue	: function (vue){ this._route['vue'] = vue;},
		setAction	: function (act){ this._route['action'] = act;},
		// setParams	: function (param){ this._route['params'] = param;},

		convertToRegex : function() {
			// Je remplace dans '/mobi/:muid' le :muid par un regex
			var reg = this.getChemin().replace(/:[^\s/]+/g, '([\\w-]+)');
			this._route['pattern']['regEx'] = new RegExp(reg);
			this.getChemin().replace(/\/$/, '').replace(/^\//, '');
			var decomp = this.getChemin().split('/');

			decomp.shift();
			this.setVue(decomp.shift() || 'base');
			this.setAction(decomp.shift() || 'index');
		}
	};



	/**
	 *  S'occupe du traitement aek l'URL
	 * 	Permet de redirect et determiner
	 * 	les vues et les actions à init.
	 */
	gerasmus.Router = (function (){
		// Savoir si le navigateur support HTML5 et l'history
		var _supHisto = (window.history && window.history.pushState);

		// Les routes enregistrées sont 'private'
		// Structure de _routes [ nomVue -> { action -> {route,vue} }]
		var _routes = {};

		var _actu	= {'vue' : 'base', 'action' : 'index'};

		// Sert à faire correspondre une route vers une autre
		// 'profil' --> /user/:uid
		var _alias = {};

		var _isRedirected = false;


		function CheckUrl (url){	}


		return {
			'supportHistorik' : _supHisto,

			getActionActu : function(){
				return _actu['action'];
			},

			getVueActu : function(){
				return _actu['vue'];
			},

			setVueActu : function (vue){
				_actu['vue'] = vue;
			},

			setActionActu : function(action){
				_actu['action'] = action;
			},

			ajouterRoute : function(rt,context, vue,titre) {
				// Insertion : [ nomVue -> { action -> {'route','vue','titre'} }]
				if(!_routes[rt.getNomVue()]){ // ?Y'as aucune Vue rajouté pour cette Route.
					_routes[rt.getNomVue()] = {};
					_routes[rt.getNomVue()]['_context'] = {};
				}
				// Rajoute le context pour cette vue si absent,
				// Tt les actions de cette Vue partagent le meme Context
				if(objIsEmpty(_routes[rt.getNomVue()]['_context']) && !objIsEmpty(context) ){
					_routes[rt.getNomVue()]['_context'] = context;
				}

				var tabActionRoutes = _routes[rt.getNomVue()], nomAction = rt.getNomAction();
				// Pas definis une route d'action pour ce type de vue
				if(!tabActionRoutes[nomAction]){
					tabActionRoutes[nomAction] = {};
					tabActionRoutes[nomAction]['routes'] = [];
					tabActionRoutes[nomAction]['routes'].push(rt);
					tabActionRoutes[nomAction]['vue'] = vue;

					// Si j'ai un titre pour cette Route, je m'en sert comme alias
					// Ex : Une Route nommé(/quidam/connexion) nommée connexion
					// Si 'lURL dans le browser est /connexion, alors je dois pointer vers /quidam/connexin
					if(titre){
						this.connect(titre,rt.getChemin());
					}
				}else{
					//
					for(var idx in tabActionRoutes[nomAction]['routes']){
						var actionRoute = tabActionRoutes[nomAction]['routes'][idx];
						if(!actionRoute.isEquals(rt)){
							tabActionRoutes[nomAction]['routes'].push(rt);
						}
					}
				}
			},


			/**
			 *Permet de connecter un alias à un url existant (/profil,/user/:uid)
			 */
			connect : function (alias,to){
				if(_alias[alias]) return ; // J'ai deja un alias
				if(typeof to === 'string'){ // ? C'est un URL ? /deconnecter
					//rt = new gerasmus.Route(to);
					_alias[alias] = to;
				}else{ // to est une Route
					var connectTo = { 'vue' : to.getNomVue(),'act':to.getNomAction(), 'url':to.getChemin() };
					_alias[alias] = connectTo.join('.');
				}
			},


			init : function (){
				// Au changement de l'URL dans la barre d'adresse
				window.addEventListener("hashchange", gerasmus.Router.go, false);

				window.addEventListener('popstate',gerasmus.Router.naviguer)

				this.go();
			},


			/**
			 * ? A koi correspond cette route ? :  A une route correspond une vue particulière.
			 * ('/mobilite/ajouter/:idEtud', MobiliteVue)  :idEtud est le parametre
			 * Alors j'appelerais MobiliteVue.ajouter(:idEtud)
			 */
			when : function(chemin,context,vue,titre){
				var self = this;
				// Aucun vue, donc la vue est à la place du context;
				if(!vue){
					vue = context;
				}else{
					if(typeof vue === 'string'){
						titre = vue;
						vue = context;
					}
				}

				// ? Just un seul chemin ?
				if( typeof chemin === 'string'){
					// Je transforme en Route et je l'ajoute direct
					return this.ajouterRoute(new gerasmus.Route(chemin),context,vue,titre);
				}

				// ? Est-ce un tableau de chemin ?
				if(Array.isArray(chemin)){
					// Chope chak elt et re-check Chemin
					return chemin.forEach(function (elt){
						console.debug('Chemin : '+elt);
						// Je re-when le chemin
						self.when(elt,context,vue,titre);
					});
				}

			},


			redirect : function(nvUrl){
				if(nvUrl && typeof nvUrl === 'string' && _alias[nvUrl]){
					nvUrl = _alias[nvUrl];
				}
				_isRedirected = true;
				 window.location.hash =  nvUrl;
			},


			/**
			 * Appeler lors du chemin de location soit par lien ou
			 */
			go : function (evt){
				var urlSrc = window.location.hash;
				if(evt && typeof evt === 'string'){
					urlSrc = evt;
				}

				var match = urlSrc.match(/\#(.*)$/);
		        var url = '/'; // Par-defaut, on affiche l'accueil

		        if(match && match.length > 1){
			       	url = match.pop();
					// Nettoie l'url , enleve les / de trop :-)
					url.replace(/\/$/, '').replace(/^\//, '');
		        }

		        if(_alias[url]){
					url = _alias[url];
				}

				var routeUrl 	= new gerasmus.Route(url),
					nomVue 	 	= routeUrl.getNomVue(),
					nomAction	= routeUrl.getNomAction();

				// ? Y'a une route correspondant à cet URL ?
				if(_routes[nomVue] && _routes[nomVue][nomAction] ){
					var route = null,params;
					var rtChemin = _routes[nomVue][nomAction];

					for (var i = rtChemin['routes'].length - 1; i >= 0; --i) {
						var actionRoute = rtChemin['routes'][i];
						if(actionRoute.getRegex().test(url)){
							route = actionRoute;
							// Je chope les params de url
							params = actionRoute.getRegex().exec(url);
							params.shift(); // Degage le 1er elt, l'url
							break;
						}
					}

					if(!route){
						gerasmus.Router.redirect('/');
					}

					var routeFunc = null, // Pr choper la fonction à appeler
						context = _routes[nomVue]['_context'], // Le context pour cette Vue
						vue = rtChemin['vue'];  // La vue ou foncttion pour ce url


					if(typeof vue === 'function'){ // Aucune Vue n'a été passé pour ce chemin,
						routeFunc = vue;// juste une fonction et son context.
					}else{	// Va cherche la fonction correspondande dans l'oBjet de Vue
						// Si y'as pas dans cette, va chercher dans le ctrler de cette vue
						routeFunc = vue[nomAction] || vue['_ctrl'][nomAction];
					}

					// Mnt, j'apelle la fonction avec les eventuelles params du chemin
					if(routeFunc){
						gerasmus.Router.setActionActu(nomAction);
						gerasmus.Router.setVueActu(nomVue);
						routeFunc.apply(context, params);
						_isRedirected = false;
						return true;
					}else{ // Y'as pas de fct defnir pour gerer cette route.
						// ? Y'as t-il un context defini ki extends Gerasmus.Vue
						if(!objIsEmpty(context) && context['afficherVue'] ){
							if(context['afficherVue'].call(context,nomVue,nomAction)){
								_isRedirected = false;
								return true; // Si j'ai pu changer la vue'
							}
						}
					}

				}



				// Aucun route n'a été prédéfini pour ce chemin -> Go accueil
				gerasmus.Router.redirect('/');
				return false;

			} // go()



		}; // return

	})();	//  IIFE pour n' avoir k'un seul Router





	/****************************************************************************************************
		*********************************************************************************************

			Context : Sert à centraliser et rendre accessible des données communes à chaque couche
			du nameSpace

		*********************************************************************************************
	****************************************************************************************************/

	gerasmus.Context = (function(){
		var _defaults	 = {
			'appNom' 		: 'Gerasmus',
			'serveurUrl'	: 'main',
			'reqAJAX'		: {
				'timeLimit'	: 1,
				'onStart' 	: function(msg){
					console.log('Requete AJAX LAncé – ' + msg);
				},
				'onComplete': function(){
					console.log('Requete AJAX Fini');
				}
			}

		};
		var _internals = {
			'appNom'		: '',
			'serveurUrl'	: '' ,
			'menu' : {
				'type'		: 'quidam',
				'actu'		: null // Le menu en cours d'affichage. Commune a toutes les vues
			},
			'vue' : {
				'actionActu': null,	// L'action actuellement realisée sur cette vue.
									// N'est valablement que pour les vues composées
				'actionFuture': null,	// L'action futur, peut-etre null
				'nomActu'	: null, // Le nom de la vue actuellement en cours de visu.
				'nomFuture'	: null, // Le nom de la vue prochain, peut être == au nomActu
				'actu'		: null  // La $(vue) actuellement en cours de visu
			},

			'vues' :{
				'dispo' 	: {	},	// Le nom des vues navigables actu : /partenaire/ajouter /login
				'container'	: null, // Le .container general ne risque pas de changer
				'partials'	: {}, 	// Les différentes vues présentes actuellement dans le .container
										// Varie selon. Avant authentification, ne contient ke 2 vues {connexion,inscription}
				'menus'		: {},
				'sideBar'	: null, // Le menu-lateral, lui va changer àprès l'authentification
				'navBar'	: null,	// Fixe, ne change pas. Ne s'occuppe ke d'afficher le nom de la vue actuelle

			},
			'reqAJAX'		: {	},
			'statusError': { // Message d'erreurs global. Utilisé en cas de dernière recours si le serveur ne répond pas
			    '400' : "Server understood the request but request content was invalid.",
			    '401' : "Unauthorized Access",
			    '403' : "Forbidden resource not accessible",
			    '500' : "Internal Server Error",
			    '503' : "Service Unavailable"
			}
		};

		return {
			init : function (options){
				_internals['appNom'] 				= options.appNom || _defaults['appNom'];
				_internals['serveurUrl']		 	= options.url || _defaults['serveurUrl'];
				_internals['vues']['container'] 	= options.container;
				_internals['vues']['sideBar']		= options.menu;
				_internals['vues']['navBar']		= options.navBar;
				_internals['reqAJAX']['timeLimit']	= options['reqAJAX']['waitTimeLimit'] || _defaults['reqAJAX']['timeLimit'];
				_internals['reqAJAX']['onStart']	= options['reqAJAX']['onStart'] || _defaults['reqAJAX']['onStart'];
				_internals['reqAJAX']['onComplete']	= options['reqAJAX']['onComplete'] || _defaults['reqAJAX']['onComplete'];

			},

			getAppName : function (){
				return _internals['appNom'];
			},

			getServeurUrl : function (){
				return _internals['serveurUrl'];
			},

			getMsgErreur : function (status){
				return _internals['statusError'][status];
			},

			getContainer : function (){
				return _internals['vues']['container'];
			},


			getVues : function (){
				return _internals['vues']['partials'];
			},


			getVue 	: function(nomVue,action){
				if(this.contientNomVue(nomVue,action)){
					return this.getVues()[nomVue][action];
				}
				return null;
			},

			ajouterVue : function(nomV,nomAction,vueCompo) {
				var partialsVue =  _internals['vues']['partials'][nomV];
				if(!partialsVue){
					_internals['vues']['partials'][nomV] = {};
				}
				_internals['vues']['partials'][nomV][nomAction] = vueCompo;
			},


			getMenus : function (){
				return _internals['vues']['menus'];
			},
			setMenus : function (menus){
				return _internals['vues']['menus'] = menus;
			},



			ajouterVueDispo 	: function(nomVue,action){
				var compos = _internals['vues']['dispo'];
				if(!compos[nomVue]){
					compos[nomVue] = [];
				}
				compos[nomVue].push(action);
			},
			contientNomVue : function(nomVueActu,action){
				if(!action){
					return _internals['vues']['dispo'].indexOf(nomVueActu) > -1;
				}else{
					if(_internals['vues']['dispo'][nomVueActu]){
						return _internals['vues']['dispo'][nomVueActu].indexOf(action) > -1;
					}
					return false;
				}
			},

			getNomMenuActu : function (){
				return _internals['menu']['type']
			},
			setNomMenuActu : function (nom){
				_internals['menu']['type'] = nom;
			},


			getMenuActu   : function (){
				return _internals['menu']['actu'];
			},
			setMenuActu  : function ($menu){
				_internals['menu']['actu'] = $menu;
			},
			setMenuActuParNom : function(nom){
				_internals['menu']['actu'] = this.getMenus()[nom];
			},


			getSideBar : function (){
				return _internals['vues']['sideBar'];
			},

			getNavBar : function (){
				return _internals['vues']['navBar'];
			},


			getVueActu : function (){
				return _internals['vue']['actu'];
			},

			setVueActuParNom : function (nomVue,action){
				if(this.contientNomVue(nomVue,action)){
					var vue = this.getVues()[nomVue];
					if(action ){ // ? Y'a une action ki est demandée ?
						vue = vue[action];
					}
					_internals['vue']['actu'] = vue;
					this.setNomVueActu(nomVue,action);
					return true;
				}

				return false
			},

			getNomVueActu : function (){
				return _internals['vue']['nomActu'];
			},
			setNomVueActu : function (vueActu,actionActu){
				_internals['vue']['nomActu'] = vueActu;
				_internals['vue']['actionActu'] = actionActu;
			},
			getActionActu : function (){
				return _internals['vue']['actionActu'];
			},


			getNomVueFuture : function (){
				return _internals['vue']['nomFuture'];
			},
			setNomVueFuture : function (vueTurfu,actionTurfu){
				_internals['vue']['nomFuture'] = vueTurfu;
				_internals['vue']['actionFuture'] = actionTurfu;
			},

			getActionFuture : function (){
				return _internals['vue']['actionFuture'];
			},

			startWaitForReqAJAX : function (msg){
				_internals['reqAJAX']['onWaitin'] = window.setTimeout(function (){
					_internals['reqAJAX']['onStart'].call({},msg);
				},_internals['reqAJAX']['timeLimit']);

			},

			finishWaitForReqAjax : function(){
				window.clearTimeout(_internals['reqAJAX']['onWaitin']);
				_internals['reqAJAX']['onComplete'].call({});
			}




		}

	})();



	/****************************************************************************************************
		*********************************************************************************************

			PubSub Pattern - Peut être vue comme des observers mais en mieux

			1 - Pub(publish) 	On publie un event avec les données de celle-ci
			2 - Sub(Subscribe) 	On s'abonne sur un event en lui passant une fonction à exécuter lors
				que l'event se produit.

		*********************************************************************************************
	****************************************************************************************************/



	gerasmus.Events = {
		_events : {},


		// Const pouvant servir de nom pour des events
		// non-exhaustive à rajouter au fur mesure du dev.
			'MSG_ERREURS'			: 'app.erreurs.msg',

			'MENUS_CHARGEMENT_OK'	: 'app.menus.chargés',
			'MENUS_CHANGEMENT'		: 'app.menus.changement',
			'VUES_CHARGEMENT_OK'	: 'appp.vues.chargés',
			'VUE_CHANGEMENT'		: 'app.vue.changement',

			'LIST_DEPARTEMENTS' 	: 'gerasmus.list.departements',
			'LIST_PAYS'				: 'gerasmus.list.pays',
			'LIST_ORGAS'			: 'gerasmus.list.types.organisations',
			'LIST_PARTENAIRES_NOM'	: 'gerasmus.list.partenaires.nom',
			'LIST_PROGS'			: 'gerasmus.list.programmes',
			'LIST_DOCS'				: 'gerasmus.list.documents',
			'LIST_MOTIFS'			: 'gerasmus.list.choix.annulation.motifs',
			'LIST_LOGIS'			: 'gerasmus.list.logiciels',
			'LIST_MOBI_ETATS'		: 'gerasmus.list.mobilite.etats',
			'LIST_MOBI_ANNEES_ACAD'	: 'gerasmus.list.mobilite.annees_academique',
			'LIST_TYPES_PROG'		: 'gerasmus.list.types.programmes',

			'USER_DECONNECTE_OK'	: 'gerasmus.user.deconnecté',
			'USER_INSCRIT_OK'		: 'gerasmus.user.inscrit',
			'USER_DETAILS_PERSO'	: 'gerasmus.user.details.actu',
			'USER_DETAILS'			: 'gerasmus.user.details.autre',

			'MOBI_AJOUT_OK'			: 'gerasmus.mobilite.ajouté',
			'MOBI_ANNUL_OK'			: 'gerasmus.mobilite.annulé',
			'MOBI_LIST_OK'			: 'gerasmus.mobilite.demande.list',
			'MOBI_DETAILS_OK'		: 'gerasmus.mobilite.demande.details',
			'MOBI_CONFIRM_OK'		: 'gerasmus.mobilite.demande.confirmé',


			'PAT_AJOUT_OK'			: 'gerasmus.partenaire.ajouté',
			'PAT_DETAILS'			: 'gerasmus.partenaire.details',
			'PAT_LIST'				: 'gerasmus.partenaire.list',

			'SEARCH_OK'				: 'gerasmus.recherche.resultat.',
			'SEARCH_MOBI_OK'		: 'gerasmus.recherche.resultat.mobilite',
			'SEARCH_MOBILITE_OK'	: 'gerasmus.recherche.resultat.mobilite',
			'SEARCH_PAT_OK'			: 'gerasmus.recherche.resultat.partenaire',
			'SEARCH_USER_OK'		: 'gerasmus.recherche.resultat.user',


			'ERREUR'				: 'erreur',


		// Je trigger les event avec les datas de celle-ci, triggerEvent()
		// Pourra être appeler dans le Model pour signaler changement de val
		// 		Events.pub('user.ajout',{'ancien':0,'value':User})
		pub : function(evt,evtData) {
			// ? Veut publier la même data pour plusieurs event ?
			if(Array.isArray(evt)){
				var self = this;
				// Chope chak nom d'evt et go recursive
				return evt.forEach(function (event){
					self.pub(event,evtData);
				});
			}

			if(gerasmus.Events._events[evt]){
				var args = Array.from(arguments);
				args.shift(); // Je dégage le nom de la fct
				args.push(evt); // Rajoute le nom en fin
 				for(var nomFct in gerasmus.Events._events[evt]){
					var event = gerasmus.Events._events[evt][nomFct];
					// Chope les fonction et l'exec avec la data de l'event
					event.func.apply(event.context,args);
					// ? Fallait-il juste appeler cette function une SEULE fois ?
					if(event.justOnce){
						delete(gerasmus.Events._events[evt][nomFct]);
						//this.unsub(evt,event.func); // Donc, on le degage
					}
				}
			}
		},

		// Je m'inscris sur un event, addEventListener
		// Pourra être dans la Vue, pour dire kel fct appeler pour pub
		//		Event.sub('user.ajout', this, function (evtData){ console.dir(evtData); })
		sub : function (evt,ctxt,evtFunc,justOnce){
			// ? On veut s'abonner pour plusieurs event aek le même listener et params ?
			if(Array.isArray(evt)){
				var self = this;
				// Chope chak nom d'evt et go recursive
				return evt.forEach(function (event){
					self.sub(event,ctxt,evtFunc,justOnce);
				});
			}

			// Check si j'ai dja des listeners pour ce nom d'event
			gerasmus.Events._events[evt] = gerasmus.Events._events[evt] || {};

			// La function se trouve dans ctxt et evtFunc a pris la valeur de justOnce
			if(typeof evtFunc === "boolean"){
				justOnce = evtFunc;
				evtFunc = ctxt;
				ctxt = {};
			}

			// Pas de context défini, le context est devenu la function
			if(!evtFunc && typeof ctxt === 'function'){
				evtFunc = ctxt;
				ctxt = {};
			}
			if(typeof evtFunc === 'function'){
				// Check si j'ai deja cette function
				var exist = gerasmus.Events._events[evt].hasOwnProperty(evtFunc.name);
				// Autrement, je l'ajoute
				if(!exist){
					gerasmus.Events._events[evt][evtFunc.name] = {
						'context'   : ctxt,     // Si je fais appel à 'this' dans evtFunc, a koi ca correspond ?
						'func'      : evtFunc, // La fct à exec lors l'apparution de l'event
						'justOnce'  : justOnce // ? Doit-il être exec une seul fois ?
					};
				}
			}
		},


		// Je me desinscris de l'event, removeEventListener
		unsub : function (evt,evtFunc){
			if(gerasmus.Events._events[evt]){
				for(var idx in gerasmus.Events._events[evt]){
					if(gerasmus.Events._events[evt][idx] === evtFunc){
						gerasmus.Events._events[evt].slice(idx, 1);
						return;
					}
				}
			}
		},


		// Je degage tout les listeners pour ce event
		clear : function (evt){
			if(gerasmus.Events._events[evt]){
				gerasmus.Events._events[evt] = [];
			}
		}



	};








	/****************************************************************************************************
		*********************************************************************************************

			Model est une representation des données utilisables dans l'app.
			Tout ce qui est en rapport avec l'AJAX, le localStorage doit être effectuée dedans.
			Ne doit contenir aucune reference vers un selecteur DOM find(....) ou $(...).
			Ne connait aucune vue et n'a rien a faire de la vue ou du controller.
			Il interragit avec l'extérieur grace au Event.pub(evtNom)

		*********************************************************************************************
	****************************************************************************************************/


	gerasmus.Model = function (typeUser){
		this._typeUser = typeUser || 'quidam';
	};

	gerasmus.Model.prototype = {
		setTypeUser : function (type){
			this._typeUser = type;
		},

		getTypeUser : function (){
			return this._typeUser;
		}

	};

	gerasmus.Model.prototype.addEventListener 		= gerasmus.Events.sub;
	gerasmus.Model.prototype.removeEventListener 	= gerasmus.Events.unsub;
	gerasmus.Model.prototype.triggerEvent			= gerasmus.Events.pub;
	gerasmus.Model.prototype.clearEventListener 	= gerasmus.Events.clear;



	/**
	 * Sert à lancer un requete AJAX vers le serveur RCP.
	 * @param method - Quelle type de requete à envoyer {GET, POST, PUT} Par
	 *            défaut: GET
	 * @param paramUrl - Vers où dans le main envoyé la requete
	 * @param inputData - Les données venant de l'user à soumettre
	 * @returns {Promise.<*>} AJAX Request - La requete AJAX à effectuer en tant que Promise
     */
    gerasmus.Model.prototype.lancerReqAJAX = function (method,paramUrl,inputData){
        var url = Gerasmus.Context.getServeurUrl() + '?' + paramUrl;
        var data = ((inputData) ? JSON.stringify(inputData) : '{}');
        var vue = Gerasmus.Context.getNomVueFuture();
        var action = Gerasmus.Context.getActionFuture();

        if(method !== 'GET'){
	        data = JSON.stringify({
	        	'vue'			: ((vue) ? vue : Gerasmus.Context.getNomVueActu()),
	        	'action'		: ((action) ? action : Gerasmus.Context.getActionActu()),
	        	'input_data' 	: data
	        });
        }

		return new Promise(function(resolve,reject){
			gerasmus.Context.startWaitForReqAJAX(method + ' '+ url + '  '+ data);
			// Je lance la requete AJAX
			$.ajax({
				'contentType' : 'text/json', // J'envoie ke du JSON
				'url'  : url,
				'type' : method,
				'data' : data,
				'dataType': "json" // Je recoie ke du JSON
			}).done( function (resp){
				resolve(resp);
			}).fail( function catchFail(err){
				var failMsg = Gerasmus.Context.getMsgErreur(err.status) || '';
				if(err.responseJSON && err.responseJSON.err){
					failMsg = err.responseJSON.err;
				}else{
					failMsg += '<br>Une erreur est survenue lors du chargement des données.<br>Veuillez rafraichir cette page !';
				}
				gerasmus.Events.pub(gerasmus.Events.ERREUR,failMsg);
			}).always(function (){
				gerasmus.Context.finishWaitForReqAjax();
			});
		});

    };




	/****************************************************************************************************
		*********************************************************************************************
			La vue est la representation d'une page affichable à l'écran.
			C'est l'interface avec l'user. Lui-seul connait le DOM et peut le manipuler.
			Aucun #id ne doit apparaitre pour eviter les conflits d'unicité.
			Pour effectuer une action  système, il fait appel au Controller.
			Ex : Pour inscrire, il fait appel à la fonction dans le Ctrler en lui passant les inputs
		*********************************************************************************************
	****************************************************************************************************/


	gerasmus.Vue = function  (ctrl,typeMenu) {
		// var static
		gerasmus.Vue._msgErreurs 	= {}; // Tt les vues auront le même msg, le même container

		gerasmus.Context.setNomMenuActu(typeMenu || 'quidam'); // Definit le type de menu attendu pour la vue

		this._ctrl			= ctrl; // Le controller en cours d'utilisation pour cet  user

		this._erreurs 		= {}; // Pour les erreurs lors de la verification des données de la vue

	};


	// Tt les GETTERS && SETTERS
	gerasmus.Vue.prototype = {
		getErreurs 		: function (){
			return this._erreurs;
		},

		setErreurs : function (nErreurs){
			this._erreurs = nErreurs;
		},

		/**
		 * Rajouter une erreur dans la var _erreurs.
		 * @param  {String} nom Il s'agit du nom de l'erreur, doit être unik pour servir de clé
		 * @param  {String} msg Le msg sur l'erreur en question
		 */
		ajouterErreur 	: function (nom,msg){
			this._erreurs[nom] = msg;
		},

		contientErreurs : function (){
			return !objIsEmpty(this.getErreurs());
		},

		setNomMenu 		: function (mnu){
			gerasmus.Context.setNomMenuActu(mnu);
		},
		getNomMenu	 	: function (){
			return gerasmus.Context.getNomMenuActu();
		},


		setNomVueActu 	: function (vue,action){
			gerasmus.Context.setNomVueActu(vue,action);
		},
		getNomVueActu 	: function (){
			return gerasmus.Context.getNomVueActu();
		},
		getNomActionActu : function (){
			return gerasmus.Context.getActionActu();
		},



		setNomVueFuture : function (vueTurfu,action){
			gerasmus.Context.setNomVueFuture(vueTurfu,action);
		},
		getNomVueFuture 	: function (){
			return gerasmus.Context.getNomVueFuture();
		},
		getNomActionFuture : function (){
				return gerasmus.Context.getActionFuture();
		},



		getVues 	: function () {
			return gerasmus.Context.getVues();
		},
		getVue 	: function(nom) {
			return this.getVues[nom];
		},
		ajouterVue : function(nomVue,nomAction,vueCompo) {
			gerasmus.Context.ajouterVue(nomVue,nomAction,vueCompo);
			gerasmus.Context.ajouterVueDispo(nomVue,nomAction); // Rajouter cette vue dans tous ceux dispo.
		},


		getMenus 	: function () {
			return gerasmus.Context.getMenus();
		},
		ajouterMenus : function(menus) {
			gerasmus.Context.setMenus(menus);
		},



		set$MenuActu 	: function ($menu){
			gerasmus.Context.setMenuActu($menu);
		},


		get$VueActu 	 : function (){
			return gerasmus.Context.getVueActu();
		},

		set$VueActuParNom : function (){
			return gerasmus.Context.setVueActuParNom(this.getNomVueActu(),this.getNomActionActu());
		},

		get$VuePrincipal : function (){
			return gerasmus.Context.getContainer();
		},
		get$SideBar 	: function (){
			return gerasmus.Context.getSideBar();
		},
		get$NavBar : function (){
			return gerasmus.Context.getNavBar();
		},

		getCtrler 		: function(){
			if(!this._ctrl){
				this._ctrl = new gerasmus.Controller(new gerasmus.Model());
			}
			return this._ctrl;
		}

	};


	gerasmus.Vue.getMsgErreurs 	= function (type){
		if(type){
			return gerasmus.Vue._msgErreurs[type];
		}
		return gerasmus.Vue._msgErreurs;
	};

	gerasmus.Vue.determninerTypeSelonEtat = function(etat){
		switch(etat){
			case 'INTRO' :
				return 'default';
			case 'CONFIRME' :
				return 'info';
			case 'PREPA' :
				return 'success';
			default :
				return 'primary';
		}
	}


	gerasmus.Vue.creerLabelSelonEtat = function(etat){
		var type = gerasmus.Vue.determninerTypeSelonEtat(etat);
		return '<label class="label label-'+ type+'">'+etat+'</label>';
	}

	gerasmus.Vue.creerLabelSelonTypeUser = function(type){
		var col = (type === 'etud') ? 'info' : 'primary';
		var nom = (type === 'etud') ? 'Etudiant' : 'Professeur' ;
		return '<label class="label label-'+col+'">'+nom+'</label>';
	}

	gerasmus.Vue.creerLabelSelonCodeDepartement = function(code,nom){
		var icon = 'fa-user';
		switch(code){
			case 'BBM' :
				icon = 'fa-user-md';
				break;
			case 'BIN' :
				icon =  'fa-code';
				break;
			case 'BCH' :
				icon =  'fa-cubes';
				break;
			case 'BDI' :
				icon =  'fa-cutlery';
				break;
			case 'BIM' :
				icon = 'fa-user-md';
				break;
			default :
				icon = 'fa-user';
		}

		return '<label> <i class="fa '+ icon+'"></i> '+nom+'</label>';
	}




	/**
	 * Affiche un message de notification en bas à droite.
	 *
	 * @param {String}
	 *            type Le type de notification {success,info,warning,error}
	 * @param {String}
	 *            msg Le contenu de la notification
	 */
	gerasmus.Vue.notification = function(type,msg){
		if(!type) type = 'info';
		if(type === 'rappel' || type === 'attention') type = 'warning';
		if(type === 'err' || type === 'erreur') type = 'error';
		if(type === 'ok') type = 'success';
		return toastr[type](msg);
	};



	gerasmus.Vue.prototype.chargerMenus = function(menus){
		var $menus = {}; // Menus à garder

		// Je vide la bar sur le menu lateral
		this.get$SideBar().empty();

		for(var idx in menus){
			var $menu = $(menus[idx]);
			var typeMenuRecup = $menu.data('menu');
			$menus[typeMenuRecup] = $menu;

			// Check si ce menu est approprié pour ce type d'User
			if(typeMenuRecup === this.getNomMenu()){
				this.set$MenuActu($menu);
				$menu.show();
			}
			// Rajoute ensuite ce menu dans la sidebar pour plus tard.
			this.get$SideBar().append($menu);
		}
		this.ajouterMenus($menus);
	};


	// Effectue un chargement des vues dans le Context
	gerasmus.Vue.prototype.chargerVues = function (vues,cbVue){
		for(var index in vues){
			var $vue = $(vues[index]); // Convert een objet jQuery
			var nomVue = $vue.data('vue'); // Chope le nom attribué cette vue
			var nomAction = $vue.data('action');
			this.ajouterVue(nomVue,nomAction, $vue);
			// Rajoute cette vue dans le $container
			this.get$VuePrincipal().append($vue);
			// Si j'ai une function de callback
			if(cbVue){
				cbVue($vue);
			}
		}

	};


	gerasmus.Vue.prototype.estDispo = function (nomVue,action){
		return gerasmus.Context.contientNomVue(nomVue,action);
	};



	/**
	 * Change la vue actuel pour charger la vueFuture demandée.

	 * @param  {Selector} input le (input) en question à checker
	 * @return Promise pour effectuer d'eventuelles telles que
	 * remplir la vue de données.
	 */
	gerasmus.Vue.prototype.afficherVue = function (vue,action,cbContext,cbAfterAffichage){

		vue = vue || this.getNomVueFuture();
		action = action || this.getNomActionFuture();

		// Si la vue demandée est l'actuel, GET OUT !
		if(vue && !action && vue === this.getNomVueActu())
			return false;
		// Y'as aussi une action ki est demandée
		if(vue && action && vue === this.getNomVueActu() && action == this.getNomActionActu())
			return false;


		// Declarer la vue demandée est mnt la vueActu
		this.setNomVueActu(vue,action);

		// Cacher la vue actuelle
		if(this.get$VueActu()){
			this.get$VueActu().hide();
		}

		// Choper la vue demande
		var change = this.set$VueActuParNom();

		if(change){
			this.get$VueActu().show();
		}

		// Pas de context défini, le context est devenu la function
		if(!cbAfterAffichage && typeof cbContext === 'function'){
			cbAfterAffichage = cbContext;
			cbContext = {};
		}


		if(cbAfterAffichage){
			cbAfterAffichage.call(cbContext,change,this.get$VueActu());
		}

		gerasmus.Events.pub(gerasmus.Events.VUE_CHANGEMENT,(change || this.get$VueActu() != null)); // Je signale un changement de la vue actuelle

	};













	/****************************************************************************************************
		*********************************************************************************************

			Le Controller est le Big-boss.
			Lui-seul sait koi faire avec cet enculé de vue et l'égoïste de Model.

		*********************************************************************************************
	****************************************************************************************************/


	gerasmus.Controller = function (model) {
		this._model = model;
	};


	/**
	 *
	 * @returns {*}
     */
	gerasmus.Controller.prototype.getModel = function() {
	    return this._model;
	};


	gerasmus.Controller.prototype.setTypeUser = function (typeMenu){
		this._model.setTypeUser(typeMenu);
	};

	gerasmus.Controller.prototype.getTypeUser = function(){
		return this._model.getTypeUser();
	};










	return gerasmus;
}()); // IIFE Je le lance direct
