create extension hstore;
create schema accounts;
create table if not exists accounts."Account" ("id" UUID PRIMARY KEY, "username" VARCHAR, "balance" INTEGER NOT NULL);