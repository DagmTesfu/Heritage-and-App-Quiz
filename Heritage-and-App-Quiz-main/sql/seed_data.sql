USE myprojectdb;

-- SHA-256 hash of "admin123"
INSERT INTO users (username, email, password_hash, role) VALUES
    ('admin', 'admin@discoverethiopia.local', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin');

-- SHA-256 hash of "user123"
INSERT INTO users (username, email, password_hash, role) VALUES
    ('demo_user', 'demo@discoverethiopia.local', 'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'user');

INSERT INTO heritage_sites (name, type, region, description, amazing_facts, image_path, added_by_admin_id) VALUES
                                                                                                               ('Lalibela Rock Churches', 'church', 'Amhara',
                                                                                                                'Lalibela is famous for eleven medieval monolithic churches carved from rock. The churches are still active places of worship and are one of Ethiopia''s most recognized UNESCO World Heritage sites.',
                                                                                                                'Built during the Zagwe dynasty; Some churches are connected by tunnels; The churches are divided into northern and southern groups; Bete Giyorgis is carved in a cross shape.',
                                                                                                                "lalibela.jpg", 1),
                                                                                                               ('Aksum Obelisks', 'archaeological', 'Tigray',
                                                                                                                'Aksum was the center of the ancient Aksumite Kingdom. Its tall carved stelae, royal tombs, and archaeological remains show the power of one of Africa''s great ancient civilizations.',
                                                                                                                'Aksum was a major trading empire; The tallest standing stele is more than 20 meters high; Aksum minted its own coins; Ethiopian tradition connects Aksum with the Ark of the Covenant.',
                                                                                                                "aksum.jpg", 1),
                                                                                                               ('Simien Mountains National Park', 'natural', 'Amhara',
                                                                                                                'The Simien Mountains are known for dramatic cliffs, deep valleys, and rare wildlife. The park protects unique species and some of Ethiopia''s most spectacular highland landscapes.',
                                                                                                                'Home to the gelada monkey; Ras Dashen is Ethiopia''s highest mountain; The park is a UNESCO natural heritage site; The Walia ibex is found only in Ethiopia.',
                                                                                                                "simien_mountains.png", 1);

INSERT INTO quiz_questions
(site_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
                                                                                                  (1, 'How many rock-hewn churches are in Lalibela?', '5', '11', '13', '7', 'B', 'Lalibela has eleven rock-hewn churches grouped into two main clusters.'),
                                                                                                  (1, 'Which dynasty is strongly linked with the building of Lalibela?', 'Zagwe', 'Solomonic', 'Aksumite', 'Gondarine', 'A', 'The churches are commonly associated with King Lalibela and the Zagwe dynasty.'),
                                                                                                  (1, 'Which Lalibela church is famous for its cross-shaped plan?', 'Bete Maryam', 'Bete Medhane Alem', 'Bete Giyorgis', 'Bete Gabriel', 'C', 'Bete Giyorgis is widely known for its cross-shaped design.'),
                                                                                                  (1, 'What type of heritage site is Lalibela in this app?', 'Natural', 'Church', 'City', 'Museum', 'B', 'The project models Lalibela as a church heritage site.'),
                                                                                                  (1, 'What material are Lalibela churches carved from?', 'Wood', 'Brick', 'Rock', 'Glass', 'C', 'They are rock-hewn churches carved directly from stone.'),

                                                                                                  (2, 'Aksum is most famous for which ancient monuments?', 'Castles', 'Obelisks and stelae', 'Rock churches', 'Hot springs', 'B', 'Aksum is famous for its tall carved stelae and obelisks.'),
                                                                                                  (2, 'Which ancient kingdom had Aksum as a major center?', 'Aksumite Kingdom', 'Mali Empire', 'Roman Empire', 'Zulu Kingdom', 'A', 'Aksum was the center of the Aksumite Kingdom.'),
                                                                                                  (2, 'In which Ethiopian region is Aksum located?', 'Oromia', 'Tigray', 'Somali', 'Sidama', 'B', 'Aksum is located in the Tigray region.'),
                                                                                                  (2, 'What did ancient Aksum mint as a sign of its economy?', 'Paper money', 'Coins', 'Shell beads only', 'Gold crowns only', 'B', 'The Aksumite Kingdom minted its own coins.'),
                                                                                                  (2, 'What site type is Aksum in this app?', 'Archaeological', 'Church', 'Natural', 'City park', 'A', 'Aksum is represented as an archaeological heritage site.'),

                                                                                                  (3, 'What rare animal is strongly associated with the Simien Mountains?', 'Walia ibex', 'Polar bear', 'Kangaroo', 'Tiger', 'A', 'The Walia ibex is native to Ethiopia and protected in the Simien Mountains.'),
                                                                                                  (3, 'Which mountain in the Simien range is Ethiopia''s highest?', 'Mount Entoto', 'Ras Dashen', 'Mount Zuqualla', 'Mount Batu', 'B', 'Ras Dashen is the highest mountain in Ethiopia.'),
                                                                                                  (3, 'What kind of UNESCO heritage site is Simien Mountains National Park?', 'Natural', 'Church', 'Archaeological', 'Palace', 'A', 'The Simien Mountains are a natural heritage site.'),
                                                                                                  (3, 'Which primate is commonly seen in the Simien Mountains?', 'Gelada monkey', 'Gorilla', 'Chimpanzee', 'Lemur', 'A', 'Geladas live in the Ethiopian highlands and are common in the Simien Mountains.'),
                                                                                                  (3, 'What landscape feature is Simien especially known for?', 'Desert dunes', 'Dramatic cliffs and valleys', 'Coral reefs', 'Rainforest canopy', 'B', 'The park is known for cliffs, escarpments, and deep valleys.');

INSERT INTO heritage_sites
(name, type, region, description, amazing_facts, image_path, added_by_admin_id)
VALUES

    ('Melka Kunture & Balchit', 'archaeological', 'Oromia',
     'Melka Kunture and Balchit are among the most important prehistoric archaeological sites in the world. Located near Addis Ababa along the Awash River, the area preserves nearly two million years of human technological evolution, showing the transition from early stone tool makers to more advanced human societies.',
     'Inscribed as a UNESCO World Heritage Site in 2024; Preserves Oldowan, Acheulean, and Sangoan stone tool technologies; Contains nearly 30 meters of archaeological deposits; Located only about 50 kilometers from Addis Ababa; Volcanic eruptions helped preserve ancient human history for millions of years.',
     "melka_kunture.png", 1),

    ('Bale Mountains National Park', 'natural', 'Oromia',
     'The Bale Mountains National Park is one of Africa''s richest biodiversity hotspots. The park contains the largest Afroalpine habitat in Africa, dramatic mountain scenery, volcanic formations, glacial lakes, and unique wildlife found nowhere else in the world.',
     'Inscribed as a UNESCO World Heritage Site in 2023; Home to more than half of the world''s Ethiopian wolves; Contains the Sanetti Plateau, one of Africa''s highest plateaus; Giant lobelia plants can grow up to six meters tall; The Mountain Nyala and giant molerat are endemic to the region.',
     "bale_mountains.jpg", 1),

    ('Gedeo Cultural Landscape', 'city', 'Southern Ethiopia',
     'The Gedeo Cultural Landscape is a remarkable example of sustainable indigenous agriculture. The Gedeo people developed a forest-garden system where trees, enset, coffee, and spices grow together harmoniously, creating one of the most densely populated yet environmentally sustainable rural areas in Africa.',
     'Inscribed as a UNESCO World Heritage Site in 2023; Became Africa''s 100th UNESCO World Heritage Site; Coffee grows naturally under large protective trees; The Gedeo maintain sacred forests using customary laws called Ballee; The landscape contains important megalithic monuments and cultural sites.',
     "gedeo.jpg", 1),

    ('Yeha', 'archaeological', 'Tigray',
     'Yeha is the center of Ethiopia''s earliest known civilization and the former capital of the ancient kingdom of D''mt. The site is famous for the Temple of the Moon, a monumental structure built around 700 BC and considered the oldest standing building in Ethiopia.',
     'The Temple of the Moon was built without mortar; Some limestone blocks measure up to three meters long; The structure has survived nearly 3,000 years; Yeha is believed to be the cradle of Ethiopian civilization; The ancient pagan temple was later converted into a Christian church.',
     "yeha.png", 1),

    ('Debre Damo Monastery', 'church', 'Tigray',
     'Debre Damo is a famous monastery built on top of a steep flat-topped mountain known as an amba. Founded during the 6th century, it preserves ancient Aksumite architecture, priceless manuscripts, and centuries-old religious traditions.',
     'The monastery is accessible only by climbing a leather rope; Local legend says a giant serpent helped the founder climb the cliff; The monastery preserves some of Ethiopia''s oldest manuscripts; Aksumite kings once used the monastery as a royal prison; The church preserves ancient stone-and-wood Aksumite construction techniques.',
     "debre_damo.png", 1),

    ('Harar Jugol', 'city', 'Harari',
     'Harar Jugol is the historic fortified city of Harar, one of the holiest cities in Islam and an important center of trade and Islamic scholarship. The old city is surrounded by ancient defensive walls and filled with colorful markets, narrow alleyways, and historic mosques.',
     'Inscribed as a UNESCO World Heritage Site in 2006; Contains 368 narrow alleyways designed to confuse invaders; Home to more than 80 mosques including ancient 10th-century mosques; Famous for the traditional hyena feeding ceremony; Harar is considered the fourth holiest city in Islam.',
     "harar.jpg", 1),

    ('Konso Cultural Landscape', 'city', 'Southern Ethiopia',
     'The Konso Cultural Landscape is a living cultural heritage shaped by centuries of traditional agriculture and environmental adaptation. The Konso people built extensive stone terraces and settlements that transformed a dry environment into productive farmland.',
     'Inscribed as a UNESCO World Heritage Site in 2011; Famous for massive dry-stone agricultural terraces; The terraces are still maintained using traditional methods; Contains carved wooden grave statues called wakas; The landscape demonstrates sustainable farming developed over more than 400 years.',
     "konso.jpg", 1);



INSERT INTO heritage_sites
(name, type, region, description, amazing_facts, image_path, added_by_admin_id)
VALUES

    ('Fasil Ghebbi', 'city', 'Amhara',
     'Fasil Ghebbi, located in the historic city of Gondar, is one of Ethiopia''s most extraordinary royal complexes and a symbol of the country''s medieval imperial glory. Built during the 17th century by Emperor Fasilides and later expanded by subsequent emperors, the fortress city contains castles, royal halls, churches, libraries, banquet rooms, and defensive walls that reflect a unique blend of Ethiopian, Portuguese, Indian, and Moorish architectural styles. Often called the "Camelot of Africa," Fasil Ghebbi became the political and cultural center of Ethiopia for more than two centuries. The massive stone castles rise dramatically above Gondar and demonstrate the wealth, military power, and artistic sophistication of the Ethiopian Empire during its golden age.',
     'Inscribed as a UNESCO World Heritage Site in 1979; Known as the "Camelot of Africa"; Built by Emperor Fasilides in the 17th century; Combines Ethiopian, Portuguese, and Indian architectural influences; Contains royal castles, libraries, and ceremonial halls; The fortress walls protected Ethiopian emperors for centuries; Gondar became Ethiopia''s imperial capital because of this complex.',
     "fasil.png", 1),

    ('Sof Omar Cave', 'natural', 'Oromia',
     'Sof Omar Cave is one of Africa''s largest and most spectacular cave systems. Carved over millions of years by the Weyib River, the cave stretches for more than 15 kilometers through limestone formations beneath the Bale region of southeastern Ethiopia. The cave is both a natural wonder and a sacred spiritual site deeply connected to Islamic traditions. Inside the cave are enormous chambers, towering pillars, underground rivers, and dramatic rock formations that create a mysterious underground world. For centuries, pilgrims, travelers, and explorers have been amazed by the beauty and spiritual atmosphere of Sof Omar.',
     'One of the longest cave systems in Africa; Formed naturally by the Weyib River over millions of years; Named after the Muslim holy man Sheikh Sof Omar; Contains massive underground chambers and natural stone pillars; The cave system has more than 40 entrances and exits; Considered sacred by local communities for centuries.',
     "sof_omar.png", 1),

    ('Danakil Depression', 'natural', 'Afar',
     'The Danakil Depression is one of the hottest, lowest, and most geologically active places on Earth. Located in northeastern Ethiopia near the border with Eritrea, the region appears almost otherworldly, with colorful mineral deposits, boiling lava lakes, salt plains, and hydrothermal fields. Despite its harsh conditions, the Danakil has been home to Afar communities for centuries and has played an important role in ancient salt trade routes across the Horn of Africa. Scientists consider the area one of the closest environments on Earth to conditions found on other planets because of its extreme heat and volcanic activity.',
     'One of the hottest inhabited places on Earth; Parts of the depression lie below sea level; Famous for colorful sulfur springs and lava lakes; The Erta Ale volcano contains one of the world''s few permanent lava lakes; Salt caravans still cross the desert using camels; Scientists study the area because it resembles conditions on Mars.',
     "dankil.jpg", 1),

    ('Erta Ale Volcano', 'natural', 'Afar',
     'Erta Ale is one of the world''s most active volcanoes and among the few places on Earth where visitors can witness a permanent lava lake. Rising from the harsh Danakil Depression, the volcano constantly emits heat, smoke, and glowing molten lava, creating one of the planet''s most dramatic geological spectacles. The name "Erta Ale" means "Smoking Mountain" in the Afar language. The volcano has fascinated scientists and adventurers because it offers a rare opportunity to observe volcanic activity directly from the crater rim.',
     'Contains one of the few permanent lava lakes on Earth; The volcano has been continuously active for decades; Known locally as the "Smoking Mountain"; Temperatures around the volcano can become extremely dangerous; The glowing lava lake can often be seen at night from far away; Located within the extreme Danakil Depression.',
     "erta_ale.png", 1),

    ('Asmara', 'city', 'Central Eritrea',
     'Asmara, the capital city of Eritrea, is one of the world''s greatest examples of preserved modernist architecture. During the Italian colonial period in the 1930s, the city was transformed into a modern urban center filled with futuristic buildings, cinemas, cafés, churches, government offices, and boulevards designed in the Art Deco, Rationalist, and Modernist styles. Today, Asmara remains remarkably preserved, offering a unique glimpse into early 20th-century urban planning and architecture. The city combines African traditions with European design influences, creating a distinctive cultural identity unlike anywhere else in Africa.',
     'Inscribed as a UNESCO World Heritage Site in 2017; Known as "Africa''s Modernist City"; Contains hundreds of preserved Italian colonial buildings; Famous landmarks include Cinema Impero and Fiat Tagliero; The city''s architecture reflects Art Deco and Futurist styles; Asmara was designed as a model modern city during the Italian colonial era.',
     "asmara.jpg", 1),

    ('Dahlak Archipelago', 'natural', 'Red Sea Region',
     'The Dahlak Archipelago is Eritrea''s greatest marine treasure, consisting of more than 200 islands scattered across the Red Sea. The islands are famous for crystal-clear waters, coral reefs, marine biodiversity, and centuries of maritime history. Historically, the islands served as important trading centers connecting Africa, Arabia, and Asia through the Red Sea trade routes. Today, the archipelago is considered one of the most beautiful and least explored coastal environments in the world, offering rich underwater ecosystems and peaceful island landscapes.',
     'Contains more than 200 islands in the Red Sea; Famous for coral reefs and marine biodiversity; Historically connected Africa with Arabian trade networks; Home to rare fish, sea turtles, and dolphins; Some islands contain ancient Islamic archaeological remains; The waters are considered among the clearest in the Red Sea.',
     "dahlak.jpg", 1),

    ('Qohaito', 'archaeological', 'Southern Eritrea',
     'Qohaito is one of Eritrea''s most important ancient archaeological sites and a former center of the Aksumite civilization. Situated high in the mountains, the city once controlled important trade routes linking the African interior with the Red Sea coast. The ruins include temples, tombs, reservoirs, inscriptions, and ancient stone structures that reveal the region''s role in trade, religion, and political power thousands of years ago. The dramatic mountain scenery surrounding the ruins adds to the mystery and beauty of the site.',
     'Connected to the ancient Aksumite civilization; Contains temples, tombs, and ancient inscriptions; Controlled important trade routes across the Horn of Africa; Features sophisticated ancient water reservoirs; The mountain setting provides breathtaking views of the Eritrean highlands; Archaeologists continue discovering new structures and artifacts.',
     "qohaito.png", 1),

    ('Massawa Old Town', 'city', 'Northern Red Sea',
     'Massawa Old Town is one of the oldest port cities on the Red Sea and a historic gateway between Africa and the Middle East. Influenced by Ottoman, Egyptian, Italian, and Arab cultures, the city developed a unique architectural style featuring coral-stone buildings, narrow streets, balconies, and mosques. For centuries, Massawa served as a major trading center where goods, cultures, religions, and ideas moved between Africa and Asia. Despite damage from wars and natural disasters, the city still preserves its historic charm and multicultural identity.',
     'One of the oldest port cities on the Red Sea; Influenced by Ottoman, Arab, Egyptian, and Italian cultures; Buildings were traditionally constructed using coral stone; Served as an important trade gateway between Africa and Asia; Known for its beautiful coastal scenery and historic architecture; The city played a major role in Red Sea maritime history.',
     "massawa.jpg", 1);
