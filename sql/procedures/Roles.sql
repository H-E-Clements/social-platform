-- GO
DELIMITER $$
	-- =============================================
    -- Procedure: SET Roles
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pSetRoles;
	CREATE PROCEDURE pSetRoles (
		IN pId			CHAR(36),
        IN pName		VARCHAR(250)
	)
    BEGIN
		DECLARE RowExists INT DEFAULT 0;
        
		IF pId IS NOT NULL THEN
			SELECT COUNT(*) INTO RowExists FROM Roles WHERE Id = pId;
		ELSEIF pName IS NOT NULL THEN
			SELECT COUNT(*) INTO RowExists FROM Roles WHERE Name = pName;
		END IF;
        
        IF RowExists > 0 THEN
			UPDATE Roles
            SET
				Name = COALESCE(pName, Name)
			WHERE
				CASE
					WHEN pId IS NOT NULL THEN Id = pId
                    WHEN pName IS NOT NULL THEN Name = pName
				END;
        ELSE
			INSERT INTO Roles (Id, Name)
			VALUES (
				COALESCE(pId, UUID()),
				pName
            );
		END IF;
	END;
$$
DELIMITER ;

DELIMITER $$
    -- =============================================
    -- Procedure: GET Roles
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pGetRoles;
    CREATE PROCEDURE pGetRoles (
		IN pId			CHAR(36),
        IN pName		VARCHAR(250)
	)
    BEGIN
		SELECT * FROM vRoles WHERE
			(Id = pId OR pId IS NULL)
			AND (Name = pName OR pName IS NULL);
	END;
$$
DELIMITER ;

DELIMITER $$
    -- =============================================
    -- Procedure: DEL Roles
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pDelRoles;
    CREATE PROCEDURE pDelRoles (
		IN pId			CHAR(36),
        IN pName		VARCHAR(250)
	)
    BEGIN
		DELETE FROM Roles WHERE
			(Id = pId OR pId IS NULL)
			AND (Name = pName OR pName IS NULL);
    END;
$$
DELIMITER ;