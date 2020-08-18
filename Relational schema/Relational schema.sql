CREATE TABLE customer
	(cid		SERIAL PRIMARY KEY,
	customer_name	VARCHAR(70) NOT NULL,
	balance		NUMERIC(8, 2) CHECK(balance >= 0) DEFAULT 0,
	username	VARCHAR(32) NOT NULL UNIQUE,
	password	VARCHAR(32) NOT NULL
	);

CREATE TABLE address
	(address_id	SERIAL PRIMARY KEY,
	street_number	INT NOT NULL,
	street_name	VARCHAR(72) NOT NULL,
	apt_number	VARCHAR(10),
	city		VARCHAR(68) NOT NULL,
	state		CHAR(2),
		CONSTRAINT address_state_constraint CHECK (state IN ('AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA', 'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD', 'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY')),
	zip		CHAR(9) NOT NULL,
	country		VARCHAR(60) NOT NULL
	);

CREATE TABLE product
	(product_name	VARCHAR(22) PRIMARY KEY,
	category	VARCHAR(20)
		CHECK(category IN ('Produce', 'Meat', 'Eggs and Dairy', 'Bakery', 'Frozen Foods', 'Snack Foods', 'Alcoholic Drinks')),
	size		NUMERIC(5, 2) CHECK(size > 0),
	add_info	VARCHAR(100),
	description	VARCHAR(100) DEFAULT 'default.jpg'
	);

CREATE TABLE credit_card
	(cc_number	CHAR(16) PRIMARY KEY,
	cc_name		VARCHAR(70) NOT NULL,
	cvv		CHAR(4) NOT NULL,
	exp_date	DATE NOT NULL,
	cid		SERIAL NOT NULL REFERENCES customer,
	address_id	SERIAL NOT NULL REFERENCES address
	);

CREATE TABLE staff
	(sid		SERIAL PRIMARY KEY,
	staff_name	VARCHAR(70) NOT NULL,
	salary		NUMERIC(8, 2) CHECK(salary > 0),
	job_title	VARCHAR(50),
	username	VARCHAR(32) NOT NULL UNIQUE,
	password	VARCHAR(32) NOT NULL,
	address_id	SERIAL NOT NULL REFERENCES address
	);

CREATE TABLE pricing
	(product_name	VARCHAR(22) REFERENCES product,
	state		CHAR(2),
		CONSTRAINT pricing_state_constraint CHECK (state IN ('AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA', 'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD', 'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY')),
	price		NUMERIC(7, 2) CHECK(price > 0),
	PRIMARY KEY (product_name, state)
	);

CREATE TABLE delivery_address
	(cid		SERIAL REFERENCES customer,
	address_id	SERIAL REFERENCES address,
	PRIMARY KEY (cid, address_id)
	);

CREATE TABLE cart_contents
	(cid		SERIAL REFERENCES customer,
	product_name	VARCHAR(22) REFERENCES product,
	quantity	INT CHECK(quantity > 0),
	PRIMARY KEY (cid, product_name)
	);

CREATE TABLE warehouse
	(wid		SERIAL PRIMARY KEY,
	capacity	NUMERIC(12, 2) CHECK(capacity > 0),
	address_id	SERIAL NOT NULL REFERENCES address
	);

CREATE TABLE orders
	(oid		SERIAL PRIMARY KEY,
	wid		SERIAL NOT NULL REFERENCES warehouse,
	date_issued	TIMESTAMP NOT NULL,
	status		VARCHAR(20) CHECK(status IN ('Processing', 'Shipping', 'Completed', 'Cancelled')),
	cid		SERIAL NOT NULL REFERENCES customer,
	cc_number	CHAR(16) NOT NULL REFERENCES credit_card
	);

CREATE TABLE order_contents
	(oid 		SERIAL REFERENCES orders,
	product_name	VARCHAR(22) NOT NULL REFERENCES product,
	quantity	INT CHECK(quantity > 0),
	PRIMARY KEY (oid, product_name)
	);

CREATE TABLE warehouse_stock
	(wid		SERIAL REFERENCES warehouse,
	product_name	VARCHAR(22) REFERENCES product,
	stock		INT CHECK(stock >= 0),
	PRIMARY KEY (wid, product_name)
	);