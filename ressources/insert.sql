--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.6
-- Dumped by pg_dump version 9.5.2

-- Started on 2016-05-06 01:09:36

SET search_path = gerasmus, pg_catalog;

--
-- TOC entry 2181 (class 0 OID 213865)
-- Dependencies: 190
-- Data for Name: departements; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO departements VALUES (1, 'BIN', 'Informatique de gestion', 1);
INSERT INTO departements VALUES (2, 'BIM', 'Imagerie médicale', 1);
INSERT INTO departements VALUES (3, 'BDI', 'Diététique', 1);
INSERT INTO departements VALUES (4, 'BCH', 'Chimie', 1);
INSERT INTO departements VALUES (5, 'BBM', 'Biologie médicale', 1);


--
-- TOC entry 2183 (class 0 OID 213876)
-- Dependencies: 192
-- Data for Name: utilisateurs; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO utilisateurs VALUES (1, 'Grol', '$2a$12$Avt0CUmnhRdAX9jCEZfnrOyAaAhUWr.U2O.adAlUhFbPVzBYHR7BO', 'Grolaux', 'Donatien', 1, 'donatien.grolaux@vinci.be', '2001-01-01 +02:00:00', true, NULL, 1);
INSERT INTO utilisateurs VALUES (2, 'Bleh', '$2a$12$Avt0CUmnhRdAX9jCEZfnrOyAaAhUWr.U2O.adAlUhFbPVzBYHR7BO', 'Lehmann', 'Brigitte', 1, 'brigitte.lehmann@vinci.be', '2001-01-01 +02:00:00', true, NULL, 1);
INSERT INTO utilisateurs VALUES (3, 'PierreK', '$2a$12$OTWvya/KwSCSTFajpqNMOO8KVlaTM4PI5urvYFgMeXO6C/GSNALSu', 'Kiroule', 'Pierre', 3, 'pierre.kiroule@student.vinci.be', '2014-09-01 +02:00:00', false, NULL, 1);
INSERT INTO utilisateurs VALUES (4, 'napa', '$2a$12$OTWvya/KwSCSTFajpqNMOO8KVlaTM4PI5urvYFgMeXO6C/GSNALSu', 'Pamousse', 'Namasse', 1, 'namasse.pamousse@student.vinci.be', '2013-07-12 +02:00:00', false, NULL, 1);
INSERT INTO utilisateurs VALUES (5, 'tota', '$2a$12$OTWvya/KwSCSTFajpqNMOO8KVlaTM4PI5urvYFgMeXO6C/GSNALSu', 'Tatilotetatou', 'Tonthe', 3, 'tonthe.tatilotetatou@student.vinci.be', '2016-09-01 +02:00:00', false, NULL, 1);


--
-- TOC entry 2195 (class 0 OID 213986)
-- Dependencies: 204
-- Data for Name: demandes_mobilites; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO demandes_mobilites VALUES (1, '01-05-16', 2016, 3, 1);
INSERT INTO demandes_mobilites VALUES (2, '01-05-16', 2016, 4, 1);
INSERT INTO demandes_mobilites VALUES (3, '01-05-16', 2016, 5, 1);


--
-- TOC entry 2191 (class 0 OID 213955)
-- Dependencies: 200
-- Data for Name: motifs_annulation; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--



--
-- TOC entry 2174 (class 0 OID 213826)
-- Dependencies: 183
-- Data for Name: programmes; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO programmes VALUES (1, 'Erasmus+', 1);
INSERT INTO programmes VALUES (2, 'Erabel', 1);
INSERT INTO programmes VALUES (3, 'FAME', 1);


--
-- TOC entry 2186 (class 0 OID 213906)
-- Dependencies: 195
-- Data for Name: pays; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO pays VALUES (1, 'AFG', 'Afghanistan', 3, 1);
INSERT INTO pays VALUES (2, 'ALA', 'Îles Aland', 3, 1);
INSERT INTO pays VALUES (3, 'ALB', 'Albanie', 3, 1);
INSERT INTO pays VALUES (4, 'DZA', 'Algérie', 3, 1);
INSERT INTO pays VALUES (5, 'ASM', 'Samoa Américaine', 3, 1);
INSERT INTO pays VALUES (6, 'AND', 'Andorre', 3, 1);
INSERT INTO pays VALUES (7, 'AGO', 'Angola', 3, 1);
INSERT INTO pays VALUES (8, 'AIA', 'Anguilla', 3, 1);
INSERT INTO pays VALUES (9, 'ATA', 'Antarctique', 3, 1);
INSERT INTO pays VALUES (10, 'ATG', 'Antigua-Et-Barbuda', 3, 1);
INSERT INTO pays VALUES (11, 'ARG', 'Argentine', 3, 1);
INSERT INTO pays VALUES (12, 'ARM', 'Arménie', 3, 1);
INSERT INTO pays VALUES (13, 'ABW', 'Aruba', 3, 1);
INSERT INTO pays VALUES (14, 'AUS', 'Australie', 3, 1);
INSERT INTO pays VALUES (15, 'AUT', 'Autriche', 1, 1);
INSERT INTO pays VALUES (16, 'AZE', 'Azerbaïdjan', 3, 1);
INSERT INTO pays VALUES (17, 'BHS', 'Bahamas', 3, 1);
INSERT INTO pays VALUES (18, 'BHR', 'Bahrain', 3, 1);
INSERT INTO pays VALUES (19, 'BGD', 'Bangladesh', 3, 1);
INSERT INTO pays VALUES (20, 'BRB', 'Barbade', 3, 1);
INSERT INTO pays VALUES (21, 'BLR', 'Belarus', 3, 1);
INSERT INTO pays VALUES (22, 'BEL', 'Belgique', 2, 1);
INSERT INTO pays VALUES (23, 'BLZ', 'Belize', 3, 1);
INSERT INTO pays VALUES (24, 'BEN', 'Benin', 3, 1);
INSERT INTO pays VALUES (25, 'BMU', 'Bermuda', 3, 1);
INSERT INTO pays VALUES (26, 'BTN', 'Bhoutan', 3, 1);
INSERT INTO pays VALUES (27, 'BOL', 'Bolivie', 3, 1);
INSERT INTO pays VALUES (28, 'BES', 'Bonaire , Saint-Eustache et Saba', 3, 1);
INSERT INTO pays VALUES (29, 'BIH', 'Bosnie Herzégovine', 3, 1);
INSERT INTO pays VALUES (30, 'BWA', 'Botswana', 3, 1);
INSERT INTO pays VALUES (31, 'BVT', 'Île Bouvet', 3, 1);
INSERT INTO pays VALUES (32, 'BRA', 'Brésil', 3, 1);
INSERT INTO pays VALUES (33, 'IOT', 'Territoire britannique de l''océan Indien', 3, 1);
INSERT INTO pays VALUES (34, 'BGR', 'Bulgarie', 1, 1);
INSERT INTO pays VALUES (35, 'BFA', 'Burkina Faso', 3, 1);
INSERT INTO pays VALUES (36, 'BDI', 'Burundi', 3, 1);
INSERT INTO pays VALUES (37, 'KHM', 'Cambodge', 3, 1);
INSERT INTO pays VALUES (38, 'CMR', 'Caméroun', 3, 1);
INSERT INTO pays VALUES (39, 'CAN', 'Canada', 3, 1);
INSERT INTO pays VALUES (40, 'CPV', 'Capo Verde', 3, 1);
INSERT INTO pays VALUES (41, 'CYM', 'Îles Caïmans', 3, 1);
INSERT INTO pays VALUES (42, 'CAF', 'République Centrafricaine', 3, 1);
INSERT INTO pays VALUES (43, 'TCD', 'Tchad', 3, 1);
INSERT INTO pays VALUES (44, 'CHL', 'Chili', 3, 1);
INSERT INTO pays VALUES (45, 'CHN', 'Chine', 3, 1);
INSERT INTO pays VALUES (46, 'CXR', 'Île de Noël', 3, 1);
INSERT INTO pays VALUES (47, 'CCK', 'Îles Cocos (Keeling)', 3, 1);
INSERT INTO pays VALUES (48, 'COL', 'Colombie', 3, 1);
INSERT INTO pays VALUES (49, 'COM', 'Comores', 3, 1);
INSERT INTO pays VALUES (50, 'COG', 'Congo', 3, 1);
INSERT INTO pays VALUES (51, 'COD', 'République Démocratique du Congo', 3, 1);
INSERT INTO pays VALUES (52, 'COK', 'Îles Cook', 3, 1);
INSERT INTO pays VALUES (53, 'CRI', 'Costa Rica', 3, 1);
INSERT INTO pays VALUES (54, 'CIV', 'Côte d''Ivoire', 3, 1);
INSERT INTO pays VALUES (55, 'HRV', 'Croatie', 1, 1);
INSERT INTO pays VALUES (56, 'CUB', 'Cuba', 3, 1);
INSERT INTO pays VALUES (57, 'CUW', 'Curacao', 3, 1);
INSERT INTO pays VALUES (58, 'CYP', 'Chypre', 1, 1);
INSERT INTO pays VALUES (59, 'CZE', 'Républiue Tchèque', 1, 1);
INSERT INTO pays VALUES (60, 'DNK', 'Danemark', 1, 1);
INSERT INTO pays VALUES (61, 'DJI', 'Djibouti', 3, 1);
INSERT INTO pays VALUES (62, 'DMA', 'Dominique', 3, 1);
INSERT INTO pays VALUES (63, 'DOM', 'République Dominicaine', 3, 1);
INSERT INTO pays VALUES (64, 'ECU', 'Equateur', 3, 1);
INSERT INTO pays VALUES (65, 'EGY', 'Egypte', 3, 1);
INSERT INTO pays VALUES (66, 'SLV', 'El Salvador', 3, 1);
INSERT INTO pays VALUES (67, 'GNQ', 'Guinée Equatoriale', 3, 1);
INSERT INTO pays VALUES (68, 'ERI', 'Érythrée', 3, 1);
INSERT INTO pays VALUES (69, 'EST', 'Estonie', 1, 1);
INSERT INTO pays VALUES (70, 'ETH', 'Éthiopie', 3, 1);
INSERT INTO pays VALUES (71, 'FLK', 'Îles Malouines', 3, 1);
INSERT INTO pays VALUES (72, 'FRO', 'Îles Féroé', 3, 1);
INSERT INTO pays VALUES (73, 'FJI', 'Fidji', 3, 1);
INSERT INTO pays VALUES (74, 'FIN', 'Finlande', 1, 1);
INSERT INTO pays VALUES (75, 'FRA', 'France', 1, 1);
INSERT INTO pays VALUES (76, 'GUF', 'Guinée Française', 3, 1);
INSERT INTO pays VALUES (77, 'PYF', 'Polynésie', 3, 1);
INSERT INTO pays VALUES (78, 'ATF', 'Terres Australes Françaises', 3, 1);
INSERT INTO pays VALUES (79, 'GAB', 'Gabon', 3, 1);
INSERT INTO pays VALUES (80, 'GMB', 'Gambie', 3, 1);
INSERT INTO pays VALUES (81, 'GEO', 'Géorgie', 3, 1);
INSERT INTO pays VALUES (82, 'DEU', 'Allemagne', 1, 1);
INSERT INTO pays VALUES (83, 'GHA', 'Ghana', 3, 1);
INSERT INTO pays VALUES (84, 'GIB', 'Gibraltar', 3, 1);
INSERT INTO pays VALUES (85, 'GRC', 'Grèce', 1, 1);
INSERT INTO pays VALUES (86, 'GRL', 'Groenland', 3, 1);
INSERT INTO pays VALUES (87, 'GRD', 'Grenade', 3, 1);
INSERT INTO pays VALUES (88, 'GLP', 'Guadeloupe', 3, 1);
INSERT INTO pays VALUES (89, 'GUM', 'Guam', 3, 1);
INSERT INTO pays VALUES (90, 'GTM', 'Guatemala', 3, 1);
INSERT INTO pays VALUES (91, 'GGY', 'Guernesey', 3, 1);
INSERT INTO pays VALUES (92, 'GIN', 'Guinée', 3, 1);
INSERT INTO pays VALUES (93, 'GNB', 'Guinée-Bissau', 3, 1);
INSERT INTO pays VALUES (94, 'GUY', 'Guyane', 3, 1);
INSERT INTO pays VALUES (95, 'HTI', 'Haïti', 3, 1);
INSERT INTO pays VALUES (96, 'HMD', 'Îles Heard-et-MacDonald', 3, 1);
INSERT INTO pays VALUES (97, 'VAT', 'Saint-Siège', 3, 1);
INSERT INTO pays VALUES (98, 'HND', 'Honduras', 3, 1);
INSERT INTO pays VALUES (99, 'HKG', 'Hong Kong', 3, 1);
INSERT INTO pays VALUES (100, 'HUN', 'Hongrie', 1, 1);
INSERT INTO pays VALUES (101, 'ISL', 'Islande', 1, 1);
INSERT INTO pays VALUES (102, 'IND', 'Inde', 3, 1);
INSERT INTO pays VALUES (103, 'IDN', 'Indonésie', 3, 1);
INSERT INTO pays VALUES (104, 'IRN', 'Iran', 3, 1);
INSERT INTO pays VALUES (105, 'IRQ', 'Irak', 3, 1);
INSERT INTO pays VALUES (106, 'IRL', 'Irlande', 1, 1);
INSERT INTO pays VALUES (107, 'IMN', 'Île de Man', 3, 1);
INSERT INTO pays VALUES (108, 'ISR', 'Israël', 3, 1);
INSERT INTO pays VALUES (109, 'ITA', 'Italie', 1, 1);
INSERT INTO pays VALUES (110, 'JAM', 'Jamaïque', 3, 1);
INSERT INTO pays VALUES (111, 'JPN', 'Japon', 3, 1);
INSERT INTO pays VALUES (112, 'JEY', 'Bailliage de Jersey', 3, 1);
INSERT INTO pays VALUES (113, 'JOR', 'Jordanie', 3, 1);
INSERT INTO pays VALUES (114, 'KAZ', 'Kazakhstan', 3, 1);
INSERT INTO pays VALUES (115, 'KEN', 'Kenya', 3, 1);
INSERT INTO pays VALUES (116, 'KIR', 'Kiribati', 3, 1);
INSERT INTO pays VALUES (117, 'PRK', 'République populaire démocratique de Corée', 3, 1);
INSERT INTO pays VALUES (118, 'KOR', 'République de Corée', 3, 1);
INSERT INTO pays VALUES (119, 'KWT', 'Koweit', 3, 1);
INSERT INTO pays VALUES (120, 'KGZ', 'Kirghizistan', 3, 1);
INSERT INTO pays VALUES (121, 'LAO', 'République démocratique populaire lao', 3, 1);
INSERT INTO pays VALUES (122, 'LVA', 'Lettonie', 1, 1);
INSERT INTO pays VALUES (123, 'LBN', 'Liban', 3, 1);
INSERT INTO pays VALUES (124, 'LBR', 'Libéria', 3, 1);
INSERT INTO pays VALUES (125, 'LBY', 'Lybie', 3, 1);
INSERT INTO pays VALUES (126, 'LIE', 'Liechstenstein', 1, 1);
INSERT INTO pays VALUES (127, 'LTU', 'Lituanie', 1, 1);
INSERT INTO pays VALUES (128, 'LUX', 'Luxembourg', 1, 1);
INSERT INTO pays VALUES (129, 'MAC', 'Macao', 3, 1);
INSERT INTO pays VALUES (130, 'MKD', 'Ancienne république yougoslave de Macédonie', 1, 1);
INSERT INTO pays VALUES (131, 'MDG', 'Madagascar', 3, 1);
INSERT INTO pays VALUES (132, 'MWI', 'Malawi', 3, 1);
INSERT INTO pays VALUES (133, 'MYS', 'Malaisie', 3, 1);
INSERT INTO pays VALUES (134, 'MDV', 'Maldives', 3, 1);
INSERT INTO pays VALUES (135, 'MLI', 'Mali', 3, 1);
INSERT INTO pays VALUES (136, 'MLT', 'Malte', 1, 1);
INSERT INTO pays VALUES (137, 'MHL', 'Îles Marshall', 3, 1);
INSERT INTO pays VALUES (138, 'MTQ', 'Martinique', 3, 1);
INSERT INTO pays VALUES (139, 'MRT', 'Mauritanie', 3, 1);
INSERT INTO pays VALUES (140, 'MYT', 'Mayottes', 3, 1);
INSERT INTO pays VALUES (141, 'MEX', 'Mexique', 3, 1);
INSERT INTO pays VALUES (142, 'FSM', 'États fédérés de Micronésie', 3, 1);
INSERT INTO pays VALUES (143, 'MDA', 'Moldavie', 3, 1);
INSERT INTO pays VALUES (144, 'MCO', 'Monaco', 3, 1);
INSERT INTO pays VALUES (145, 'MNG', 'Mongolie', 3, 1);
INSERT INTO pays VALUES (146, 'MNE', 'Montenegro', 3, 1);
INSERT INTO pays VALUES (147, 'MSR', 'Monserrat', 3, 1);
INSERT INTO pays VALUES (148, 'MAR', 'Maroc', 3, 1);
INSERT INTO pays VALUES (149, 'MOZ', 'Mozambique', 3, 1);
INSERT INTO pays VALUES (150, 'MMR', 'Myanmar', 3, 1);
INSERT INTO pays VALUES (151, 'NAM', 'Namibie', 3, 1);
INSERT INTO pays VALUES (152, 'NRU', 'Nauru', 3, 1);
INSERT INTO pays VALUES (153, 'NPL', 'Népal', 3, 1);
INSERT INTO pays VALUES (154, 'NLD', 'Pays-Bas', 1, 1);
INSERT INTO pays VALUES (155, 'NCL', 'Nouvelle-Calédonie', 3, 1);
INSERT INTO pays VALUES (156, 'NZL', 'Nouvelle-Zélande', 3, 1);
INSERT INTO pays VALUES (157, 'NIC', 'Nicaragua', 3, 1);
INSERT INTO pays VALUES (158, 'NER', 'Niger', 3, 1);
INSERT INTO pays VALUES (159, 'NGA', 'Nigéria', 3, 1);
INSERT INTO pays VALUES (160, 'NIU', 'Niue', 3, 1);
INSERT INTO pays VALUES (161, 'NFK', 'Île Norfolk', 3, 1);
INSERT INTO pays VALUES (162, 'MNP', 'Îles Marianne du Nord', 3, 1);
INSERT INTO pays VALUES (163, 'NOR', 'Norvège', 1, 1);
INSERT INTO pays VALUES (164, 'OMN', 'Oman', 3, 1);
INSERT INTO pays VALUES (165, 'PAK', 'Pakistan', 3, 1);
INSERT INTO pays VALUES (166, 'PLW', 'République des Palaos', 3, 1);
INSERT INTO pays VALUES (167, 'PSE', 'État de Palestine', 3, 1);
INSERT INTO pays VALUES (168, 'PAN', 'Panama', 3, 1);
INSERT INTO pays VALUES (169, 'PNG', 'Papouasie Nouvelle Guinée', 3, 1);
INSERT INTO pays VALUES (170, 'PRY', 'Paraguay', 3, 1);
INSERT INTO pays VALUES (171, 'PER', 'Pérou', 3, 1);
INSERT INTO pays VALUES (172, 'PHL', 'Philippines', 3, 1);
INSERT INTO pays VALUES (173, 'PCN', 'Îles Pitcairn', 3, 1);
INSERT INTO pays VALUES (174, 'POL', 'Pologne', 1, 1);
INSERT INTO pays VALUES (175, 'PRT', 'Portugal', 1, 1);
INSERT INTO pays VALUES (176, 'PRI', 'Puerto Rico', 3, 1);
INSERT INTO pays VALUES (177, 'QAT', 'Qatar', 3, 1);
INSERT INTO pays VALUES (178, 'REU', 'Île de la Réunion', 3, 1);
INSERT INTO pays VALUES (179, 'ROU', 'Roumanie', 1, 1);
INSERT INTO pays VALUES (180, 'RUS', 'Russie', 3, 1);
INSERT INTO pays VALUES (181, 'RWA', 'Rwanda', 3, 1);
INSERT INTO pays VALUES (182, 'BLM', 'Saint-Barthélémy', 3, 1);
INSERT INTO pays VALUES (183, 'SHN', 'Sainte-Hélène, Ascension et Tristan da Cunha', 3, 1);
INSERT INTO pays VALUES (184, 'KNA', 'Saint-Christophe-et-Niévès', 3, 1);
INSERT INTO pays VALUES (185, 'LCA', 'Saint-Lucia', 3, 1);
INSERT INTO pays VALUES (186, 'MAF', 'Saint-Martin', 3, 1);
INSERT INTO pays VALUES (187, 'SPM', 'Saint-Pierre-et-Miquelon', 3, 1);
INSERT INTO pays VALUES (188, 'VCT', 'Saint-Vincent-et-les-Grenadines', 3, 1);
INSERT INTO pays VALUES (189, 'WSM', 'Samoa', 3, 1);
INSERT INTO pays VALUES (190, 'SMR', 'Saint-Marin', 3, 1);
INSERT INTO pays VALUES (191, 'STP', 'Sao Tomé et Principe', 3, 1);
INSERT INTO pays VALUES (192, 'SAU', 'Arabie Saoudite', 3, 1);
INSERT INTO pays VALUES (193, 'SEN', 'Sénégal', 3, 1);
INSERT INTO pays VALUES (194, 'SRB', 'Serbie', 3, 1);
INSERT INTO pays VALUES (195, 'SYC', 'Seychelles', 3, 1);
INSERT INTO pays VALUES (196, 'SLE', 'Sierra Léone', 3, 1);
INSERT INTO pays VALUES (197, 'SGP', 'Singapour', 3, 1);
INSERT INTO pays VALUES (198, 'SXM', 'Sint Maarten', 3, 1);
INSERT INTO pays VALUES (199, 'SVK', 'Slovaquie', 1, 1);
INSERT INTO pays VALUES (200, 'SVN', 'Slovénie', 1, 1);
INSERT INTO pays VALUES (201, 'SLB', 'Îles Salomon', 3, 1);
INSERT INTO pays VALUES (202, 'SOM', 'Somalie', 3, 1);
INSERT INTO pays VALUES (203, 'ZAF', 'Afrique du Sud', 3, 1);
INSERT INTO pays VALUES (204, 'SGS', 'Géorgie du Sud et les îles Sandwich du Sud', 3, 1);
INSERT INTO pays VALUES (205, 'SSD', 'Soudan du Sud', 3, 1);
INSERT INTO pays VALUES (206, 'ESP', 'Espagne', 1, 1);
INSERT INTO pays VALUES (207, 'LKA', 'Sri Lanka', 3, 1);
INSERT INTO pays VALUES (208, 'SDN', 'Soudan', 3, 1);
INSERT INTO pays VALUES (209, 'SUR', 'Suriname', 3, 1);
INSERT INTO pays VALUES (210, 'SJM', 'Svalbard et Jan Mayen', 3, 1);
INSERT INTO pays VALUES (211, 'SWZ', 'Swaziland', 3, 1);
INSERT INTO pays VALUES (212, 'SWE', 'Suède', 1, 1);
INSERT INTO pays VALUES (213, 'CHE', 'Suisse', 3, 1);
INSERT INTO pays VALUES (214, 'SYR', 'Syrie', 3, 1);
INSERT INTO pays VALUES (215, 'TWN', 'Taïwan', 3, 1);
INSERT INTO pays VALUES (216, 'TJK', 'Tadjikistan', 3, 1);
INSERT INTO pays VALUES (217, 'TZA', 'République-Unie de Tanzanie', 3, 1);
INSERT INTO pays VALUES (218, 'THA', 'Thaïlande', 3, 1);
INSERT INTO pays VALUES (219, 'TLS', 'Timor Oriental', 3, 1);
INSERT INTO pays VALUES (220, 'TGO', 'Togo', 3, 1);
INSERT INTO pays VALUES (221, 'TKL', 'Tokelau', 3, 1);
INSERT INTO pays VALUES (222, 'TON', 'Tonga', 3, 1);
INSERT INTO pays VALUES (223, 'TTO', 'Trinité-et-Tobago', 3, 1);
INSERT INTO pays VALUES (224, 'TUN', 'Tunisie', 3, 1);
INSERT INTO pays VALUES (225, 'TUR', 'Turquie', 1, 1);
INSERT INTO pays VALUES (226, 'TKM', 'Turkménistan', 3, 1);
INSERT INTO pays VALUES (227, 'TCA', 'Îles Turques-et-Caïques', 3, 1);
INSERT INTO pays VALUES (228, 'TUV', 'Tuvalu', 3, 1);
INSERT INTO pays VALUES (229, 'UGA', 'Ouganda', 3, 1);
INSERT INTO pays VALUES (230, 'UKR', 'Ukraine', 3, 1);
INSERT INTO pays VALUES (231, 'ARE', 'Emirats Arabes Unis', 3, 1);
INSERT INTO pays VALUES (232, 'GBR', 'Royaume-Uni', 1, 1);
INSERT INTO pays VALUES (233, 'USA', 'États Unis d''Amérique', 3, 1);
INSERT INTO pays VALUES (234, 'UMI', 'Îles mineures éloignées des États-Unis', 3, 1);
INSERT INTO pays VALUES (235, 'URY', 'Uruguay', 3, 1);
INSERT INTO pays VALUES (236, 'UZB', 'Ouzbékistan', 3, 1);
INSERT INTO pays VALUES (237, 'VUT', 'Vanuatu', 3, 1);
INSERT INTO pays VALUES (238, 'VEN', 'Vénézuela', 3, 1);
INSERT INTO pays VALUES (239, 'VNM', 'Viêt-Nam', 3, 1);
INSERT INTO pays VALUES (240, 'VGB', 'Îles Vierges britanniques', 3, 1);
INSERT INTO pays VALUES (241, 'VIR', 'Îles Vierges américaines', 3, 1);
INSERT INTO pays VALUES (242, 'WLF', 'Wallis-et-Futuna', 3, 1);
INSERT INTO pays VALUES (243, 'ESH', 'Sahara Occidental', 3, 1);
INSERT INTO pays VALUES (244, 'YEM', 'Yémen', 3, 1);
INSERT INTO pays VALUES (245, 'ZMB', 'Zambie', 3, 1);
INSERT INTO pays VALUES (246, 'ZWE', 'Zimbabwe', 3, 1);


--
-- TOC entry 2188 (class 0 OID 213919)
-- Dependencies: 197
-- Data for Name: partenaires; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO partenaires VALUES (1, 'Dublin Institute of Technology', 'Dublin Institute of Technology', 'Dublin Institute of Technology', 'TGE', 'School of Computing', 2000, 'DIT International Office, 143-149 Rathmines Road, Rathmines', 106, NULL, NULL, 'Dublin', 'peter.dalton@dit.ie', 'http://www.dit.ie', NULL, NULL, true, 1);
INSERT INTO partenaires VALUES (2, 'UNIVERSITY COLLEGE LEUVEN LIMBURG', 'UNIVERSITY COLLEGE LEUVEN LIMBURG', 'UNIVERSITY COLLEGE LEUVEN LIMBURG', NULL, NULL, 0, NULL, 22, 'Vlaams Brabant', NULL, 'Leuven', 'Griet.tservranckx@ucll.be', 'http://www.ucll.be/', '(32) 16 375 245', NULL, true, 1);
INSERT INTO partenaires VALUES (3, 'Université du Sanglier', 'Université du Sanglier', 'Université du Sanglier', 'TGE', 'Une ardeur d''avance', 1984, NULL, 128, 'Luxembourg', NULL, 'Jamoigne', 'jamoigne@lux.be', NULL, NULL, 2, true, 1);


--
-- TOC entry 2176 (class 0 OID 213834)
-- Dependencies: 185
-- Data for Name: types_programme; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO types_programme VALUES (1, 'SMP', 1);
INSERT INTO types_programme VALUES (2, 'SMS', 1);


--
-- TOC entry 2197 (class 0 OID 213999)
-- Dependencies: 206
-- Data for Name: choix_mobilites; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO choix_mobilites VALUES (1, 1, 'INTRO', NULL, 1, false, 1, 2, 1, NULL, 1);
INSERT INTO choix_mobilites VALUES (1, 2, 'INTRO', NULL, 1, false, 2, 2, 3, NULL, 1);
INSERT INTO choix_mobilites VALUES (1, 3, 'INTRO', 235, 1, false, 1, 1, NULL, NULL, 1);
INSERT INTO choix_mobilites VALUES (2, 4, 'INTRO', NULL, 1, false, 2, 2, 2, NULL, 1);
INSERT INTO choix_mobilites VALUES (2, 5, 'INTRO', NULL, 1, false, 2, 1, NULL, NULL, 1);
INSERT INTO choix_mobilites VALUES (2, 6, 'INTRO', 216, 1, false, 1, 1, NULL, NULL, 1);
INSERT INTO choix_mobilites VALUES (3, 7, 'INTRO', NULL, 1, false, 2, 2, 2, NULL, 1);
INSERT INTO choix_mobilites VALUES (3, 8, 'INTRO', NULL, 1, false, 1, 1, NULL, NULL, 1);


--
-- TOC entry 2204 (class 0 OID 0)
-- Dependencies: 205
-- Name: choix_mobilites_num_preference_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('choix_mobilites_num_preference_seq', 8, true);


--
-- TOC entry 2205 (class 0 OID 0)
-- Dependencies: 203
-- Name: demandes_mobilites_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('demandes_mobilites_id_seq', 3, true);


--
-- TOC entry 2206 (class 0 OID 0)
-- Dependencies: 189
-- Name: departements_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('departements_id_seq', 5, false);


--
-- TOC entry 2189 (class 0 OID 213938)
-- Dependencies: 198
-- Data for Name: departements_partenaire; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO departements_partenaire VALUES (1, 1, 1);
INSERT INTO departements_partenaire VALUES (1, 3, 1);
INSERT INTO departements_partenaire VALUES (3, 2, 1);
INSERT INTO departements_partenaire VALUES (4, 1, 1);


--
-- TOC entry 2193 (class 0 OID 213968)
-- Dependencies: 202
-- Data for Name: documents; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO documents VALUES (1, 'Contrat de bourse', 'DEPART', NULL, NULL, 1);
INSERT INTO documents VALUES (2, 'Convention de stage', 'DEPART', NULL, NULL, 1);
INSERT INTO documents VALUES (3, 'Charte étudiant', 'DEPART', NULL, NULL, 1);
INSERT INTO documents VALUES (4, 'Tests linguistiques', 'DEPART', 1, NULL, 1);
INSERT INTO documents VALUES (5, 'Engagement', 'DEPART', NULL, NULL, 1);
INSERT INTO documents VALUES (6, 'Attestation', 'RETOUR', NULL, NULL, 1);
INSERT INTO documents VALUES (7, 'Relevé de notes', 'RETOUR', NULL, NULL, 1);
INSERT INTO documents VALUES (8, 'Certificat de stage', 'RETOUR', NULL, NULL, 1);
INSERT INTO documents VALUES (9, 'Rapport final', 'RETOUR', NULL, NULL, 1);
INSERT INTO documents VALUES (10, 'Tests linguistiques', 'RETOUR', 1, NULL, 1);


--
-- TOC entry 2207 (class 0 OID 0)
-- Dependencies: 201
-- Name: documents_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('documents_id_seq', 10, true);


--
-- TOC entry 2199 (class 0 OID 214050)
-- Dependencies: 208
-- Data for Name: documents_signes; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--



--
-- TOC entry 2184 (class 0 OID 213891)
-- Dependencies: 193
-- Data for Name: informations_etudiants; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--



--
-- TOC entry 2178 (class 0 OID 213842)
-- Dependencies: 187
-- Data for Name: logiciels; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--

INSERT INTO logiciels VALUES (1, 'ProEco', 1);
INSERT INTO logiciels VALUES (2, 'Mobility Tool', 1);
INSERT INTO logiciels VALUES (3, 'Mobi', 1);


--
-- TOC entry 2198 (class 0 OID 214035)
-- Dependencies: 207
-- Data for Name: logiciels_encodes; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--



--
-- TOC entry 2208 (class 0 OID 0)
-- Dependencies: 186
-- Name: logiciels_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('logiciels_id_seq', 3, false);


--
-- TOC entry 2209 (class 0 OID 0)
-- Dependencies: 199
-- Name: motifs_annulation_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('motifs_annulation_id_seq', 1, false);


--
-- TOC entry 2210 (class 0 OID 0)
-- Dependencies: 196
-- Name: partenaires_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('partenaires_id_seq', 3, false);


--
-- TOC entry 2211 (class 0 OID 0)
-- Dependencies: 194
-- Name: pays_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('pays_id_seq', 246, true);


--
-- TOC entry 2212 (class 0 OID 0)
-- Dependencies: 182
-- Name: programmes_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('programmes_id_seq', 3, false);


--
-- TOC entry 2179 (class 0 OID 213848)
-- Dependencies: 188
-- Data for Name: programmes_logiciel; Type: TABLE DATA; Schema: gerasmus; Owner: kodjo_adegnon
--



--
-- TOC entry 2213 (class 0 OID 0)
-- Dependencies: 184
-- Name: types_programme_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('types_programme_id_seq', 3, false);


--
-- TOC entry 2214 (class 0 OID 0)
-- Dependencies: 191
-- Name: utilisateurs_id_seq; Type: SEQUENCE SET; Schema: gerasmus; Owner: kodjo_adegnon
--

SELECT pg_catalog.setval('utilisateurs_id_seq', 5, true);


-- Completed on 2016-05-06 01:09:44

--
-- PostgreSQL database dump complete
--

