CREATE TABLE pet
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL ,
    type varchar(255) NOT NULL ,
    company_id BIGINT NOT NULL ,
    date_of_arrival DATE DEFAULT current_date
);

CREATE INDEX idx_pets_company_id ON pets(company_id);

-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Angie', 'dog', '50fcdfda-b823-47e6-9bad-541e87163c9a', '2010-05-14');

-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Nessy', 'dog', 'fd320d7a-a288-4f84-9b0a-4648ad40f50e', '2004-08-20');

-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Max', 'cat', '50fcdfda-b823-47e6-9bad-541e87163c9a', '2010-05-10');

-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Spot', 'rabbit', '50fcdfda-b823-47e6-9bad-541e87163c9a', '2024-09-01');

-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Charlie', 'bird', '8ba5f6aa-b0d8-45b8-93a3-78429bcb1ee5', '2024-09-08');

-- SELECT DISTINCT * FROM pets WHERE type = 'dog'

-- DELETE FROM pets WHERE id = '98d200c4-fad5-4980-b72a-23f08f27bf63';

-- SELECT * FROM pets WHERE arrival_date < CURRENT_DATE - INTERVAL '1 year';
