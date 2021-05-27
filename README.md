# Grocery Store
> A grocery database management application using Java, JDBC, and PostgreSQL.

## Table of contents
* [General Info](#general-info)
* [Screenshots](#screenshots)
  * [Development process](#development-process)
  * [Final application](#final-application)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)
* [Authors](#authors)

## General Info
The goal of this project was to build a grocery store application using a database backend to store information about products, availability of products in the stock, and customers of the store. Customers of the store were supposed to be able to search for products and look up information about products, setup an account and change their preferences and account details, order products, and make payments.

Our group began this project by writing down all of the information we needed to describe a grocery store and its users. Following that, we developed an [entity–relationship model](https://en.wikipedia.org/wiki/Entity%E2%80%93relationship_model) (ER model) that included the information we needed in an efficient manner. Then, we started working on our relational schema, which would be translated into an actual SQL script to create the tables and constraints using [PostgreSQL](https://www.postgresql.org/ "PostgreSQL: The world's most advanced open source database"). Finally, we worked on incorporating our database into our graphical user interface made with [JavaFX](https://openjfx.io/ "JavaFX").

## Screenshots
Here are some images from the development process and some screenshots of the final application. You can also download [these videos](./Videos) to view the application in action (unfortunately, the videos are too large to preview on Github).

### Development process
#### ER model
![ER model](./ER-model/ER-model.PNG)

### Final application
#### Login
![User select](./Screenshots/userSelect.PNG)
![Staff login](./Screenshots/staffLogin.PNG)
![Customer login](./Screenshots/customerLogin.PNG)

#### Store page
![Main page](./Screenshots/mainPage.PNG)
![Product select](./Screenshots/productSelect.PNG)
![Cart](./Screenshots/cart.PNG)
![Order confirmation](./Screenshots/orderConfirmation.PNG)
![Orders](./Screenshots/orders.PNG)

## Technologies
* [PostgreSQL](https://www.postgresql.org/ "PostgreSQL: The World's Most Advanced Open Source Relational Database")
* Java
  * [JavaFX](https://openjfx.io/ "JavaFx")

## Setup
Instructions for local setup are WIP.

## Features
* User login for customers and staff
* Product search by name, category, or state
* Add items to customer cart
* Order items in customer cart
* Database stock tracking

## Status
Project is: _finished_

## Authors
Created by Alex Riegler, Martijn Koning, and Maria.
