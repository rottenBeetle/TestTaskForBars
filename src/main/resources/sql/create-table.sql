DROP TABLE contracts;
CREATE TABLE IF NOT EXISTS contracts
(
    id          BIGSERIAL   NOT NULL PRIMARY KEY,
    number      INTEGER NOT NULL,
    date        DATE NOT NULL,
    last_update DATE NOT NULL

);

insert into contracts (id, number, date , last_update) values (1, 10, '2000-01-08','2022-07-21');
insert into contracts (id, number, date , last_update) values (2, 20, '2005-03-15','2022-08-27');
insert into contracts (id, number, date , last_update) values (3, 30, '2010-06-19','2021-04-11');
insert into contracts (id, number, date , last_update) values (4, 40, '2015-08-25','2022-06-13');
insert into contracts (id, number, date , last_update) values (5, 50, '2013-12-25','2022-03-10');