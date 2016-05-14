-- Crée le schéma sur le compte pgAdmin III

DROP SCHEMA IF EXISTS gerasmus CASCADE;

CREATE SCHEMA gerasmus;

--ALTER SCHEMA gerasmus OWNER TO kodjo_adegnon;

-- Table programme (SMS,SMP)
CREATE TABLE gerasmus.programmes(
	id SERIAL PRIMARY KEY,
	nom VARCHAR(255) NOT NULL,
	num_version INTEGER NOT NULL
);

-- Table types_programmes (Erabel,FAME, Erasmus)
CREATE TABLE gerasmus.types_programme(
	id SERIAL PRIMARY KEY,
	nom VARCHAR(255) NOT NULL,
	num_version INTEGER NOT NULL
);


-- Table logiciels
CREATE TABLE gerasmus.logiciels(
	id SERIAL PRIMARY KEY,
	nom VARCHAR(255) NOT NULL,
	num_version INTEGER NOT NULL
);

-- Table programmes_logiciel 
CREATE TABLE gerasmus.programmes_logiciel(
	id_logiciel INTEGER NOT NULL REFERENCES gerasmus.logiciels(id),
	id_programme INTEGER NOT NULL REFERENCES gerasmus.programmes(id),
	num_version INTEGER NOT NULL,
	CONSTRAINT pk_prog_logi PRIMARY KEY(id_logiciel,id_programme)
);
	

-- Table departements
CREATE TABLE gerasmus.departements(
	id SERIAL PRIMARY KEY,
	code VARCHAR(255) NOT NULL,
	nom VARCHAR(255) NOT NULL,
	num_version INTEGER NOT NULL
);



-- Table utilisateurs
CREATE TABLE gerasmus.utilisateurs(
	id SERIAL PRIMARY KEY,
	pseudo VARCHAR(255) NOT NULL ,
	mdp VARCHAR(255) NOT NULL,
	nom VARCHAR(255) NOT NULL,
	prenom VARCHAR(255) NOT NULL,
	departement INTEGER NOT NULL REFERENCES gerasmus.departements(id),
	email VARCHAR(255)  NOT NULL,
	date_inscription VARCHAR(255) NOT NULL,
	prof BOOLEAN DEFAULT FALSE,
	avatar BYTEA NULL,
	num_version INTEGER NOT NULL
);

-- Table informations_etudiants
CREATE TABLE gerasmus.informations_etudiants(
	id INTEGER NOT NULL REFERENCES gerasmus.utilisateurs(id),
	civilite VARCHAR(4) NOT NULL, --  Mr, Mme, ....
	nationalite VARCHAR(40) NOT NULL ,
	adresse VARCHAR(255) NOT NULL ,
	tel VARCHAR(30) NOT NULL ,
	email VARCHAR(150) NOT NULL ,
	sexe VARCHAR(1) NOT NULL ,
	compte_bancaire VARCHAR(40) NOT NULL ,
	compte_titulaire VARCHAR(150) NOT NULL 	,
	nom_banque VARCHAR(255) NOT NULL,
	code_bic VARCHAR(11) NOT NULL , -- Code BIC comporte généralement 8 caractères, mais peut en contenir 11
	date_naissance VARCHAR(40) NOT NULL ,
	nb_annees_reussies INTEGER NOT NULL ,
	num_version INTEGER NOT NULL,
	CONSTRAINT pk_info_etu PRIMARY KEY(id)
);

-- Table pays
CREATE TABLE gerasmus.pays(
	id SERIAL PRIMARY KEY,
	code_iso VARCHAR(3) NOT NULL,
	nom VARCHAR(255) NOT NULL,
	programme INTEGER REFERENCES gerasmus.programmes(id),
	num_version INTEGER NOT NULL
);

-- Table partenaires
CREATE TABLE gerasmus.partenaires(
	id SERIAL PRIMARY KEY,
	nom_legal VARCHAR(255) NOT NULL,
	nom_affaire VARCHAR(255) NOT NULL,
	nom_complet VARCHAR(255),
	type_organisation VARCHAR(5) NULL,
	departement VARCHAR(255) NULL,
	nb_employe INTEGER,
	adresse VARCHAR(255) NULL,
	pays INTEGER NOT NULL REFERENCES gerasmus.pays(id),
	region VARCHAR(255) NULL,
	cp VARCHAR(25),
	ville VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	site_web VARCHAR(255) NULL,
	tel VARCHAR(255) NULL,
	createur INTEGER NULL REFERENCES gerasmus.utilisateurs(id),
	visible BOOLEAN ,
	num_version INTEGER NOT NULL
);


-- Table departements_partenaire
CREATE TABLE gerasmus.departements_partenaire(
	id_departement INTEGER NOT NULL REFERENCES gerasmus.departements(id),
	id_partenaire INTEGER NOT NULL REFERENCES gerasmus.partenaires(id),
	num_version INTEGER NOT NULL,
	CONSTRAINT pk_depart_part PRIMARY KEY(id_departement,id_partenaire)
);


-- Table motifs_annulation
CREATE TABLE gerasmus.motifs_annulation(
	id SERIAL PRIMARY KEY,
	createur INTEGER NOT NULL REFERENCES gerasmus.utilisateurs(id),
	motif VARCHAR(255) NOT NULL,
	num_version INTEGER NOT NULL
);

-- Table documents
CREATE TABLE gerasmus.documents(
	id SERIAL PRIMARY KEY,
	nom VARCHAR(255) NOT NULL ,
	genre VARCHAR(10) NOT NULL ,
	programme INTEGER NULL REFERENCES gerasmus.programmes(id), -- si un document est utile pour tous les programme alors on met a NULL sinon au programme qui le refere --
	type_programme INTEGER NULL REFERENCES gerasmus.types_programme(id), -- idem si dessus --
	num_version INTEGER NOT NULL
);

-- Table demandes_mobilites
CREATE TABLE gerasmus.demandes_mobilites(
	id SERIAL PRIMARY KEY,
	date_introduction VARCHAR(40) NOT NULL,
	annee_academique INTEGER NOT NULL , -- On retient uniquement la partie basse d'une année académique. Par exemple pour l'année académique 2015-2016 on retiendra 2015.
	etudiant INTEGER NOT NULL REFERENCES gerasmus.utilisateurs(id),
	num_version INTEGER NOT NULL
);

-- Table choix_mobilites
CREATE TABLE gerasmus.choix_mobilites(
	candidature INTEGER NOT NULL REFERENCES gerasmus.demandes_mobilites(id),
	num_preference SERIAL ,
	etat VARCHAR(30) NOT NULL,
	pays INTEGER NULL REFERENCES gerasmus.pays(id), --localite VARCHAR(255),--
	quadri INTEGER NOT NULL,
	annule BOOLEAN NOT NULL,
	programme INTEGER NOT NULL REFERENCES gerasmus.programmes(id),
	type_programme INTEGER NOT NULL REFERENCES gerasmus.types_programme(id),
	partenaire INTEGER NULL REFERENCES gerasmus.partenaires(id),
	motif_annulation INTEGER NULL REFERENCES gerasmus.motifs_annulation(id),
	num_version INTEGER NOT NULL,
	CONSTRAINT pk_choix_mob PRIMARY KEY(candidature,num_preference)
);


-- Table logiciels_encodes
CREATE TABLE gerasmus.logiciels_encodes(
	id_log INTEGER NOT NULL REFERENCES gerasmus.logiciels(id),
	id_choix_cand INTEGER NOT NULL,
	id_choix_pref INTEGER NOT NULL,
	num_version INTEGER NOT NULL,
	CONSTRAINT fk_logi_encodees FOREIGN KEY (id_choix_cand,id_choix_pref) REFERENCES gerasmus.choix_mobilites(candidature,num_preference),
	CONSTRAINT pk_logi_encodees PRIMARY KEY(id_log,id_choix_cand,id_choix_pref)
);




-- Table documents_signe
CREATE TABLE gerasmus.documents_signes(
	id_doc INTEGER NOT NULL REFERENCES gerasmus.documents(id),
	id_choix_cand INTEGER NOT NULL,
	id_choix_pref INTEGER NOT NULL,
	num_version INTEGER NOT NULL,
	CONSTRAINT fk_doc_signes FOREIGN KEY (id_choix_cand,id_choix_pref) REFERENCES gerasmus.choix_mobilites(num_preference, candidature),
	CONSTRAINT pk_doc_signes PRIMARY KEY(id_doc,id_choix_cand,id_choix_pref)
);



-- Authorisation de la connection à la DB
-- Les grants commentés sont pour ceux qui n'ont pas de db actuellement.

-- GRANT CONNECT ON DATABASE dbchristopher_ermgodts TO candy_coenen;
-- GRANT CONNECT ON DATABASE dbchristopher_ermgodts TO kodjo_adegnon;
-- GRANT CONNECT ON DATABASE dbchristopher_ermgodts TO aurelien_lefebvre;
-- GRANT CONNECT ON DATABASE dbchristopher_ermgodts TO junior_maduka;

-- Authorisation d'utiliser le schéma
GRANT ALL ON SCHEMA gerasmus TO candy_coenen;
GRANT ALL ON SCHEMA gerasmus TO kodjo_adegnon;
GRANT ALL ON SCHEMA gerasmus TO aurelien_lefebvre;
GRANT ALL ON SCHEMA gerasmus TO christopher_ermgodts;

GRANT CONNECT ON DATABASE dbkodjo_adegnon TO public;


-- Authorisation de tout manipuler
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gerasmus TO kodjo_adegnon;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gerasmus TO aurelien_lefebvre;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gerasmus TO christopher_ermgodts;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gerasmus TO candy_coenen;

