![icon](icon.ico) 
# Parchments

Parchments es una aplicación que permite a un grupo de usuaries escribir una historia encadenando capítulos. Cada capítulo (llamado Parchment) puede tener infinitas continuaciones, por lo que la historia resultante es un árbol de múltiples géneros y giros en la trama.


## Cómo levantar el entorno de desarrollo en forma local

1. Clonar el proyecto.
2. Descargar Neo4j Desktop desde [la página oficial](https://neo4j.com/download/?ref=try-neo4j-lp) e instalar
3. Ejecutar Neo4j Desktop y crear un nuevo proyecto de Neo4j. La contraseña elegida para la base de datos tiene que ser la misma que el valor de la variable `spring.data.neo4j.password` en el archivo application.properties en `parchments_backend`. 
4. Una vez la base de datos esté creada, ir a la pestaña de plugins e instalar APOC. Reiniciar la base de datos luego de que el plugin esté instalado
5. Ejecutar `ParchmentsBackendApplication`
