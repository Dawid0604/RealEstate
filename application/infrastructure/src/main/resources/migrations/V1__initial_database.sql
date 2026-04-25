-- @author: Dawid Sikora

-- @table: localities
CREATE TABLE IF NOT EXISTS localities
(
    id         UUID PRIMARY KEY,
    name       varchar(128)                          NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_localities_name UNIQUE (name)
);

COMMENT ON TABLE localities IS 'Tabela przechowująca lokalizacje';

-- @table: users
CREATE TABLE IF NOT EXISTS users
(
    id                        UUID PRIMARY KEY,
    email                     varchar(254)                          NOT NULL,
    password                  varchar(72)                           NOT NULL,
    first_name                varchar(128),
    last_name                 varchar(128),
    notification_email        varchar(254),
    notification_phone_number varchar(128),
    avatar_url                varchar(1024),
    role                      varchar(20)                           NOT NULL,
    type                      varchar(20)                           NOT NULL,
    status                    varchar(20)                           NOT NULL,
    last_login_at             TIMESTAMPTZ,
    created_at                TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_email UNIQUE (email)
);

COMMENT ON TABLE users IS 'Tabela przechowująca użytkowników';

-- @table: flat_advertisements
CREATE TABLE IF NOT EXISTS flat_advertisements
(
    id                     UUID PRIMARY KEY,
    slug                   VARCHAR(100)                          NOT NULL,
    title                  VARCHAR(100)                          NOT NULL,
    description            TEXT,
    price                  NUMERIC(10, 2) CHECK ( price > 0 ),
    area                   NUMERIC(10, 2) CHECK ( area > 0 ),
    price_per_square_meter NUMERIC(10, 2) CHECK ( price_per_square_meter >= 0 ),
    locality_id            UUID                                  NOT NULL,
    user_id                UUID                                  NOT NULL,
    is_featured            BOOLEAN                               NOT NULL,
    status                 VARCHAR(20)                           NOT NULL,
    building_type          VARCHAR(20)                           NOT NULL,
    number_of_rooms        INT CHECK ( number_of_rooms > 0 ),
    floor                  INT CHECK ( floor >= 0 AND floor <= floors ),
    floors                 INT CHECK ( floors >= 0 ),
    built_year             INT CHECK ( built_year > 0 ),
    type_of_market         VARCHAR(20),
    created_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_flat_advertisements_slug UNIQUE (slug),
    constraint fk_flat_advertisements_locality_id
        foreign key (locality_id)
            references localities (id)
            On delete cascade,

    constraint fk_flat_advertisements_user_id
        foreign key (user_id)
            references users (id)
            On delete cascade
);

COMMENT ON TABLE flat_advertisements IS 'Tabela przechowująca ogłoszenia z mieszkaniami';
COMMENT ON COLUMN flat_advertisements.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN flat_advertisements.user_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN flat_advertisements.locality_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_flat_advertisements_user_id ON flat_advertisements (user_id);
CREATE INDEX idx_flat_advertisements_locality_id ON flat_advertisements (locality_id);
CREATE INDEX idx_flat_advertisements_status ON flat_advertisements (status);

-- @table: commercial_advertisements
CREATE TABLE IF NOT EXISTS commercial_advertisements
(
    id                     UUID PRIMARY KEY,
    slug                   VARCHAR(100)                          NOT NULL,
    title                  VARCHAR(100)                          NOT NULL,
    description            TEXT,
    price                  NUMERIC(10, 2) CHECK ( price > 0 ),
    area                   NUMERIC(10, 2) CHECK ( area > 0 ),
    price_per_square_meter NUMERIC(10, 2) CHECK ( price_per_square_meter >= 0 ),
    locality_id            UUID                                  NOT NULL,
    user_id                UUID                                  NOT NULL,
    is_featured            BOOLEAN                               NOT NULL,
    status                 VARCHAR(20)                           NOT NULL,
    building_type          VARCHAR(20)                           NOT NULL,
    number_of_rooms        INT CHECK ( number_of_rooms > 0 ),
    floor                  INT CHECK ( floor >= 0 AND floor <= floors ),
    floors                 INT CHECK ( floors >= 0 ),
    built_year             INT CHECK ( built_year > 0 ),
    type_of_market         VARCHAR(20),
    created_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_commercial_advertisements_slug UNIQUE (slug),
    constraint fk_commercial_advertisements_locality_id
        foreign key (locality_id)
            references localities (id)
            On delete cascade,

    constraint fk_commercial_advertisements_user_id
        foreign key (user_id)
            references users (id)
            On delete cascade
);

COMMENT ON TABLE commercial_advertisements IS 'Tabela przechowująca ogłoszenia z budynkami komercyjnymi';
COMMENT ON COLUMN commercial_advertisements.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN commercial_advertisements.user_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN commercial_advertisements.locality_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_commercial_advertisements_user_id ON commercial_advertisements (user_id);
CREATE INDEX idx_commercial_advertisements_locality_id ON commercial_advertisements (locality_id);
CREATE INDEX idx_commercial_advertisements_status ON commercial_advertisements (status);

-- @table: house_advertisements
CREATE TABLE IF NOT EXISTS house_advertisements
(
    id                     UUID PRIMARY KEY,
    slug                   VARCHAR(100)                          NOT NULL,
    title                  VARCHAR(100)                          NOT NULL,
    description            TEXT,
    price                  NUMERIC(10, 2) CHECK ( price > 0 ),
    area                   NUMERIC(10, 2) CHECK ( area > 0 ),
    price_per_square_meter NUMERIC(10, 2) CHECK ( price_per_square_meter >= 0 ),
    locality_id            UUID                                  NOT NULL,
    user_id                UUID                                  NOT NULL,
    is_featured            BOOLEAN                               NOT NULL,
    status                 VARCHAR(20)                           NOT NULL,
    building_type          VARCHAR(20)                           NOT NULL,
    number_of_rooms        INT CHECK ( number_of_rooms > 0 ),
    floors                 INT CHECK ( floors >= 0 ),
    built_year             INT CHECK ( built_year > 0 ),
    type_of_market         VARCHAR(20),
    created_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_house_advertisements_slug UNIQUE (slug),
    constraint fk_house_advertisements_locality_id
        foreign key (locality_id)
            references localities (id)
            On delete cascade,

    constraint fk_house_advertisements_user_id
        foreign key (user_id)
            references users (id)
            On delete cascade
);

COMMENT ON TABLE house_advertisements IS 'Tabela przechowująca ogłoszenia z domami';
COMMENT ON COLUMN house_advertisements.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN house_advertisements.user_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN house_advertisements.locality_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_house_advertisements_user_id ON house_advertisements (user_id);
CREATE INDEX idx_house_advertisements_locality_id ON house_advertisements (locality_id);
CREATE INDEX idx_house_advertisements_status ON house_advertisements (status);

-- @table: plot_advertisements
CREATE TABLE IF NOT EXISTS plot_advertisements
(
    id                     UUID PRIMARY KEY,
    slug                   VARCHAR(100)                          NOT NULL,
    title                  VARCHAR(100)                          NOT NULL,
    description            TEXT,
    price                  NUMERIC(10, 2) CHECK ( price > 0 ),
    area                   NUMERIC(10, 2) CHECK ( area > 0 ),
    price_per_square_meter NUMERIC(10, 2) CHECK ( price_per_square_meter >= 0 ),
    locality_id            UUID                                  NOT NULL,
    user_id                UUID                                  NOT NULL,
    is_featured            BOOLEAN                               NOT NULL,
    status                 VARCHAR(20)                           NOT NULL,
    plot_type              VARCHAR(20)                           NOT NULL,
    created_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_plot_advertisements_slug UNIQUE (slug),
    constraint fk_plot_advertisements_locality_id
        foreign key (locality_id)
            references localities (id)
            On delete cascade,

    constraint fk_plot_advertisements_user_id
        foreign key (user_id)
            references users (id)
            On delete cascade
);

COMMENT ON TABLE plot_advertisements IS 'Tabela przechowująca ogłoszenia z gruntami';
COMMENT ON COLUMN plot_advertisements.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN plot_advertisements.user_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN plot_advertisements.locality_id IS 'Klucz obcy w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_plot_advertisements_user_id ON plot_advertisements (user_id);
CREATE INDEX idx_plot_advertisements_locality_id ON plot_advertisements (locality_id);
CREATE INDEX idx_plot_advertisements_status ON plot_advertisements (status);

-- @table: flat_advertisements_claims
CREATE TABLE IF NOT EXISTS flat_advertisements_claims
(
    id               UUID PRIMARY KEY,
    claim_key        VARCHAR(100)                          NOT NULL,
    claim_value      VARCHAR(1024)                         NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_flat_advertisements_claims_key UNIQUE (claim_key),
    constraint fk_flat_advertisements_claims_advertisement_id
        foreign key (advertisement_id)
            references flat_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE flat_advertisements_claims IS 'Tabela przechowująca metadane ogłoszeń mieszkań';
COMMENT ON COLUMN flat_advertisements_claims.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN flat_advertisements_claims.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_flat_advertisements_claims_advertisement_id ON flat_advertisements_claims (advertisement_id);

-- @table: house_advertisements_claims
CREATE TABLE IF NOT EXISTS house_advertisements_claims
(
    id               UUID PRIMARY KEY,
    claim_key        VARCHAR(100)                          NOT NULL,
    claim_value      VARCHAR(1024)                         NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_house_advertisements_claims_key UNIQUE (claim_key),
    constraint fk_house_advertisements_claims_advertisement_id
        foreign key (advertisement_id)
            references house_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE house_advertisements_claims IS 'Tabela przechowująca metadane ogłoszeń domów';
COMMENT ON COLUMN house_advertisements_claims.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN house_advertisements_claims.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_house_advertisements_claims_advertisement_id ON house_advertisements_claims (advertisement_id);

-- @table: commercial_advertisements_claims
CREATE TABLE IF NOT EXISTS commercial_advertisements_claims
(
    id               UUID PRIMARY KEY,
    claim_key        VARCHAR(100)                          NOT NULL,
    claim_value      VARCHAR(1024)                         NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_commercial_advertisements_claims_key UNIQUE (claim_key),
    constraint fk_commercial_advertisements_claims_advertisement_id
        foreign key (advertisement_id)
            references commercial_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE commercial_advertisements_claims IS 'Tabela przechowująca metadane ogłoszeń budynków komercyjnych';
COMMENT ON COLUMN commercial_advertisements_claims.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN commercial_advertisements_claims.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_commercial_advertisements_claims_advertisement_id ON commercial_advertisements_claims (advertisement_id);

-- @table: plot_advertisements_claims
CREATE TABLE IF NOT EXISTS plot_advertisements_claims
(
    id               UUID PRIMARY KEY,
    claim_key        VARCHAR(100)                          NOT NULL,
    claim_value      VARCHAR(1024)                         NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_plot_advertisements_claims_key UNIQUE (claim_key),
    constraint fk_plot_advertisements_claims_advertisement_id
        foreign key (advertisement_id)
            references plot_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE plot_advertisements_claims IS 'Tabela przechowująca metadane ogłoszeń gruntów';
COMMENT ON COLUMN plot_advertisements_claims.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN plot_advertisements_claims.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_plot_advertisements_claims_advertisement_id ON plot_advertisements_claims (advertisement_id);

-- @table: flat_advertisements_photos
CREATE TABLE IF NOT EXISTS flat_advertisements_photos
(
    id               UUID PRIMARY KEY,
    url              varchar(1024)                         NOT NULL,
    position         INT check ( position >= 0 )           NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_flat_advertisements_photos_url UNIQUE (url),
    constraint fk_flat_advertisements_photos_advertisement_id
        foreign key (advertisement_id)
            references flat_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE flat_advertisements_photos IS 'Tabela przechowująca zdjęcia ogłoszeń mieszkań';
COMMENT ON COLUMN flat_advertisements_photos.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN flat_advertisements_photos.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_flat_advertisements_photos_advertisement_id ON flat_advertisements_photos (advertisement_id);

-- @table: house_advertisements_photos
CREATE TABLE IF NOT EXISTS house_advertisements_photos
(
    id               UUID PRIMARY KEY,
    url              varchar(1024)                         NOT NULL,
    position         INT check ( position >= 0 )           NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_house_advertisements_photos_url UNIQUE (url),
    constraint fk_house_advertisements_photos_advertisement_id
        foreign key (advertisement_id)
            references house_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE house_advertisements_photos IS 'Tabela przechowująca zdjęcia ogłoszeń domów';
COMMENT ON COLUMN house_advertisements_photos.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN house_advertisements_photos.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_house_advertisements_photos_advertisement_id ON house_advertisements_photos (advertisement_id);

-- @table: commercial_advertisements_photos
CREATE TABLE IF NOT EXISTS commercial_advertisements_photos
(
    id               UUID PRIMARY KEY,
    url              varchar(1024)                         NOT NULL,
    position         INT check ( position >= 0 )           NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_commercial_advertisements_photos_url UNIQUE (url),
    constraint fk_commercial_advertisements_photos_advertisement_id
        foreign key (advertisement_id)
            references commercial_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE commercial_advertisements_photos IS 'Tabela przechowująca zdjęcia ogłoszeń budynków komercyjnych';
COMMENT ON COLUMN commercial_advertisements_photos.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN commercial_advertisements_photos.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_commercial_advertisements_photos_advertisement_id ON commercial_advertisements_photos (advertisement_id);

-- @table: plot_advertisements_photos
CREATE TABLE IF NOT EXISTS plot_advertisements_photos
(
    id               UUID PRIMARY KEY,
    url              varchar(1024)                         NOT NULL,
    position         INT check ( position >= 0 )           NOT NULL,
    advertisement_id UUID                                  NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_plot_advertisements_photos_url UNIQUE (url),
    constraint fk_plot_advertisements_photos_advertisement_id
        foreign key (advertisement_id)
            references plot_advertisements (id)
            On delete cascade
);

COMMENT ON TABLE plot_advertisements_photos IS 'Tabela przechowująca zdjęcia ogłoszeń gruntów';
COMMENT ON COLUMN plot_advertisements_photos.id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';
COMMENT ON COLUMN plot_advertisements_photos.advertisement_id IS 'Klucz główny w formacie UUID v7 (Time-ordered)';

CREATE INDEX idx_plot_advertisements_photos_advertisement_id ON plot_advertisements_photos (advertisement_id);

-- @view: user_advertisements_view
CREATE OR REPLACE VIEW user_advertisements_view AS
SELECT f.id,
       f.slug,
       f.title,
       f.price,
       f.area,
       f.price_per_square_meter,
       f.created_at,
       f.locality_id,
       f.is_featured       as featured,
       f.status,
       'FLAT'::varchar(10) as type,
       f.building_type,
       f.number_of_rooms,
       f.floor,
       f.floors,
       f.built_year,
       f.type_of_market,
       NULL::varchar(20)   as plot_type,
       u.email
FROM flat_advertisements f
         JOIN users u ON f.user_id = u.id

UNION ALL

SELECT f.id,
       f.slug,
       f.title,
       f.price,
       f.area,
       f.price_per_square_meter,
       f.created_at,
       f.locality_id,
       f.is_featured        as featured,
       f.status,
       'HOUSE'::varchar(10) as type,
       f.building_type,
       f.number_of_rooms,
       null::int            as floor,
       f.floors,
       f.built_year,
       f.type_of_market,
       NULL::varchar(20)    as plot_type,
       u.email
FROM house_advertisements f
         JOIN users u ON f.user_id = u.id

UNION ALL

SELECT f.id,
       f.slug,
       f.title,
       f.price,
       f.area,
       f.price_per_square_meter,
       f.created_at,
       f.locality_id,
       f.is_featured             as featured,
       f.status,
       'COMMERCIAL'::varchar(10) as type,
       f.building_type,
       f.number_of_rooms,
       f.floor,
       f.floors,
       f.built_year,
       f.type_of_market,
       NULL::varchar(20)         as plot_type,
       u.email
FROM commercial_advertisements f
         JOIN users u ON f.user_id = u.id

UNION ALL

SELECT f.id,
       f.slug,
       f.title,
       f.price,
       f.area,
       f.price_per_square_meter,
       f.created_at,
       f.locality_id,
       f.is_featured       as featured,
       f.status,
       'PLOT'::varchar(10) as type,
       null::varchar(20)   as building_type,
       null::int           as number_of_rooms,
       null::int           as floor,
       null::int           as floors,
       null::int           as built_year,
       null::varchar(20)   as type_of_market,
       plot_type,
       u.email
FROM plot_advertisements f
         JOIN users u ON f.user_id = u.id
