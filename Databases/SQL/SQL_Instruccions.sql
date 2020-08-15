/*SQL - Create Table*/
CREATE TABLE countries(
id_pais INTEGER PRIMARY KEY,
nombre VARCHAR (50) NOT NULL,
codigo INTEGER NOT NULL,
);
/*Change column name*/
ALTER TABLE pedidos /*table name*/
RENAME "codigo cliente" TO cliente;/*column name*/

/*SQL - Copy CSV to POSTGRES SQL*/
COPY pedidos FROM 'C:\Users\Public\Documents\PEDIDOS.csv' DELIMITER ';' CSV HEADER ENCODING 'windows-1251'

/*Change INTEGER VALUE*/
ALTER TABLE pedidos /*table name*/ 
ALTER COLUMN descuento /*column name*/
TYPE varchar; /*tpye value (varchar,integer,float...*/

/*Calculation query*/
SELECT "nombre articulo", seccion, precio, ROUND(precio*1.21,2) AS "precio iva"
FROM productos;/*round write the quantity of decimals numbers*/

/*date difference*/
SELECT "nombre articulo", seccion, precio,fecha, NOW() AS "hoy", /*NOW () it returns the currents date*/
EXTRACT(DAY FROM NOW() - fecha) AS "diferencia dias"/*calculate the difference between 2 dates*/
FROM productos
WHERE seccion = 'deportes';

/*INNER JOIN*/
SELECT *
FROM clientes
INNER JOIN pedidos
ON clientes.cliente = pedidos."cliente codigo"/*"table_name.column_name = ......*/
WHERE poblacion = 'MADRID'
/*seleccionamos las tablas que queremos que aparezcan y si tienen mimso nombre seleccionamos tabla*/
SELECT clientes.cliente, poblacion, direccion,"numero de pedido", "cliente codigo", "forma de pago"
FROM clientes
INNER JOIN pedidos
ON clientes.cliente = pedidos."cliente codigo"
WHERE poblacion = 'MADRID';
