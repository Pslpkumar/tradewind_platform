CREATE TABLE orders (
    id          UUID           PRIMARY KEY,
    symbol      VARCHAR(12)    NOT NULL,
    side        VARCHAR(4)     NOT NULL CHECK (side IN ('BUY', 'SELL')),
    quantity    INTEGER        NOT NULL CHECK (quantity > 0),
    limit_price NUMERIC(19, 4) NOT NULL CHECK (limit_price > 0),
    status      VARCHAR(16)    NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL
);

CREATE INDEX idx_orders_symbol ON orders (symbol);