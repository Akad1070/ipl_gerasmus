// Recoie en param une instance d'Events qui ne sera accessible ke dans tt Base
// Pour permettre une discussion entre les différentes couches (parties)
var Base = (function (Events){

    var base = {}; // Definis un namespace à return en fin


/*******************************************************************************************
                                Partie MODEL
*******************************************************************************************/

    base.Model = function (){

        Gerasmus.Model.call(this); // equivaut à super

        // Est 'static' : tjrs dispo pour tt le monde
        base.Model._departements = {};
        base.Model._pays = {}; //
        base.Model._typeOrganisations = {}; //
        base.Model._programmes = [];
        base.Model._typesProgramme = {};
        base.Model._etas = {};
        base.Model._asUser;
        base.Model.prototype._user;

        // ? S'est-t-il deja connecté ?

        if(this.isAuth()){
            this._user = JSON.parse(localStorage.getItem('gAuthUser'));
            this.setTypeUser(this._user['type']);
        }


    };
    base.Model.heriteDe(Gerasmus.Model);


    base.Model.prototype.isAuth = function(){
        return (localStorage && localStorage.getItem('gAuthUser') !== null);
    };

    base.Model.prototype.saveUserDetails = function (user){
        localStorage.setItem("gAuthUser",JSON.stringify(user)); // Je save user
    };

    base.Model.prototype.getCurrentUser = function(){
        return JSON.parse(localStorage.getItem('gAuthUser'));
    };


    base.Model.prototype.chargerVues = function (){
        this.lancerReqAJAX('GET').then(function (response){
            Events.pub(Events.VUES_CHARGEMENT_OK,response);
        });
    };



    base.Model.prototype.getDepartements = function (){
        // Check si undefined ou bien vide
        if(!base.Model._departements || Gerasmus.Util.isEmpty(base.Model._departements)){
            this.lancerReqAJAX('POST','req=departements').then(function (response){
                base.Model._departements = response['departements'];
                Events.pub(Events.LIST_DEPARTEMENTS,base.Model._departements);
            })
        }else{
            Events.pub(Events.LIST_DEPARTEMENTS,base.Model._departements);
        }
    };


    base.Model.prototype.getUserDetails = function (uid){
        // Veut ses données perso et j'ai déja save ca : Pas de ReqAJAX
        if( (!uid)){
            Events.pub(Events.USER_DETAILS_PERSO, this.getCurrentUser());
            return;
        }
        this.lancerReqAJAX('POST','req=user&uid='+uid).then(function (response){
            var evtName = (uid) ? Events.USER_DETAILS : Events.USER_DETAILS_PERSO;
            var estProf = response['user']['prof'];
            response['user']['type'] = ((estProf) ? 'prof' : 'etud');
            Events.pub(evtName,response['user'],response['infos']);
        });
    };


    base.Model.prototype.getOrganisations = function(){
        // Check si vide
        if(Gerasmus.Util.isEmpty(base.Model._typeOrganisations)){
            this.lancerReqAJAX('POST','req=organisations').then(function (response){
                base.Model._typeOrganisations = response['organisations'];
                Events.pub(Events.LIST_ORGAS,base.Model._typeOrganisations);
            })
        }else{
            Events.pub(Events.LIST_ORGAS,base.Model._typeOrganisations);
        }
    };

    base.Model.prototype.getProgrammes = function(){
        // Check si vide
        if(Gerasmus.Util.isEmpty(base.Model._programmes)){
            this.lancerReqAJAX('POST','req=programmes').then(function (response){
                base.Model._programmes = response['programmes'];
                Events.pub(Events.LIST_PROGS,base.Model._programmes);
            })
        }else{
            Events.pub(Events.LIST_PROGS,base.Model._programmes);
        }
    };

    base.Model.prototype.getTypesProgramme = function(){
        // Check si vide
        if(Gerasmus.Util.isEmpty(base.Model._typesProgramme)){
            this.lancerReqAJAX('POST','req=types_programme').then(function (response){
                base.Model._typesProgramme = response['types_programme'];
                Events.pub(Events.LIST_TYPES_PROG,base.Model._typesProgramme);
            })
        }else{
            Events.pub(Events.LIST_TYPES_PROG,base.Model._typesProgramme);
        }
    };

    base.Model.prototype.getPays = function(){
        // Check si vide
        if(Gerasmus.Util.isEmpty(base.Model._pays)){
            this.lancerReqAJAX('POST','req=pays').then(function (response){
                base.Model._pays = response['pays'];
                Events.pub(Events.LIST_PAYS,base.Model._pays);
            })
        }else{
            Events.pub(Events.LIST_PAYS,base.Model._pays);
        }
    };

    base.Model.prototype.getAnnesAcademique = function(){
        this.lancerReqAJAX('POST','req=annees_academique').then(function (response){
            Events.pub(Events.LIST_MOBI_ANNEES_ACAD,response['annees_academique']);
        })
    };


    base.Model.prototype.getEtatsMobilite = function(){
        if(Gerasmus.Util.isEmpty(base.Model._etas)){
            this.lancerReqAJAX('POST','req=etats_mobilite').then(function (response){
                base.Model._etas = response['etats_mobilite'];
                Events.pub(Events.LIST_MOBI_ETATS,base.Model._etas);
            })
        }else{
            Events.pub(Events.LIST_MOBI_ETATS,base.Model._etas);
        }
    };


    base.Model.prototype.saveAsUser = function(uid){
        localStorage.setItem("asUID",uid); // Je save user
    }





/*******************************************************************************************
                                Partie CONTROLLER
*******************************************************************************************/

    base.Controller = function (model){
        Gerasmus.Controller.call(this,model);
    };
    base.Controller.heriteDe(Gerasmus.Controller);



    base.Controller.prototype.checkIfAuth = function (){
        if(!this._model.isAuth()){
            var msg = 'Cette action n\'est pas autorisée. <br>';
            if(this._model.getTypeUser() === "quidam"){
                msg += 'Veuillez d\'abord vous connecter !';
            }else{
                if(this._model.getTypeUser() === "etud"){
                    msg += 'Nécessite le niveau d\' un professeur';
                }
            }

            Events.pub(Events.ERREUR , msg);
        }
        return true;
    };

    base.Controller.prototype.isProf = function(){
        return this._model.getCurrentUser()['prof'];
    };

    base.Controller.prototype.isCurrentUser = function(id){
        return this._model.getCurrentUser()['id'] == id;
    };



    base.Controller.prototype.chargerVues  = function (){
        this._model.chargerVues();
    };

    base.Controller.prototype.isAuth  = function (){
        return this._model.isAuth();
    };

    base.Controller.prototype.getUserDetails = function (uid){
        this._model.getUserDetails(uid);
    };

    base.Controller.prototype.getDepartements = function (){
        this._model.getDepartements();
    };

    base.Controller.prototype.getProgrammes = function (){
        this._model.getProgrammes();
    };

    base.Controller.prototype.getPartenaires = function (){
        this._model.getPartenaires();
    };

    base.Controller.prototype.getTypesProgramme = function(){
        this._model.getTypesProgramme();
    };

    base.Controller.prototype.getOrganisations = function (){
        this.getModel().getOrganisations();
    };

    base.Controller.prototype.getPays = function (){
        this._model.getPays();
    };


    base.Controller.prototype.getAnnesAcademique = function (){
        this._model.getAnnesAcademique();
    };


    base.Controller.prototype.getEtatsMobilite = function (){
        this._model.getEtatsMobilite();
    };



/*******************************************************************************************
                                Partie VUE
 *******************************************************************************************/


    // Constructeur
    base.Vue = function (ctrl) {
        Gerasmus.Vue.call(this,ctrl,ctrl.getTypeUser()); // super(ctrl,typeuser)
    };

    base.Vue.heriteDe(Gerasmus.Vue);


    /**********************************************
    *************** Bind Events *******************
    ***********************************************/


    // Ke faire lors k'un evt erreur survient dans cette vue
    Events.sub([Events.ERREUR,'attention','rappel'],function (err,evtName){
        if(typeof err === 'object') err = err.msg || err.getMsg(); // DOIS TJRS contenir un msg si c'est un object
        Gerasmus.Vue.notification(evtName, err);
    });


    // Que faire kan je recois par event une list de departements
    Events.sub(Events.LIST_DEPARTEMENTS, function remplirSelectDepartements (depts){
        var $select = $('select[name=\'departements_ipl\']');
        $select.empty(); // S'il y a des <option>, ils degagent
        for(var idx in depts){
            var dep = depts[idx];
            var $opt =$('<option>').val(dep.id).html(dep.nom);
            $select.append($opt);
        }
        $select.trigger("chosen:updated");
    });


    // Que faire kan je recois par event une list de TypeOrganisation
    Events.sub(Events.LIST_ORGAS, function remplirSelectOrgas(orgas){
        var $select = $('select[name=\'type_organisation\']');
        $select.empty(); // S'il y a des <option>, ils degagent
        $select.append($('<option>').val(null).html('Aucune idée'));
        for(var idx in orgas){
            $select.append($('<option>').val(orgas[idx]).html(orgas[idx]));
        }
        $select.trigger("chosen:updated");
    });


    // Que faire kan je recois par event une list des pays
    Events.sub(Events.LIST_PAYS, function remplirSelectPays(pays){
        var $select = $('select[name=\'pays\']');
        $select.empty(); // S'il y a des <option>, ils degagent

        for(var idx in pays){
            $select.append($('<option>').val(pays[idx].id).html(pays[idx].nom));
        }
        $select.trigger("chosen:updated");
    });

    // Que faire kan je recois par event une list des années academiques
    Events.sub(Events.LIST_MOBI_ANNEES_ACAD, function remplirSelectAnneesAcademique(annees){
        var $select = $('select[name=\'mobi_annee_academique\']');
        $select.empty(); // S'il y a des <option>, ils degagent

        for(var idx in annees){
            $select.append($('<option>').val(annees[idx]).text(annees[idx]));
        }
        $select.trigger("chosen:updated");
    });


    // Que faire kan je recois par event une list des années academiques
    Events.sub(Events.LIST_MOBI_ETATS, function remplirSelectAnneesAcademique(etats){
        var $select = $('select[name=\'mobi_etat\']');
        $select.empty(); // S'il y a des <option>, ils degagent

        for(var idx in etats){
            $select.append($('<option>').val(idx).text(etats[idx]));
        }
        $select.trigger("chosen:updated");
    });




    // Pour gerer l'event changer le menu
    Events.sub(Events.MENUS_CHANGEMENT, function changerMenuSelonTypeUser(userType){
        var $menuActu = Gerasmus.Context.getMenuActu();
        if($menuActu) $menuActu.hide(); // Le menu actuel doit être caché
        Gerasmus.Context.setNomMenuActu(userType); // Definis le type de menu
        Gerasmus.Context.setMenuActuParNom(userType); // Va choper le menu du quidam
        $menuActu = Gerasmus.Context.getMenuActu();
        if($menuActu) $menuActu.show(); // Ré-Affiche le menu du quidam.
    });




    function afterChargerConfigVuesEtBindActions (){

        // Fenetre en modal
        Gerasmus.Context.getContainer().find('[data-toggle=modal]').modal('hide');

        // Popover
        Gerasmus.Context.getContainer().find("[data-toggle=popover]").popover({
            trigger : 'hover focus',
            html : true
        });

        // Tooltips
        Gerasmus.Context.getContainer().find("[data-toggle=tooltip]").tooltip({
            placement : 'top',
            trigger : 'focus'
        });

        Gerasmus.Context.getContainer().find(".footable").footable();


        // Chosen those fu***ng select
        Gerasmus.Context.getContainer().find('select').chosen({
            no_results_text:'Aucun résultat pour ',
            placeholder_text_single : "Selectionnez",
            placeholder_text_multiple : "Selectionnez à volonté mais avec modération",
            width: '100%'
        });

        // Tant ke j'y suis, j'écoute ausi la touche Enter sur les inputs
        Gerasmus.Context.getContainer().on('keyup', 'input', function(e) {
            var keycode = (e.keyCode ? e.keyCode : e.which);
            if(keycode === 13){
                var btnAct = Gerasmus.Context.getVueActu().find('form [data-action], [data-action]');
                if(btnAct){
                    Gerasmus.Router.redirect(btnAct.attr('href'));
                    return false;
                }
            }
        });
    }








    /**
     * Sert à afficher la vue demandée en la chargeant si necessaire.
     * Par défaut, cherchera à affciher la vue de l'accueil.
     */
     base.Vue.prototype.afficher = function(nomVue,action,cbContext,cbAfterAffichage) {

		// Pas de context défini, le context est devenu la function
		if(!cbAfterAffichage && typeof cbContext === 'function'){
			cbAfterAffichage = cbContext;
			cbContext = {};
		}

         if(!nomVue){
             nomVue = 'quidam';
         }
         if(!action){
             action = 'index';
         }

        if(nomVue !== 'quidam' && !this.getCtrler().isAuth()){
            $('.topbar').hide();
            return this.afficher('quidam','connexion');
        }

        this.setNomVueFuture(nomVue,action);

        if(action === 'connexion' || action === 'inscription'){
            $('.topbar').hide();
        }else{
            $('.topbar').show();
        }

        if(this.estDispo(nomVue,action)){
            var aChange = false;
            // J'ai deja la vue parmi mes composants : Donc dja chargé
            this.afficherVue(nomVue,action,cbContext,cbAfterAffichage);
            return true;
        }


        // La vue demandée n'est pas dispo, n'existe pas, donc les charger  => Effectuer un reqAJAX vers Serveur
        this.getCtrler().chargerVues();

        var EVT_SPECIAL_VUE_DEMANDE = Events.MENUS_CHARGEMENT_OK + '.'+nomVue+'.'+action;
        // Je m'abonne pour l'event  'chargement de vues pour l'user'
        Events.sub([Events.VUES_CHARGEMENT_OK,EVT_SPECIAL_VUE_DEMANDE ] , this, function afterChargerVues (response){
            // J'ai recu des mensu et j'ai pas encore de menus
            if(response['menus'] && Gerasmus.Util.isEmpty(this.getMenus())){
                this.chargerMenus(response['menus']);
            }
            this.chargerVues(response['vues'],function ($vue){
                //$vue.find('select').chosen();
            }); // Je charge toutes les vues ke j'ai recues

            afterChargerConfigVuesEtBindActions();

            if(!this.estDispo(nomVue,action)){
                this.get$VuePrincipal().html($('h1').html('Page NOT Found - La vue demandée n\'est pas disponible'));
                return;
            }
            this.afficherVue(null,null,cbContext,cbAfterAffichage); // J'affiche mnt la vue demandée;
            if(this.getCtrler().isAuth()){ // Suis-je deja connecté
                // Envoie moi les données sur l'user' pour l'accueillir :-)
                Events.sub(Events.USER_DETAILS_PERSO, this, this.accueillir,true);
                this.getCtrler().getUserDetails();
            }
        });

         // Ajouter dans la barre de Navigation, le nom de la page actuelle.
         // Les différents liens parcourus pour atteindre cette vue.
         Events.sub(Events.VUE_CHANGEMENT, this, function ajouterDansBarreNavigation (changement){
             if(changement){
                // Choper le titre de la page dans le data-
                var titre = this.get$VueActu().data('titre-vue');
                // Choper l'icone et l'agrandir de 1/3
                var icone = this.get$VueActu().data('icone')+" fa-lg";
                // Je vais chercher le tag responsable du titre de nav
                this.get$NavBar().empty().prev('.vue_actu_titre').html("&nbsp;"+titre).prepend($('<i>').addClass(icone));

                document.title = titre + ' - ' + Gerasmus.Context.getAppName();
             }

        });

     };






     // L'appel à l'index renvoie vers la methode accueil
     base.Vue.prototype.index = base.Vue.prototype.accueil;
     base.Vue.prototype.accueil = function() {
        if(this.getCtrler().isAuth()){
            Events.sub(Events.USER_DETAILS_PERSO, this, this.accueillir,true);
            this.getCtrler().getUserDetails();
            return this.afficher('base' ,'accueil');
        }

        Gerasmus.Router.redirect('connexion');
    };

    // Koi faire apres  voir afficher la page d'accueil
    base.Vue.prototype.accueillir = function accueillir (user){
        var type  = (user && user.type) ? (user['type']) : 'etud' ;
        this.setNomMenu(type); // Je definis le type de menu à afficher mnt

        $('.user-prenom').text(user.prenom);
        $('.user-nom').text(user.nom.toUpperCase());
        $('.user-pseudo').text(user.pseudo);
        $('.user-mail').text(user.mail);
        var label = Gerasmus.Vue.creerLabelSelonCodeDepartement(user.departement.code,user.departement.nom);
        $('.user-dep').html(label);
        $('.user-date-inscrip').text(Gerasmus.Util.formatDate(user.dateInscription));
    };


    /**
     * Creer les differentes années academique dispo pour les mobilités.
     */
    base.Vue.prototype.remplirSelectAnneAcademique = function(){
        var $select = $('select[name="annee_academique"]');
        var annee_actu = new Date().getFullYear();
        $select.empty(); // S'il y a des <option>, ils degagent
        for (var annee = annee_actu; annee < annee_actu + 2;++annee) {
            var annee_acad = annee+ ' - ' + (annee + 1) ;
            $select.append($('<option>').val(annee).html(annee_acad));
        }

        $select.change(function(event) {
            var annee_acad = $(this).val();
            $('[name="annee_academique"]:not(:selected)').each(function (idx,elt){
                $(elt).val(annee_acad).trigger("chosen:updated");
            });
        });
        $select.trigger("chosen:updated");
    };



    // Videra tous les champs du formulaire de la vue en cours
    // Met tous les radio & checkbox à null
    base.Vue.prototype.viderFormulaire = function (){
        var $form = this.get$VueActu().find('form');
        $form.find('input,textarea').val('');
        $form.find('select').trigger('chosen:reset');
        $form.find('input:radio, input:checkbox').removeAttr('selected').removeAttr('checked');
    };



    /**
     * Parcourt tous les <input> dans le formulaire et prend les valeurs
     * necessaires.
     * @param $formulaire - Le selecteur du form à parcourir.
     * @returns {*} Un object JSON contenant les data valides ou les erreurs{input.name : input.err}
     */
     base.Vue.prototype.collecterFormulaire = function($formulaire ){
        var self = this;
        var inputs = {};
        var errone = false;
        self.setErreurs({});

        var $form = $formulaire || this.get$VueActu().find('form');

        // Je chope ke le <select> ki ont des options selectionnées
        $form.find('select').each(function (idx,select) {
            // Je prend ke les <select> dans lesquelles on a selectionnée quelque chose
            var $selectSelectionne =  $(select).find(':has(option:selected)');
            // ? Y'en a-t-il ?
            if($selectSelectionne){
                // Si y'avais une erreur avec ce select, plus d'erreur
                $(select).parent().removeClass('has-error').children('.erreur').text('');

                inputs[select.name] = $(select).val();

            }else{
                // Non, alors ? ce select, est-il obligatoire ?
                if(select.hasAttribute('required')){
                    // Baamm, une erreur
                    self.ajouterErreur(select.name, Gerasmus.Vue.getMsgErreurs('CHAMP_REQUIS_VIDE'));
                    $(select).parent().addClass('has-error')
                    .children('.erreur').text(Gerasmus.Vue.getMsgErreurs('CHAMP_REQUIS_VIDE'));
                }
            }
        });



        // Je chope tous les inputs et je les foreach
        $form.find('input[name]').each(function (index,input){
            errone = false;
            var $input      = $(input);
            var $parent     = $input.parent();
            var $lblErreur  = $parent.children('.erreur');

            switch(input.type){
                case 'radio' :
                if(!input.checked){
                    $parent.addClass('has-error');
                    self.afficherFormErreur(Gerasmus.Vue.getMsgErreurs('CHAMP_REQUIS_VIDE'),$input,$lblErreur);
                }
                break;
                case 'number':
                case 'url':
                case 'search':
                case 'text':
                case 'email':
                case 'password' :
                    // ? Cet input est obligatoire && ? L'user a input quelque chose ?
                    if(input.validity.valueMissing){ // L'enculé n'a rien inputed
                        // Met l'input en erreur
                        $parent.addClass('has-error');
                        self.afficherFormErreur(Gerasmus.Vue.getMsgErreurs('CHAMP_REQUIS_VIDE'),$input,$lblErreur);
                        errone = true;
                        self.ajouterErreur(input.name, Gerasmus.Vue.getMsgErreurs('CHAMP_REQUIS_VIDE'));
                    }else{
                        self.afficherFormErreur('',$input,$lblErreur);
                        $parent.removeClass('has-error').removeClass('has-warning');
                    }

                    // Je check si c'est bon.
                    if(!errone && !(errone = self.checkInput(input,$parent,$lblErreur))){
                        inputs[input.name] = input.value;
                    }

                    break;
            } // Switch sur input.type

        }); // forEach inouts in <form>

        // Mnt ke j'ai fini de foreach les <input>, check si y'a des erreurs
        if(errone ||  this.contientErreurs()){
            Gerasmus.Vue.notification('warning','Des donnees sont incorrectes. Veuillez les corriger !');
            return null;
        }
        return inputs;
    }



    /**
     * Effectuer un check sur l'input recu et rempli l'erreur sys en fonction du type.
     * @param  {Selector} input le (input) en question à checker
     */
     base.Vue.prototype.checkInput = function(input,$parent,$lblErreur) {
        var msg = '';

        if(input.validity.valueMissing && !input.validity.valid){
            msg = input.validationMessage;
        }

        switch(input.type){
            case 'password' :
            if(input.validity.rangeUnderflow){
                msg = 'Votre mot de passe '+Gerasmus.Vue.getMsgErreurs('TROP_COURT');
            }
            break;
            case 'email' :
            if(input.validity.typeMismatch){
                msg = Gerasmus.Vue.getMsgErreurs('FORMAT_EMAIL_INVALIDE') ;
            }
            break;
            case 'number' :
            if(input.validity.typeMismatch){
                msg = Gerasmus.Vue.getMsgErreurs('NUMERICAL_INPUT') ;
            }
            break;
            case 'tel' :
            if(input.validity.typeMismatch){
                msg = Gerasmus.Vue.getMsgErreurs('ALPHANUM_INPUT') ;
            }

        }

        // Si j'ai une erreur ki doit être afficher
        if(msg){
            if(!$parent.hasClass('has-error')){ $parent.addClass('has-warning'); }
            this.afficherFormErreur(msg + input.validationMessage,$(input),$lblErreur);
            Gerasmus.Vue.notification('warning',msg);
            //this.ajouterErreur(input.name, msg);
        }

        return msg !== '';
    };


    /**
     * Affiche les differents erreurs apparus lors de la collecte.
     * @param msgErreur - Le msg d'erreur à afficher. Peut être vide pour cacher le msg d'erreur.
     * @param $input - L'input sur lequel est apparu l'erreur
     * @param $lblErreur - [Optionnel] Ou place le msg d'erreur;
     */
    base.Vue.prototype.afficherFormErreur = function (msgErreur,$input,$lblErreur){
        if(!$lblErreur){
            $lblErreur = $input.parent().addClass('has-error').children('.erreur');
        }

        if($lblErreur&& $lblErreur.length > 0){
            $lblErreur.text(msgErreur);
        }else{
            $input.attr({
                'data-toggle': 'tooltip',
                'data-placement' : 'top',
                'data-original-title': msgErreur
            }).tooltip();
        }

    };



    /*********************** Fin de Base.js **************************/

    return base;

})(Gerasmus.Events); // IIFE
