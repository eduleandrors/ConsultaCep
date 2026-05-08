SCRIPT DE CRIAÇAO:

create database Via_Cep character set utf8mb4
collate utf8mb4_unicode_ci;

use Via_Cep;

create table cep (
	codigo varchar (8) primary key,
	cidade varchar (100),
	estado varchar (100),
	logradouro varchar (100),
	complemento varchar (100),
	bairro varchar (100),
	UF varchar (2),
	regiao varchar (100),
	DDD varchar (3),
	cont int,
	horario timestamp
);
