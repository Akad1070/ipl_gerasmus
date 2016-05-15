AppServlet
===========

Le chemin (URL) vers l'API est  ...:8080/api/gerasmus/v1

L'AppServlet ne répond que 2 types de methodes {GET, POST}.La methode GET renvoie, après verification de l'authentification, les vues HTML pour le front-end.
La methode POST sert à obtenir et modifier des données en rapport avec l'application.

Le body de la requete de type POST doit toujours contenir {vue,action ,input_data}.
Ceci n'est pas requis pour une requete de type POST avec un paramètre 'req'.

Ex:
---
Pour connecter un utilisateur, voici le format du body
    {
    "vue":"quidam",
    "action":"connexion",
    "input_data":"{\"identifiant\":\"donatien.grolaux@vinci.be\",\"mdp\":\"MdpAdmin123\"}"
    }

ou Lister les mobiltés en tant que professeur
    {
    "vue":"mobilite",
    "action":"list",
    "input_data":"{}"
    }

Les actions de type "maj" requiert de specifier le type de maj à effectuer dans le 'input_data' du body.



La methode POST peut aussi prendre un paramètre 'req' pour obtenir des données non-relatives à une vue particulière telle que
les différents departements, la liste des types d'organisations possibles pour un partenaire, liste des pays disponibles,
verifier si l'user courrant est bien authifié.



JavaScript
==========

J'avais pour idée de créer un petit 'framework' MVC pour etablir une base sur laquelle chacun pourrait contribuer au JS au lieu de laisser une seule personne en charge de cette partie.
En fin de compte, ca ne s'est pas passée ainsi car personne dans le groupe n'avait réussi l'examen JS et avait des lacunes pour la compréhension du JS.

Je vais présenter en gros les principales parties de ce 'framework'.


_CORE.js :
---------
Regroupe les fonctionnalités du framework. Representer par le namespace 'Gerasmus'

### Router : Un syteme de router simple avec pris en charge de paramètre.
Utilise une reconnaissance par RegExp.
Ecoutant les chagements du #hash dans la barre d'adresse.
Permet la redirection vers un route ne particulier.
Permet de mettre un alias pour un chemin : #inscription dirige vers #/quidam/inscription


### Context :
Pour retenir toutes les vues visibles actuellement par l'utilisateur, les différens msg affichables en cas d'erreur d'une reqAJAX sans message d'erreur envoyé, le nom de l'application et l'URL vers le serveur.



### Events : Pour gerer la communication entre les couches. Basée sur le pattern PubSub
J'avais commencé avec le pattern Observer mais j'avais constamment besoin de connaitre les listenners pour pouvoir ajouter.
Et ca ne permettait pas de transmettre la reponse la partie en charge des Mobilités quand on se trouvait dans les Partenaires.
Par exemple, pour dire mettre a jour liste des partenaires disponibles pour une Mobilité après avoir ajouté un nouveau partenaire.

Un event dans l'app est caractérisée par une nom obligatoire avec une data ou une fonction pour la gérer quand elle est survient.



### Model	: Gere les appels AJAX et les données.
Apres éxec d'une requete AJAX retoure une Promise, manipulable par les autres Model. Gere directement le msg d'erreur en cas de fail de la requete.
Ne peut communiquer avec les autres couches grace aux Events.


### Controller : Connait le model et appelable par la vue pour effectuer des actiosn des données.


### Vue :	Responsable de ce qui est visible pour l'utilisateur.
Charge les vues et les menus dans le Context
Permet de notifier l'utilisateur avec un msg et un type {SUCCESS, WARNING,ERROR}



_MAIN.js :
========
Sert de boot pour l'app

Initialise le context avec le $(.container principal), le $(.menu), url vers le serveur,....

Definis les différents routes dans l'application avec la/les functions gérant celle-ci.

Met en route le router pour l'ecoute sur le hash.



Base.js :
=======
Regroupe les petits actions communes à tous les autres fichiers js restant.

Se base sur une structure MVC.
J'ai définis une variable globale servant de namespace et return cette var en dernière ligne du fichier.


Contient une fonction repondant à l'Event 'Erreur'


La partie vue possède une fonction afficher(nomVue,nomaction,callback) responsable de l'affichage de la vue demandée en utilisant le Context ou via une requete AJAX sinon affiche par-défaut l'accueil.



Les autres fichiers restants respectent aussi la structure MVC avec utilisation d'une variable globale servant de namespace à retur en fin de ficher.

