ALTER TABLE feedback DROP COLUMN IF EXISTS feedback;
ALTER TABLE feedback ADD COLUMN feedback VARCHAR(500) NOT NULL;

ALTER TABLE response DROP COLUMN IF EXISTS response;
ALTER TABLE response ADD COLUMN response VARCHAR(500) NOT NULL;
