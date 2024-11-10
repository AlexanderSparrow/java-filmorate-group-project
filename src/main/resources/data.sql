
MERGE INTO MPA
    USING (VALUES ('G') , ('PG'), ('PG-13'), ('R'), ('NC-17')) AS s (source)
    ON MPA.MPA_NAME = s.source
WHEN MATCHED THEN
    UPDATE
        SET MPA.MPA_NAME = source
WHEN NOT MATCHED THEN
    INSERT(MPA_NAME) VALUES(source);

MERGE INTO GENRES
    USING (VALUES ('Комедия') , ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS s (source)
    ON GENRES.GENRE_NAME = s.source
WHEN MATCHED THEN
    UPDATE
        SET GENRES.GENRE_NAME = source
WHEN NOT MATCHED THEN
    INSERT(GENRE_NAME) VALUES(source);