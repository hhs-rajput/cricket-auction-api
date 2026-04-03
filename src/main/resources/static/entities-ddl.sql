CREATE TABLE mpl.players (
    player_id SERIAL PRIMARY KEY,
    player_name VARCHAR(100),
    player_role varchar(50),
    base_price INTEGER DEFAULT 0,
    sold BOOLEAN DEFAULT FALSE,
    caption BOOLEAN DEFAULT FALSE,
    sold_price INTEGER DEFAULT 0,
    team_id INTEGER,
    retention_status varchar(50),
    phone_number VARCHAR(20),
    player_category VARCHAR(10),
    user_id INTEGER,

    CONSTRAINT fk_player_user
        FOREIGN KEY (user_id)
        REFERENCES mpl.users(user_id)
        ON DELETE SET null,

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
    caption_user_id INTEGER NOT NULL unique
);



CREATE TABLE mpl.auction_team_mapping (
    auction_team_mapping_id SERIAL PRIMARY KEY,
    auction_id INTEGER NOT NULL,
    team_id INTEGER  NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    caption_user_id INTEGER NOT null,
    total_purse INTEGER,
	remaining_purse INTEGER,
	CONSTRAINT uq_auction_team UNIQUE (auction_id, team_id)
);







CREATE TABLE mpl.player_bid_transactions  (
    player_bid_id SERIAL PRIMARY KEY,
    auction_id INTEGER NOT NULL,
    INTEGER  ,
    player_id INTEGER  NOT NULL,
    created_by INTEGER  NOT NULL,
    status VARCHAR(10) NOT null,
    last_updated_by INTEGER  NOT NULL
);

CREATE TABLE mpl.player_bid (
    player_bid_id SERIAL PRIMARY KEY,
    auction_id INTEGER NOT NULL,
    leading_team_id INTEGER,
    player_id INTEGER NOT NULL,
    player_base_price INTEGER NOT NULL,
    bid_amount INTEGER,
    status VARCHAR(20) NOT NULL,
    created_by INTEGER NOT NULL,
    last_updated_by INTEGER NOT NULL,
    CONSTRAINT unique_auction_player UNIQUE (auction_id, player_id),
    CONSTRAINT unique_auction_player_status UNIQUE (auction_id,player_id, status)
);

CREATE TABLE mpl.player_bid_transactions (
	transaction_id SERIAL PRIMARY key,
    player_bid_id INTEGER NOT NULL,
    auction_id INTEGER NOT NULL,
    team_id INTEGER,
    player_id INTEGER NOT NULL,
    player_base_price INTEGER NOT NULL,
    bid_amount INTEGER,
    created_by INTEGER NOT NULL
);