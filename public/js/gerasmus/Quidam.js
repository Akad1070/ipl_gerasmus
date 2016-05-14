
var Quidam = (function (Events){

	var quid = {};

    quid.Model = function (){
        Base.Model.call(this); // equivaut à super
    };
    quid.Model.heriteDe(Base.Model);

    quid.Model.prototype.connecter = function (inputs){
        var self = this;
        this.lancerReqAJAX('POST',null,inputs)
            .then(function (response){
                var user = response['user'];
                user['type'] = response['user.type'];
                // Je save le token recu du Serveur dans la mem. du browser
                // localStorage.setItem('gerasmus-token', response['token']);
                self.saveUserDetails(user);

                Events.pub(Events.USER_INSCRIT_OK,user);
            });
    };

    quid.Model.prototype.inscrire = function(inputs) {
        this.lancerReqAJAX('POST', 'quid-action=inscrire',inputs).then(function (response){
            Events.pub(Events.USER_INSCRIT_OK,response['msg']);
        });
    };

    quid.Model.prototype.deconnecter = function() {
        var self = this;
        localStorage.clear();
        this.lancerReqAJAX('POST','req=out').then(function (){
            self.setTypeUser('quidam');
            Events.pub(Events.USER_DECONNECTE_OK);
        })
    };





/*******************************************************************************************
                                Partie CONTROLLER
*******************************************************************************************/

    quid.Controller = function (model){
        Base.Controller.call(this,model);
    };
    quid.Controller.heriteDe(Base.Controller);



    quid.Controller.prototype.connecter = function (inputs){
        if(inputs){ // Il a remplit le form de connexion
            this._model.connecter(inputs)
        }else{ // Il a peut-etre deja été connecté
            this._model.checkIfAuth();
        }

        Events.sub(Events.USER_INSCRIT_OK,this,function configAuthUser (user){
            // Change le type d'user en prof || etud
            this.setTypeUser(user['type']);
            Events.pub(Events.MENUS_CHANGEMENT,user['type']); // Mets moi le bon menu.
            Gerasmus.Router.redirect('accueil');
            Gerasmus.Vue.notification('success',"Bienvenue, "+ user['pseudo']);
        });
    };

    quid.Controller.prototype.inscrire = function (inputs){
        this._model.inscrire(inputs);
        Events.sub(Events.USER_INSCRIT_OK, function afterInscrire(msg){
            Gerasmus.Router.redirect('connexion');
            Gerasmus.Vue.notification('success',msg +'<br> Veuillez vous connecter à présent !');
        });
    };

    quid.Controller.prototype.deconnecter = function (){
        this._model.deconnecter();
        Events.sub(Events.USER_DECONNECTE_OK, this,function afterDeconnecter(){
            Events.pub(Events.MENUS_CHANGEMENT,'quidam'); // J'ordonne le chargement du menu.
            Gerasmus.Router.redirect('connexion');
            Gerasmus.Vue.notification('info','A bientôt');
        },true);
    };



/*******************************************************************************************
                                Partie VUE
*******************************************************************************************/


    // Constructeur
    quid.Vue = function (ctrl) {
        Base.Vue.call(this,ctrl,ctrl.getTypeUser()); // super(ctrl,typeuser)
        /*
        $(window).unload(function() {
            ctrl.deconnecter();
        });
        */
    };

    quid.Vue.heriteDe(Base.Vue);


    /**********************************************
    *************** Bind Events *******************
    ***********************************************/





    quid.Vue.prototype.connexion = function afficherConnexion(){
        if(this._ctrl.isAuth()){
            return Gerasmus.Router.redirect('accueil');
        }
        this.afficher('quidam','connexion');
    };

    quid.Vue.prototype.connecter = function(){
        if(this.get$VueActu()){
            var inputs = this.collecterFormulaire();
            if(inputs){
                this.getCtrler().connecter(inputs);
                this.viderFormulaire();
                Gerasmus.Router.redirect('accueil');
                return;
            }
        }

        Gerasmus.Router.redirect('connexion');
    };




    quid.Vue.prototype.inscription = function afficherInscription(){
        var self = this;
        if(this._ctrl.isAuth()){
            return Gerasmus.Router.redirect('accueil');
        }
        this.afficher('quidam' ,'inscription', this,function (done, $vue){
            self.getCtrler().getDepartements();
        });

    };

    quid.Vue.prototype.inscrire = function (){
        if(this.get$VueActu()){
            // Va choper tout ce k'il mit dans le formulaire
            var inputs = this.collecterFormulaire();
            if(inputs){ // ? A-t-il entré quelque chose ?
                var $form = this.get$VueActu().find('form'), $input =null;

                if(inputs.mdp !== inputs.mdp2){
                    $input = $form.find('input[name=mdp]');
                    var msg = 'Les mots de passe ne correspondent pas';
                    this.afficherFormErreur(msg,$input);
                    this.afficherFormErreur(msg,$form.find('input[name=mdp2]'));
                    $input.focus();
                }

                if(inputs.mdp === inputs.pseudo){
                    $input = $form.find('input[name=pseudo]');
                    this.afficherFormErreur('Doit être différent du mot de passe',$input);
                    $input.focus();
                }

                inputs['departement'] = inputs['departements_ipl'];

                // ? Tout est bueno et valide ?
                if($input === null && !this.contientErreurs()){  // ? Aucune erreurs dans le formulaire ?
                    this.getCtrler().inscrire(inputs);
                    Events.sub(Events.USER_INSCRIPTION_OK,this, function (){
                        this.viderFormulaire();
                    });
                }
                return;
            }
        }
        Gerasmus.Router.redirect('inscription');

    };

    //quid.Vue.prototype.out = quid.Vue.prototype.deconnecter;
    quid.Vue.prototype.deconnecter = function (){
        this.getCtrler().deconnecter();
    };



/*********************** Fin de quidam.js **************************/

    return quid;

})(Gerasmus.Events); // IIFE
