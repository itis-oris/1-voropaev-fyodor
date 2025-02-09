-- Table: role
CREATE TABLE role (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

-- Table: user
CREATE TABLE "user" (
                      id BIGSERIAL PRIMARY KEY,
                      first_name VARCHAR(255) NOT NULL,
                      last_name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      status VARCHAR(50) NOT NULL,
                      activity_points BIGINT CHECK ( activity_points >= 0 ),
                      password VARCHAR(255) NOT NULL,
                      session_id VARCHAR(255),
                      role_id BIGINT REFERENCES role(id)
);

-- Table: product
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         price BIGINT NOT NULL CHECK ( price >= 0 ),
                         name VARCHAR(255) UNIQUE NOT NULL,
                         description TEXT
);

-- Table: product_specification
CREATE TABLE product_specification (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       product_id BIGINT REFERENCES product(id),
                                       quantity_in_stock BIGINT NOT NULL CHECK ( quantity_in_stock >= 0 )
);

-- Table: product_photo
CREATE TABLE product_photo (
                               id BIGSERIAL PRIMARY KEY,
                               url VARCHAR(255) NOT NULL,
                               "order" BIGINT NOT NULL,
                               product_id BIGINT REFERENCES product(id)
);

-- Table: cart_item
CREATE TABLE cart_item (
                           id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT REFERENCES "user"(id),
                           product_specification_id BIGINT REFERENCES product_specification(id),
                           quantity BIGINT NOT NULL CHECK ( quantity > 0 )
);

-- Table: order
CREATE TABLE "order" (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT REFERENCES "user"(id),
                         status VARCHAR(50) NOT NULL,
                         creation_date_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         update_date_time TIMESTAMP WITH TIME ZONE,
                         description TEXT
);

-- Table: order_item
CREATE TABLE order_item (
                            id BIGSERIAL PRIMARY KEY,
                            order_id BIGINT REFERENCES "order"(id),
                            product_specification_id BIGINT REFERENCES product_specification(id),
                            quantity BIGINT NOT NULL CHECK ( quantity > 0 )
);

-- Add some indexes for performance
CREATE INDEX idx_user_email ON "user" (email);
CREATE INDEX idx_product_name ON product (name);
CREATE INDEX idx_order_user_id ON "order" (user_id);
CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_product_spec_product_id ON product_specification (product_id);