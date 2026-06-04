-- @author: Dawid Sikora

-- @table refresh_tokens
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id              UUID PRIMARY KEY,
    user_id         UUID                             NOT NULL,
    hashed_token    VARCHAR(128)                     NOT NULL,
    expires_at TIMESTAMPTZ                           NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_refresh_tokens_hashed_name UNIQUE (hashed_token),
    CONSTRAINT uk_refresh_tokens_user_id UNIQUE (user_id)
);

COMMENT ON TABLE refresh_tokens IS 'Tabela przechowująca tokeny odświeżające użytkowników';
