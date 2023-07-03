DROP TABLE IF EXISTS news CASCADE ;
CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    time TIME  NOT NULL,
    title VARCHAR(255) NOT NULL UNIQUE,
    text TEXT NOT NULL,
    username VARCHAR(55)
);


DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    text TEXT NOT NULL,
    username VARCHAR(50),
    news_id INT NOT NULL,
    FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE
);




INSERT INTO news (id, time, title, text, username) VALUES
(1, '09:15:00', 'Economy resumes growth', 'After several months of decline, the economy is finally showing signs of recovery', 'Jak'),
(2, '12:20:30', 'New electric vehicle sales record', 'More electric vehicles sold this month than ever before', 'Artur'),
(3, '17:35:15', 'City launches small business support program', 'To reduce unemployment and promote small businesses, the city will launch support program', 'Petr'),
(4, '14:40:45', 'Technological innovations in healthcare', 'New technologies can significantly reduce healthcare costs and make it more efficient', 'Jan'),
(5, '08:00:10', 'Celebration of the citys day', 'Today the city celebrates its birthday, celebrations will take place in all districts of the city', 'Judi'),
(6, '11:05:20', 'Vineyards in the region start harvesting', 'Vine harvesting has started in the region, experts expect a high quality harvest', 'Romeo'),
(7, '15:30:50', 'New bridge over river opens to traffic', 'After years of construction, new bridge opens to traffic', 'Oksi'),
(8, '10:45:05', 'Locals oppose the construction of a new factory', 'Locals took to the streets to protest against the construction of a factory that could negatively affect the ecology of the region', 'Maria'),
(9, '13:25:30', 'Increasing the salaries of civil servants', 'Today the government announced a salary increase for civil servants, which should help improve the economic situation in the country', 'Kurilo'),
(10, '16:55:15', 'New way of processing grain reduces costs', 'Using new technology, grain producers will be able to significantly reduce their production costs', 'Grek'),
(11, '09:10:30', 'Culture and art festival starts in the city', 'Today starts the culture and art festival that will take place over the next two weeks', 'Mario'),
(12, '12:15:45', 'Increase in car production', 'Major car company announces plans to increase car production next year', 'NedStark'),
(13, '18:30:20', 'New reconstructions of the main square of the city', 'New reconstruction of the main square of the city will begin soon, which should improve its appearance' , 'Dragon'),
(14, '19:30:20', 'Accident on the M4 highway', 'Two cars collided this morning on the M4 highway. One person died and three others were injured.', 'Tomund'),
(15, '20:30:20', 'Coronavirus in Moscow', '500 new cases of coronavirus infection were registered in the capital per day. Most of the cases are in the Southern District.', 'Tofi'),
(16, '21:30:20', 'Rain in the Moscow region', 'In the coming days, rain and thunderstorms are expected in all areas of the Moscow region. Flooding is possible in some areas.', 'KofeAnan'),
(17, '22:30:20', 'Weekend in Moscow', 'The Moscow Zoo is hosting a free tour of the Carnivorous Mammals exhibition this weekend. Visitors will be able to see lions, tigers and leopards.', 'Baiden'),
(18, '23:30:20', 'A new cinema has opened in the center of Moscow', 'A new cinema for 10 spectators has opened in the very center of Moscow. Films long awaited by the audience were presented at the opening.', 'Zelc'),
(19, '01:30:20', 'Increasing the retirement age', 'The State Duma is considering a bill to raise the retirement age to 68. Deputies have already started discussing this issue.', 'Mario'),
(20, '02:30:20', 'A new startup in the cloud market', 'A new startup in the cloud market offers highly specialized cloud services for a target audience. The company intends to become a leader in this industry.', 'Dekster');
INSERT INTO comments (id, time, text, username, news_id) VALUES
                                                             (1,'12:05:10', 'I agree, completely!', 'Elena', 1),
                                                             (2,'13:45:28', 'What a touching story!', 'Andrey', 2),
                                                             (3,'16:20:13', 'Strong! I m already ready for the holiday!', 'Oksana', 3),
                                                          (4,'18:55:02', 'Such stories always please!', 'Paul', 4),
                                                          (5,'20:10:50', 'Great series, I ll definitely watch it!', 'Maria', 5),
                                                             (6,'22:30:45', 'It s great that modern cinema does not forget about the classics!', 'Ilya', 6),
                                                          (7,'07:15:30', 'What beautiful flowers, thanks for the photo!', 'Elena', 7),
                                                          (8,'09:50:10', 'It doesn t go half a station!', 'Vasily', 8),
                                                             (9,'11:25:28', 'Excellent athlete and an example to everyone!', 'Nikita', 9),
                                                             (10,'14:00:18', 'Great, what s going on here!', 'Anna', 10),
                                                          (11,'16:35:30', 'Beautiful poem!', 'Svetlana', 11),
                                                          (12,'19:10:05', 'Great service, always happy to come there!', 'Yuri', 12),
                                                          (13,'21:45:15', 'How long have I been waiting for this news!', 'Olga', 13),
                                                          (14,'08:00:20', 'A strong competitor, but our company is not far behind!', 'Anastasia', 14),
                                                          (15,'10:20:05', 'I just wanted to write about this!', 'Alina', 15),
                                                          (16,'12:45:55', 'Very nice and cozy restaurant!', 'Irina', 16),
                                                          (17,'15:10:30', 'Such a nice and useful product!', 'Dmitry', 17),
                                                          (18,'17:25:48', 'The concert was amazing!', 'Mikhail', 18),
                                                          (19,'20:05:05', 'This is just a work of art!', 'Lyudmila', 19),
                                                          (20,'22:35:30', 'What a beautiful view of the city!', 'Eugene', 20),
                                                             (21,'10:30:15', 'Great news!', 'Ivan Ivanov', 1),
                                                             (22,'11:45:20', 'Not a bad article', 'Anna Petrova', 2),
                                                             (23,'12:20:40', 'Absolutely agree!', 'Maria Sidorova', 3),
                                                             (24,'13:10:05', 'I fully support the author', 'Oleg Kozlov', 4),
                                                             (25,'15:30:55', 'How interesting!', 'Nikita Ignatov', 5),
                                                             (26,'16:40:10', 'That s right', 'Tatiana Smirnova', 6),
                                                          (27,'17:50:30', 'Very exciting', 'Egor Isaev', 7),
                                                          (28,'18:25:15', 'I liked it very much', 'Nadezhda Volkova', 8),
                                                          (29,'20:15:35', 'I disagree with the author', 'Dmitry Popov', 9),
                                                          (30,'21:10:25', 'Absolutely wrong', 'Vera Ivanova', 10),
                                                          (31,'22:05:40', 'Wrong information', 'Maxim Kozlov', 11),
                                                          (32,'23:15:50', 'I do not agree with this opinion', 'Evgeny Fedorov', 12),
                                                          (33,'08:20:30', 'I thank the author for the information', 'Lydia Sokolova', 13),
                                                          (34,'09:05:45', 'Very important news!', 'Irina Kovaleva', 14),
                                                          (35,'10:40:10', 'I think it s wrong', 'Ilya Nikolaev', 15),
                                                             (36,'11:50:25', 'I didn t like it', 'Andrey Ivanov', 16),
                                                          (37,'13:10:50', 'The author said everything correctly', 'Oleg Kozlov', 17),
                                                          (38,'14:30:25', 'I agree with the author', 'Viktor Petrov', 18),
                                                          (39,'15:40:15', 'Interesting news', 'Anna Smirnova', 19),
                                                          (40,'16:55:05', 'Very informative!', 'Ivan Popov', 20),
                                                          (41,'09:45:00', 'Great news!', 'Ivan', 1),
                                                          (42,'10:30:15', 'I didn t like this news', 'Katya', 2),
                                                             (43,'11:20:05', 'Very interesting!', 'Andrey', 3),
                                                             (44,'12:15:00', 'Totally agree', 'Michael', 4),
                                                             (45,'13:00:20', 'I will follow this topic', 'Elena', 5),
                                                             (46,'14:10:10', 'I think this is very important', 'Paul', 6),
                                                             (47,'15:30:05', 'The news raised questions', 'Anna', 7),
                                                             (48,'16:45:30', 'I am very pleased with this development of events', 'Dmitry', 8),
                                                             (49,'17:20:20', 'That s great!', 'Victor', 9),
                                                          (50,'18:15:10', 'You left me indifferent', 'Olga', 10),
                                                             (51,'19:00:25', 'I believe that this meaning should not be forgotten', 'Natalya', 11),
                                                             (52,'20:05:20', 'I promise to support this idea', 'Sergey', 12),
                                                             (53,'21:15:05', 'The news is extremely relevant', 'Alexandra', 13),
                                                             (54,'22:20:30', 'I don t really like the author s opinion', 'Igor', 14),
                                                             (55,'23:10:00', 'I think this will be a real breakthrough', 'Kristina', 15),
                                                             (56,'00:00:01', 'I will definitely share my opinion', 'Roman', 16),
                                                             (57,'01:00:00', 'It would be great if this happened', 'Egor', 17),
                                                             (58,'02:30:00', 'This news gives hope for the best', 'Daria', 18),
                                                             (59,'03:45:10', 'I think that this problem needs to be solved urgently', 'Valentina', 19),
                                                             (60,'04:20:15', 'This information raises some doubts', 'Aleksey', 20),
                                                             (61,'10:23:45', 'Great news!', 'Alexander', 1),
                                                             (62,'11:35:22', 'Bravo!', 'Catherine', 2),
                                                             (63,'13:45:12', 'Really interesting', 'Sergei', 3),
                                                             (64,'14:06:59', 'Quite right', 'Olga', 4),
                                                             (65,'15:20:32', 'I fully support', 'Ivan', 5),
                                                             (66,'16:18:49', 'No doubt', 'Marina', 6),
                                                             (67,'18:02:01', 'Well said!', 'Dmitry', 7),
                                                             (68,'19:12:45', 'To the point!', 'Hope', 8),
                                                             (69,'20:05:23', 'Agree 100%', 'Andrey', 9),
                                                             (70,'21:30:11', 'Useful information', 'Tatiana', 10),
                                                             (71,'22:14:57', 'Thanks for the article', 'Peter', 11),
                                                             (72,'23:05:20', 'I have been looking for such information for a long time', 'Svetlana', 12),
                                                             (73,'04:15:17', 'An interesting look at the situation', 'Mikhail', 13),
                                                             (74,'05:45:36', 'I ll add my 5 kopecks', 'Anastasia', 14),
                                                          (75,'06:30:40', 'I like this direction', 'Roman', 15),
                                                          (76,'08:10:21', 'Useful material', 'Elena', 16),
                                                          (77,'09:20:55', 'New to me', 'Igor', 17),
                                                          (78,'10:18:30', 'Interesting to read', 'Valentina', 18),
                                                          (79,'11:40:09', 'Thanks for the article!', 'Aleksey', 19),
                                                          (80,'12:55:47', 'Useful content', 'Xenia', 20),
                                                             (81,'09:45:00', 'Great news!', 'Ivan', 1),
                                                             (82,'10:30:15', 'I didn t like this news', 'Katya', 2),
                                                          (83,'11:20:05', 'Very interesting!', 'Andrey', 3),
                                                          (84,'12:15:00', 'Totally agree', 'Michael', 4),
                                                          (85,'13:00:20', 'I will follow this topic', 'Elena', 5),
                                                          (86,'14:10:10', 'I think this is very important', 'Paul', 6),
                                                          (87,'15:30:05', 'The news raised questions', 'Anna', 7),
                                                          (88,'16:45:30', 'I am very pleased with this development of events', 'Dmitry', 8),
                                                          (89,'17:20:20', 'That s great!', 'Victor', 9),
                                                             (90,'18:15:10', 'Left me indifferent', 'Olga', 10),
                                                             (91,'19:00:25', 'I believe that this meaning should not be forgotten', 'Natalya', 11),
                                                             (92,'20:05:20', 'I promise to support this idea', 'Sergey', 12),
                                                             (93,'21:15:05', 'The news is extremely relevant', 'Alexandra', 13),
                                                             (94,'22:20:30', 'I don t really like the author s opinion', 'Igor', 14),
                                                             (95,'23:10:00', 'I think this will be a real breakthrough', 'Kristina', 15),
                                                             (96,'00:00:01' , 'I will definitely share my opinion' , 'Roman' ,16 ),
                                                             (97,'01:00:00' , 'It would be great if this happened' , 'Egor' ,17 ),
                                                             (98,'23:10:00', 'I think this will be a real breakthrough', 'Kristina', 18),
                                                             (99,'00:00:01' , 'I will definitely share my opinion' , 'Roman' ,19 ),
                                                             (100,'01:00:00' , 'It would be great if this happened' , 'Egor' ,20 ),
                                                             (101,'09:20:30', 'Great news!', 'Ivanov Ivan', 1),
                                                             (102,'10:15:25', 'I like this website', 'Petrov Petrov', 2),
                                                             (103,'11:45:50', 'It was expected', 'Sidorova Anna', 3),
                                                             (104,'12:30:40', 'Great initiative', 'Maria Vladimirova', 4),
                                                             (105,'13:50:15', 'I support!', 'Ivanova Elena', 5),
                                                             (106,'14:20:30', 'Thanks for the information', 'Petrova Svetlana', 6),
                                                             (107,'15:15:25', 'Promising project', 'Konstantinov Konstantin', 7),
                                                             (108,'16:45:50', 'Keep going', 'Vasily Sidorov', 8),
                                                             (109,'17:30:40', 'Good work', 'Vladimirova Tatyana', 9),
                                                             (110,'18:50:15', 'I hope for your success', 'Ivanov Georgiy', 10),
                                                             (111,'19:20:30', 'I m looking forward to the continuation', 'Petrov Alexander', 11),
                                                          (112,'20:15:25', 'Excellent project', 'Konstantinova Olga', 12),
                                                          (113,'21:45:50', 'Your achievement inspires', 'Sidorov Roman', 13),
                                                          (114,'22:30:40', 'I ve been following this project for a long time', 'Andrey Vladimirov', 14),
                                                             (115,'23:50:15', 'I hope you succeed', 'Ivanov Alexey', 15),
                                                             (116,'01:20:30', 'Ambition at the highest level', 'Petrov Egor', 16),
                                                             (117,'02:15:25', 'Glad it s happening', 'Konstantinov Mikhail', 17),
                                                          (118,'03:45:50', 'You don t start weakly', 'Sidorov Evgeniy', 18),
                                                             (119,'04:30:40', 'Well done, keep it up!', 'Artem Vladimirov', 19),
                                                             (120,'05:50:15', 'This is the highest level', 'Ivanova Julia', 20),

                                                             (121,'10:30:00', 'Great news! I ll definitely read it.', 'Natalya', 1),
                                                          (122,'11:15:20', 'Very interesting material.', 'Aleksey', 2),
                                                          (123,'12:05:45', 'Thanks for the detailed description.', 'Anna', 3),
                                                          (124,'13:40:15', 'Impressive. Looking forward to continuing.', 'Dmitry', 4),
                                                          (125,'14:20:30', 'It s amazing how everything works.', 'Irina', 5),
                                                             (126,'15:10:10', 'As always, everything is detailed and intelligible.', 'Elena', 6),
                                                             (127,'16:25:00', 'This is really worth reading.', 'Michael', 7),
                                                             (128,'17:30:40', 'Wonderful material. Thank you.', 'Olga', 8),
                                                             (129,'18:45:15', 'It s always interesting to learn new things.', 'Peter', 9),
                                                          (130,'19:30:25', 'Great commentary. Short and to the point.', 'Victoria', 10),
                                                          (131,'20:05:50', 'I agree with the author. That s right.', 'Vladimir', 11),
                                                             (132,'21:20:15', 'As always, at the highest level.', 'Galina', 12),
                                                             (133,'22:10:30', 'No words, just admiration.', 'Sergei', 13),
                                                             (134,'23:00:10', 'Thank you, it was interesting to read.', 'Tatiana', 14),
                                                             (135,'00:15:45', 'Very useful material.', 'Yuri', 15),
                                                             (136,'01:30:00', 'It s always interesting to hear from you.', 'Nina', 16),
                                                          (137,'02:45:00', 'No comment, but thanks anyway.', 'Alexander', 17),
                                                          (138,'03:55:00', 'Detailed information at a decent level.', 'Oleg', 18),
                                                          (139,'04:35:00', 'These comments always help to understand the news better.', 'Evgeniya', 19),
                                                          (140,'05:25:00', 'Very interesting material.', 'Xenia', 20),
                                                          (141,'12:30:20', 'Great news!', 'Ivanova', 1),
                                                          (142,'13:25:10', 'I agree, but...', 'Petrov', 2),
                                                          (143,'15:40:50', 'The author of the article is mistaken', 'Sidorov', 3),
                                                          (144,'16:20:30', 'This is very important!', 'Kuznetsova', 4),
                                                          (145,'18:00:40', 'Thanks for the information', 'Danilov', 5),
                                                          (146,'19:05:20', 'It will be interesting!', 'Chernova', 6),
                                                          (147,'20:30:10', 'I hope these changes will improve the situation', 'Tarasov', 7),
                                                          (148,'22:10:15', 'The author of the article needs more facts', 'Kuzmin', 8),
                                                          (149,'08:20:30', 'Great stuff!', 'Ivanov', 9),
                                                          (150,'09:15:40', 'I m not very interested', 'Petrova', 10),
                                                             (151,'10:50:20', 'Thank you very much for the information', 'Sidorov', 11),
                                                             (152,'12:30:10', 'It s just great', 'Kuznetsov', 12),
                                                          (153,'14:40:20', 'I hope this really helps', 'Danilova', 13),
                                                          (154,'15:20:30', 'It means a lot', 'Chernov', 14),
                                                          (155,'17:10:25', 'More information is needed before drawing conclusions', 'Tarasova', 15),
                                                          (156,'18:30:15', 'It was predictable', 'Kuzmina', 16),
                                                          (157,'10:20:50', 'Great interview!', 'Ivanova', 17),
                                                          (158,'12:35:40', 'Many positive emotions after reading', 'Petrov', 18),
                                                          (159,'14:50:20', 'The author needs more facts and research', 'Sidorova', 19),
                                                             (160,'14:50:20', 'I am shocked by such news', 'Arthur', 20),
                                                             (161,'11:23:45', 'Good news, thanks!', 'Ivan', 1),
                                                             (162,'12:00:15', 'I like this news', 'Elena', 2),
                                                             (163,'13:10:20', 'I don t like it very much', 'Peter', 3),
                                                          (164,'14:35:55', 'Great news!', 'Maria', 4),
                                                          (165,'15:40:10', 'Absolutely agree', 'Alexander', 5),
                                                          (166,'17:05:30', 'Bad news', 'Dmitry', 6),
                                                          (167,'18:15:00', 'Very interesting', 'Oleg', 7),
                                                          (168,'19:20:45', 'Some strange news', 'Tatiana', 8),
                                                          (169,'20:30:25', 'I don t like the direction everything is going', 'Anna', 9),
                                                             (170,'21:40:30', 'Level news', 'Sergey', 10),
                                                             (171,'22:45:15', 'Useful news!', 'Natalia', 11),
                                                             (172,'11:30:00', 'Thanks for the story, very informative', 'Mikhail', 12),
                                                             (173,'12:45:30', 'I am glad that the situation has improved', 'Olga', 13),
                                                             (174,'14:00:00', 'Interesting news!', 'Artem', 14),
                                                             (175,'15:20:20', 'I didn t think it was possible', 'Vadim', 15),
                                                          (176,'16:30:45', 'I don t like this news', 'Catherine', 16),
                                                             (177,'18:00:00', 'Thanks for the info', 'Daria', 17),
                                                             (178,'19:10:30', 'Everything is very interesting', 'Roman', 18),
                                                             (179,'20:15:15', 'I liked the news', 'Gleb', 19),
                                                             (180,'22:00:30', 'The news raises questions', 'Irina', 20),
                                                             (181,'10:25:00', 'Good news', 'Ivan', 1),
                                                             (182,'12:15:00', 'An amazing story', 'Maria', 2),
                                                             (183,'14:30:00', 'I agree, interesting', 'Aleksey', 3),
                                                             (184,'16:10:00', 'Impressive discovery', 'Catherine', 4),
                                                             (185,'18:00:00', 'Did they tell about it here?', 'Dmitry', 5),
                                                             (186,'20:20:00', 'Very convenient', 'Svetlana', 6),
                                                             (187,'22:45:00', 'I would like that too', 'Peter', 7),
                                                             (188,'09:00:00', 'Greatly written', 'Natalya', 8),
                                                             (189,'11:35:00', 'Excellent material', 'Andrey', 9),
                                                             (190,'13:40:00', 'In plain language, thank you!', 'Elena', 10),
                                                             (191,'15:20:00', 'This is news!', 'Gennady', 11),
                                                             (192,'17:00:00', 'Clearly explained', 'Tatiana', 12),
                                                             (193,'19:10:00', 'Successful application of technology', 'Michael', 13),
                                                             (194,'21:30:00', 'You should have known about him earlier', 'Irina', 14),
                                                             (195,'08:45:00', 'Good video, thanks!', 'Olga', 15),
                                                             (196,'10:55:00', 'It s really interesting', 'Artem', 16),
                                                          (197,'12:45:00', 'I continue to follow this development', 'Victor', 17),
                                                          (198,'14:20:00', 'Well done, great idea', 'Gregory', 18),
                                                          (199,'16:00:00', 'Looking forward to the continuation', 'Eugene', 19),
                                                          (200,'16:00:00', 'This world is going to hell', 'Arthur', 20);