
CREATE TABLE IF NOT EXISTS host(
	id_host SERIAL NOT NULL UNIQUE,
	is_superhost BOOLEAN NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	host_name VARCHAR(20) NOT NULL,
	surname VARCHAR(20) NOT NULL,
	host_address VARCHAR(50) NOT NULL,
	PRIMARY KEY (id_host),

	CONSTRAINT check_email_format CHECK ( email LIKE '%@%.%' )
);

CREATE TABLE IF NOT EXISTS tenant(
	id_tenant SERIAL NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	tenant_name VARCHAR(20) NOT NULL,
	surname VARCHAR(20) NOT NULL,
	tenant_address VARCHAR(50) NOT NULL,
	PRIMARY KEY (id_tenant),

	CONSTRAINT check_email_format CHECK ( email LIKE '%@%.%' )
);

CREATE TABLE IF NOT EXISTS accomodation(
	id_accomodation SERIAL NOT NULL UNIQUE,
    accomodation_name VARCHAR(20) NOT NULL,
    n_rooms INTEGER NOT NULL,
    accomodation_address VARCHAR(100) NOT NULL,
    id_host INTEGER NOT NULL,
    n_bed_places INTEGER NOT NULL,
    floor INTEGER NOT NULL,
    starter_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    price INTEGER NOT NULL,
    PRIMARY KEY (id_accomodation),
	FOREIGN KEY (id_host) REFERENCES host(id_host),
 
	CONSTRAINT check_price CHECK ((price) >= 10 AND (price) <= 50000) 
); 

CREATE TABLE IF NOT EXISTS reservation(
	id_reservation SERIAL NOT NULL UNIQUE,
	reservation_start_date DATE NOT NULL,
	reservation_end_date DATE NOT NULL,
	id_tenant INTEGER NOT NULL,
	id_accomodation INTEGER NOT NULL,
	PRIMARY KEY (id_reservation),
	FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
	FOREIGN KEY (id_accomodation) REFERENCES accomodation(id_accomodation),
	
	CONSTRAINT check_dates CHECK (reservation_end_date > reservation_start_date)
);

CREATE TABLE IF NOT EXISTS feedback(
	id_feed SERIAL NOT NULL UNIQUE,
	title VARCHAR(100) ,
	text_feedback TEXT ,
	points INTEGER NOT NULL,
	id_reservation INTEGER NOT NULL,
	PRIMARY KEY (id_feed),
	FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation),
	
	CONSTRAINT check_points CHECK (points >= 1 AND points <= 5)
);



CREATE INDEX IF NOT EXISTS idx_accomodation_id_host ON accomodation(id_host); 
CREATE INDEX IF NOT EXISTS idx_feedback_id_reservation ON feedback(id_reservation);
CREATE INDEX IF NOT EXISTS idx_reservation_id_tenant ON reservation(id_tenant);
CREATE INDEX IF NOT EXISTS idx_reservation_id_accomodation ON reservation(id_accomodation);


CREATE OR REPLACE FUNCTION toggle_superhost(host_id INT) 
RETURNS BOOLEAN AS $$ 
BEGIN
    IF (SELECT COUNT(*) 
        FROM reservation r
        JOIN accomodation a ON r.id_accomodation = a.id_accomodation
        WHERE a.id_host = host_id) >= 100 
    THEN
        UPDATE host 
        SET is_superhost = TRUE 
        WHERE id_host = host_id;
        RETURN TRUE;
    ELSE
        UPDATE host 
        SET is_superhost = FALSE 
        WHERE id_host = host_id;
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION check_superhost_after_reservation() 
RETURNS TRIGGER AS $$
BEGIN
    PERFORM toggle_superhost(
        (SELECT id_host FROM accomodation WHERE id_accomodation = NEW.id_accomodation)
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_check_superhost
AFTER INSERT ON reservation
FOR EACH ROW
EXECUTE FUNCTION check_superhost_after_reservation();