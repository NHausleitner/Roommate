create table if not exists arbeitsplatz(
    id serial primary key,
    raum varchar(20)
);

create table if not exists ausstattung(
    arbeitsplatz int,
    arbeitsplatz_key int,
    name varchar(100),
    spezifikation varchar(100),
    foreign key (arbeitsplatz) references arbeitsplatz(id)
);