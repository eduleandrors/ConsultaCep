SCRIPT DE CRIAÇAO:

create database Via_Cep character set utf8mb4 collate utf8mb4_unicode_ci;

use Via_Cep;

create table usuario (
id_usuario INT NOT NULL AUTO_INCREMENT,
nome varchar (100) not null,
email varchar (257) not null unique,
senha varchar (100) not null,
dataCriacao timestamp,
PRIMARY KEY (id_usuario)
);

create table cep (
fk_id_usuario INT NOT null,
codigo varchar (8),
cidade varchar (100),
estado varchar (100),
logradouro varchar (100),
complemento varchar (100),
bairro varchar (100),
UF varchar (2),
regiao varchar (100),
DDD varchar (3),
cont int,
horario timestamp,
CONSTRAINT fk_cep_usuario FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id_usuario),
primary key (fk_id_usuario, codigo)
);


create table categorias (
fk_id_usuario int not null,
nome varchar (100) not null,
CONSTRAINT fk_categoria_usuario FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id_usuario),
primary key (fk_id_usuario, nome)
);

create table favoritos (
id_favorito int NOT NULL AUTO_INCREMENT,
fk_id_usuario INT NOT null,
nome varchar (100),
codigo varchar (8),
cidade varchar (100),
estado varchar (100),
logradouro varchar (100),
complemento varchar (100),
bairro varchar (100),
UF varchar (2),
regiao varchar (100),
DDD varchar (3),
cont int,
horario timestamp,
CONSTRAINT fk_favoritos_usuario FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id_usuario),
primary key (id_favorito)
);

create table categoria_favoritos(
fk_id_favorito int NOT null,
fk_id_usuario int not null,
fk_nome varchar (100) not null,
CONSTRAINT fk_categoria_favoritos1 FOREIGN KEY (fk_id_favorito)
