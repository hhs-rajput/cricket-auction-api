CREATE TABLE mpl.players (
    player_id SERIAL PRIMARY KEY,
    player_name VARCHAR(100),
    base_price INTEGER DEFAULT 0,
    sold BOOLEAN DEFAULT FALSE,
    caption BOOLEAN DEFAULT FALSE,
    sold_price INTEGER DEFAULT 0,
    team_id INTEGER,
    phone_number VARCHAR(20),
    player_category VARCHAR(10),
    user_id INTEGER,   
    
    CONSTRAINT fk_player_user
        FOREIGN KEY (user_id)
        REFERENCES mpl.users(user_id)
        ON DELETE SET NULL
);

CREATE TABLE mpl.users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT null UNIQUE,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    team VARCHAR(100),
    password VARCHAR(100) NOT NULL
);
CREATE TABLE mpl.auction (
    auction_id SERIAL PRIMARY KEY,
    auction_name VARCHAR(255) not null,
    active BOOLEAN DEFAULT FALSE,
    status VARCHAR(255) not null,
    auction_date TIMESTAMP not null
);



CREATE TABLE mpl.teams (
    id SERIAL PRIMARY KEY,
    team_name VARCHAR(255) NOT NULL UNIQUE,
    team_size INTEGER CHECK (team_size > 0),
    team_status VARCHAR(10) NOT NULL ,
    auction_joined BOOLEAN DEFAULT FALSE,
    caption_user_id INTEGER NOT NULL unique,
    auction_id INTEGER ,
    total_purse INTEGER,
	remaining_purse INTEGER
);

CREATE TABLE mpl.player_bid (
    bid_id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL ,
    team_id INTEGER  not null,
    auction_id INTEGER  not null,
    caption_user_id INTEGER NOT null,
    bid_amount INTEGER
);













