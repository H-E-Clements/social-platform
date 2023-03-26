-- GO
DELIMITER $$
	-- =============================================
	-- Table: Users
	-- =============================================
	-- DROP TABLE IF EXISTS Users;
	CREATE TABLE Users (
		Id				CHAR(36)			DEFAULT (UUID()) UNIQUE NOT NULL,
		Username		VARCHAR(250)		UNIQUE NOT NULL,
		Password		VARCHAR(250)		NOT NULL,
		FullName		VARCHAR(500)		DEFAULT '' NOT NULL,
		FirstName		VARCHAR(250)		DEFAULT '' NOT NULL,
		LastName		VARCHAR(250)		DEFAULT '' NOT NULL,
		EmailAddress	VARCHAR(550)		NOT NULL,
        RoleId			CHAR(36)			DEFAULT '0862646b-a6b9-11ed-9c3a-d278c461b262' NOT NULL,
		PRIMARY KEY (Id),
        FOREIGN KEY (RoleId) REFERENCES Roles(Id)
	);
$$

-- GO
DELIMITER $$
	-- =============================================
	-- Trigger: BEFORE INSERT Users
    -- Sets the user's FullName
	-- =============================================
	-- DROP TRIGGER IF EXISTS tInsertUsers;
	CREATE TRIGGER tInsertUsers
	BEFORE INSERT ON Users
	FOR EACH ROW
	BEGIN
		IF NEW.FirstName IS NOT NULL AND NEW.LastName IS NOT NULL THEN 
			SET NEW.FullName = CONCAT(NEW.FirstName, ' ', NEW.LastName);
		ELSEIF NEW.FirstName IS NOT NULL THEN
			SET NEW.FullName = NEW.FirstName;
		END IF;
	END
$$

-- GO
DELIMITER $$
    -- =============================================
	-- Trigger: BEFORE UPDATE Users
    -- Updates the user's FullName
	-- =============================================
	-- DROP TRIGGER IF EXISTS tUpdateUsers;
	CREATE TRIGGER tUpdateUsers
	BEFORE UPDATE ON Users
	FOR EACH ROW
	BEGIN
		IF NEW.FirstName IS NOT NULL AND NEW.LastName IS NOT NULL THEN 
			SET NEW.FullName = CONCAT(NEW.FirstName, ' ', NEW.LastName);
		ELSEIF NEW.FirstName IS NOT NULL THEN
			SET NEW.FullName = NEW.FirstName;
		END IF;
	END
$$
DELIMITER ;