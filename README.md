# MongoRelacionalDB
<p align="center">
  <img src="https://i.imgur.com/oJO8v25.png" alt="Mongo DB"/>
</p>

## ⚠ Requisitos
- Docker y Docker Compose

## ✏ Instrucciones
Inicializar la base de datos:
1. Abrir la consola y acceder a la carpeta **docker**.
2. **Escribir el siguiente comando para inicializar la base de datos con Docker Compose:** sudo docker-compose up -d
3. Ejecutar el programa con el parámetro de la ubicación donde se guardarán los datos a exportar.
**Utilizar Java JDK 11 para evitar problemas.**

## Puesto en práctica
- Se realiza la implementación de una base de datos relacional con MongoDB mediante el mapeo a objetos para la implementación de operaciones CRUD.

## Librerías usadas 📚
- Lombok.
- JUnit5.
- Jupiter.
- Mockito.
- Hibernate.
- Narayana.
- JBEX.
- Driver Mongo.
- JavaDev.
- Cdimascio.

## 🐛 En caso de fallo (reinicio base de datos)
1. **Para parar el adminer** sudo docker stop adminer
2. **Para parar MariaDB** sudo docker stop mariadb
3. **Para eliminar todas las imágenes, volúmenes, contenedores y redes no utilizadas:** sudo docker system prune -a --volumes

## ⚙️ Mejoras posibles al programa
- Añadir opción de poder agregar elementos a las listas y sets de las entidades. Agregar al menú la opción de interacción con listas y sets de las entidades: El menú preguntaría sobre qué lista quieres interactuar, te solicitará el tipo de acción a realizar (recibir, inserción, reemplazar, borrar) tal y como se hacen las consultas CRUD, pero con listas.

## Autores
- Carlos <https://github.com/Nerd-Geek>
- Adrián <https://github.com/AdrianLozano96>
- Madirex <https://github.com/Madirex>
