#!/usr/bin/env bash

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=books-service \
--package-name=com.cardealership.books \
--groupId=com.cardealership.books \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
books-service

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=members-service \
--package-name=com.cardealership.members \
--groupId=com.cardealership.members \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
members-service

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=reservations-service \
--package-name=com.cardealership.reservations \
--groupId=com.cardealership.reservations \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
reservations-service

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=loans-service \
--package-name=com.cardealership.loans \
--groupId=com.cardealership.loans \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
loans-service

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.cardealership.apigateway \
--groupId=com.cardealership.apigateway \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
api-gateway

