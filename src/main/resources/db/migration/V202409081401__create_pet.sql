CREATE TABLE pet
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL ,
    type varchar(255) NOT NULL ,
    company_id BIGINT NOT NULL ,
    date_of_arrival DATE DEFAULT current_date
);

CREATE UNIQUE INDEX idx_pet_on_company_id ON pet(company_id);

-- INSERT INTO pet (name, type, company_id, date_of_arrival)
-- VALUES ('Angie', 'dog', '9007199254740991n', '2010-05-14');

-- INSERT INTO pet (name, type, company_id, date_of_arrival)
-- VALUES ('Nessy', 'dog', '9007199254740991n', '2004-08-20');

-- INSERT INTO pet (name, type, company_id, date_of_arrival)
-- VALUES ('Max', 'cat', '9007199254740991n', '2010-05-10');

-- INSERT INTO pet (name, type, company_id, date_of_arrival)
-- VALUES ('Spot', 'rabbit', '9007199254740991n', '2024-09-01');

-- INSERT INTO pet(name, type, company_id, date_of_arrival)
-- VALUES ('Charlie', 'bird', '9007199254740991n', '2024-09-08');

-- SELECT DISTINCT * FROM pet WHERE type = 'dog'

-- DELETE FROM pet WHERE id = '98d200c4-fad5-4980-b72a-23f08f27bf63';

-- SELECT * FROM pet WHERE arrival_date < CURRENT_DATE - INTERVAL '1 year';
