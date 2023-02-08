-- GO
DELIMITER $$
	-- =============================================
    -- Procedure: SET Users
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pSetUsers;
	CREATE PROCEDURE pSetUsers (
		IN pId					CHAR(36),
        IN pUsername			VARCHAR(250),
        IN pPassword			VARCHAR(250),
        IN pFullName			VARCHAR(500),
        IN pFirstName			VARCHAR(250),
        IN pLastName			VARCHAR(250),
        IN pEmailAddress		VARCHAR(550),
        IN pRoleId				VARCHAR(36)
	)
    BEGIN
		DECLARE RowExists INT DEFAULT 0;
        
		IF pId IS NOT NULL THEN
			SELECT COUNT(*) INTO RowExists FROM Users WHERE Id = pId;
		ELSEIF pUsername IS NOT NULL THEN
			SELECT COUNT(*) INTO RowExists FROM Users WHERE Username = pUsername;
		END IF;
        
        IF RowExists > 0 THEN
			UPDATE Users
            SET
				Username = COALESCE(pUsername, Username),
				Password = COALESCE(pPassword, Password),
				FirstName = COALESCE(pFirstName, FirstName),
				LastName = COALESCE(pLastName, LastName),
				EmailAddress = COALESCE(pEmailAddress, EmailAddress),
                RoleId = COALESCE(pRoleId, RoleId)
			WHERE
				CASE
					WHEN pId IS NOT NULL THEN Id = pId
                    WHEN pUsername IS NOT NULL THEN Username = pUsername
				END;
        ELSE
			INSERT INTO Users (Id, Username, Password, FullName, FirstName, LastName, EmailAddress, RoleId)
            VALUES (
				COALESCE(pId, UUID()),
				pUsername,
                pPassword,
                COALESCE(pFullName, DEFAULT(FullName)),
                COALESCE(pFirstName, DEFAULT(FirstName)),
                COALESCE(pLastName, DEFAULT(LastName)),
                pEmailAddress,
                COALESCE(pRoleId, DEFAULT(RoleId))
			);
		END IF;
	END;
$$

-- GO
DELIMITER $$
    -- =============================================
    -- Procedure: GET Users
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pGetUsers;
    CREATE PROCEDURE pGetUsers (
		IN pId					CHAR(36),
        IN pUsername			VARCHAR(250),
        IN pPassword			VARCHAR(250),
        IN pFullName			VARCHAR(500),
        IN pFirstName			VARCHAR(250),
        IN pLastName			VARCHAR(250),
        IN pEmailAddress		VARCHAR(550),
        IN pRoleId				CHAR(36)
	)
    BEGIN
		IF pId IS NOT NULL THEN
			SELECT * FROM vUsers WHERE Id = pId;
		ELSEIF pUsername IS NOT NULL THEN
			SELECT * FROM vUsers WHERE Username = pUsername;
		ELSE
			SELECT * FROM vUsers WHERE
				(Password = pPassword OR pPassword IS NULL)
				AND (FullName = pFullName OR pFullName IS NULL)
                AND (FirstName = pFirstName OR pFirstName IS NULL)
                AND (LastName = pLastName OR pLastName IS NULL)
                AND (EmailAddress = pEmailAddress OR pEmailAddress IS NULL)
                AND (RoleId = pRoleId OR pRoleId IS NULL);
		END IF;
	END;
    
-- GO
DELIMITER $$
    -- =============================================
    -- Procedure: DEL Users
    -- =============================================
    -- DROP PROCEDURE IF EXISTS pDelUsers;
    CREATE PROCEDURE pDelUsers (
		IN pId					CHAR(36),
        IN pUsername			VARCHAR(250),
        IN pPassword			VARCHAR(250),
        IN pFullName			VARCHAR(500),
        IN pFirstName			VARCHAR(250),
        IN pLastName			VARCHAR(250),
        IN pEmailAddress		VARCHAR(550),
        IN pRoleId				CHAR(36)
	)
    BEGIN
		IF pId IS NOT NULL THEN
			DELETE FROM Users WHERE Id = pId;
        ELSEIF pUsername IS NOT NULL THEN
			DELETE FROM Users WHERE Username = pUsername;
        ELSE
			DELETE FROM Users WHERE
				(FullName = pFullName OR pFullName IS NULL)
                AND (FirstName = pFirstName OR pFirstName IS NULL)
                AND (LastName = pLastName OR pLastName IS NULL)
                AND (EmailAddress = pEmailAddress OR pEmailAddress IS NULL)
                AND (RoleId = pRoleId OR pRoleId IS NULL);
        END IF;
    END;
$$
DELIMITER ;