DROP DATABASE IF EXISTS `proyecto`;
CREATE DATABASE proyecto;
USE proyecto;

CREATE TABLE `usuario` (
	usuario VARCHAR(15) PRIMARY KEY NOT NULL,
    contraseña VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE);
    
CREATE TABLE `perfil` (
	usuario VARCHAR(15) PRIMARY KEY NOT NULL,
    nombre VARCHAR(15) NOT NULL default '',
    apellido VARCHAR(15) NOT NULL default '',
    ciudad VARCHAR(20) NOT NULL default '',
    sexo VARCHAR(15) NOT NULL default 'Sin especificar',
    edad INT(2) NOT NULL default '-1',
    foto LONGBLOB,
    extImagen VARCHAR(10) NOT NULL default ' ',
    registroCompleto CHAR(1) NOT NULL default '0');
    
CREATE TABLE `mensajes` (
	participante1 VARCHAR(15) NOT NULL,
    participante2 VARCHAR(15) NOT NULL,
    texto LONGTEXT,
    id VARCHAR(32) PRIMARY KEY NOT NULL)