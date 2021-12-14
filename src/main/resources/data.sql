INSERT INTO USER(name, email, password) VALUES('Dev', 'dev@email.com', '$2a$10$gR1y1JWyrszezGNrRYXdDux1G4Ke1HYgP0V6uF4iP6YmUFPiOBSI6');
INSERT INTO USER(name, email, password) VALUES('Master', 'master@email.com', '$2a$10$gR1y1JWyrszezGNrRYXdDux1G4Ke1HYgP0V6uF4iP6YmUFPiOBSI6');

INSERT INTO PRIVILEGE(id, name) VALUES(1,'ROLE_DEV');
INSERT INTO PRIVILEGE(id, name) VALUES(2,'ROLE_MASTER');

INSERT INTO USER_PRIVILEGES(user_id, privileges_id) VALUES(1,1);
INSERT INTO USER_PRIVILEGES(user_id, privileges_id) VALUES(2,2);
INSERT INTO USER_PRIVILEGES(user_id, privileges_id) VALUES(2,1);

INSERT INTO EPIC(name, description, reporter_id) VALUES('First epic','This is the first epic',1);
INSERT INTO EPIC(name, description, reporter_id) VALUES('Second epic','This is the second epic',2);

INSERT INTO BUG(title, description, priority, status, updated_at, created_at, epic_id, assignee_id, reporter_id) VALUES('First bug','This is the first epic','LOW','IN_PROGRESS','2021-12-14 11:48:16.663304','2021-12-14 11:48:16.663304',1,1,2);
INSERT INTO BUG(title, description, priority, status, updated_at, created_at, epic_id, reporter_id) VALUES('Second bug','This is the second epic','HIGH','BACKLOG','2021-12-14 11:48:16.663304','2021-12-14 11:48:16.663304',2,2);