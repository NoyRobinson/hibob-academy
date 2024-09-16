CREATE TABLE vaccineToPet
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    vaccine_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    date_of_vaccination DATE NOT NULL DEFAULT current_date
);
