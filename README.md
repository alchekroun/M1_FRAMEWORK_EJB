# Projet INF2

Repo git du projet INF2 sur la modélisation des trains naviguant sur le réseau SNCF.

## Auteurs

Alexandre Chekroun
David Serruya
Julien Doujet
Hannah Naccache

## Lancement

Dans l'odre il faut executer les programmes suivants : 
- Main
- AppSubscriber
- RestClientApp

Main : pour lancer le serveur web et initialiser la base de données.
AppSubscriber : on crée les infogares et les mettons en écoute sur le topic "bulletin-info"
RestClientApp : Cette classe va faire des requêtes HTTP vers le serveur pour : initialiser le trajet des passager, initialiser l'envoi de bulletins périodiques et mettre en marche les trains.

## What's here

this project contains all the moving parts to complete M1 MIAGE INF2 programming project. It contains configured CDI through Weld and Jersey's integration with HK2. In contains persistence with JPA, implemented with Eclipse Link on top of a H2 database. It shows how to design a REST API using JAXRS implemented by Jersey. All the JAXB Classes are generated by an XSD with the appropriate xjc bindings to make sure that we can handle Data smoothly.


## Running

### project-jee8

This is the parent projet

### project-jee8-model

This project containts sur JAXB generated DTO to be used for communication in the REST, JMS and Business Layer

### project-jee8-webapp

This project contains a REST API (JAXRS), the persistence layer (JPA), the service layer and make use of Publisher Subsciber (JMS) to send periodic bulletin to info gare

### project-jee8-rest-client

This project shows how to call a REST API using Jersey Client for JAXRS

### project-jee8-jms-subscriber

This project consumes message produced by project-jee8-webapp to display information to infoGare to custommer

