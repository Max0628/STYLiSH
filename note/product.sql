
CREATE TABLE `Product`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    category VARCHAR(50),
    title VARCHAR(255),
    description VARCHAR(255),
    price BIGINT,
    texture VARCHAR(255),
    wash VARCHAR(50),
    place VARCHAR(50),
    note VARCHAR(255),
    story VARCHAR(255),
    main_image_url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE `Color`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50),
    name VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE `Size`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    size VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE `Image`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    url VARCHAR(255),
    product_id BIGINT,
    PRIMARY KEY(id),
    FOREIGN KEY(product_id) REFERENCES Product(id)
);

CREATE TABLE `Variant`(
    size_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,    
    stock INT NOT NULL,
    PRIMARY KEY(product_id,size_id,color_id),
    FOREIGN KEY(size_id) REFERENCES Size(id),
    FOREIGN KEY(color_id) REFERENCES Color(id),
    FOREIGN KEY(product_id) REFERENCES Product(id)
);
