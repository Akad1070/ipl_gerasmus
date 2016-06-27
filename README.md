# Explication sur AppServlet


Le chemin (URL) vers l'API est  ...:8080/api/gerasmus/v1

L'AppServlet ne répond qu'a 2 type de requête {GET, POST}.

La methode GET renvoie, après verification de l'authentification, les vues HTML pour le front-end.

La methode POST sert à obtenir et modifier des données en rapport avec l'application.

Le body de la requete de type POST doit toujours contenir {vue,action ,input_data} non-requis avec un paramètre 'req'.

La methode POST peut aussi prendre un paramètre 'req' pour obtenir des données non-relatives à une vue particulière telle que
les différents departements, la liste des types d'organisations possibles pour un partenaire, liste des pays disponibles,
verifier si l'user courrant est bien authifié.

## Ex:

Voici le format du body de la requête pour Connecter un utilisateur
```js
{
    "vue":"quidam",
    "action":"connexion",
    "input_data":"{\"identifiant\":\"prenom.nom@vinci.be\",\"mdp\":\"MdpAdmin123\"}"
}
```
ou Lister les mobiltés en tant que professeur
```js
{
    "vue":"mobilite",
    "action":"list",
    "input_data":"{}"
}
```

Les actions de type "maj" requiert les nouveles data dans le 'input_data'.



# JavaScript


Je vais présenter les principaux composants de ce 'mini-framework'.


## _CORE.js

**Regroupe les fonctionnalités du framework & representé par le namespace 'Gerasmus'**

### Router
**Un syteme de router simple avec pris en charge de paramètre et d'alias pour une route.**

    - Reconnaissance par RegExp de l'URL.
    - Ecoute les changements du #hash dans la barre d'adresse.
    - Redirection vers un route ne particulier.
    - Assignation d'alias pour un chemin : #inscription dirige vers #/quidam/inscription


### Context
    - Le nom de l'application.
    - URL vers le serveur.
    - Toutes les vues visibles actuellement par l'utilisateur



### Events

Pour gerer la communication entre les couches. Basée sur le pattern PubSub

Un 'Event' dans l'app est caractérisé par une nom obligatoire avec une data ou une fonction pour la gérer quand elle est survient.


### Model
**Gere les appels AJAX et les données.**

    - Retourne une Promise, manipulable par les autres Model, après un succes de l'appel AJAX.
    - Gestion direct du msg d'erreur en cas de fail de la requete.
    - Communication inter-couche grâce aux Events.


### Controller
**Connait la couche Model et appelable par la Vue pour effectuer des actions des données du Model.**


### Vue

**Responsable de ce qui est visible pour l'utilisateur.**

    - Charge les vues et les menus dans le Context.
    - Notification de l'utilisateur avec un msg selon un type {SUCCESS, WARNING,ERROR}.



## _MAIN.js :

**Sert de boot pour l'app**

1. Initialise le context avec le $(.container principal), le $(.menu), URL vers le serveur,....
2. Met en route le router pour l'ecoute sur le hash.
3. Definit les différents routes dans l'application avec la/les functions gérant celle-ci.



Base.js :
=======
**Regroupe les petits actions communes à tous les autres fichiers js restant.**

Se base sur une structure MVC.
Variable globale servant de namespace et retourne celle-ci en dernière ligne du fichier.

Contient une fonction repondant à l'Event 'Erreur'

La partie vue possède une fonction afficher(nomVue,nomaction,callback) responsable de l'affichage de la vue demandée en utilisant le Context ou via une requete AJAX sinon affiche par-défaut l'accueil.


Les autres fichiers restants respectent aussi la structure MVC avec utilisation d'un nameespace à renvoyer en fin de ficher.



# License

(The MIT License)

Copyright (c) 2016 A. Kad <ak@d.mail>

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
