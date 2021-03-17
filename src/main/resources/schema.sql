-- products_schema.products definition

-- Drop table

-- DROP TABLE products_schema.products;

CREATE TABLE IF NOT EXISTS products (
	id serial NOT NULL ,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	price float8 NOT NULL,
	CONSTRAINT products_pkey PRIMARY KEY (id)
);