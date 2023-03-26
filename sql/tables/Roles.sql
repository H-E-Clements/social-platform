-- GO
DELIMITER $$
	-- =============================================
	-- Table: Roles
	-- =============================================
	-- DROP TABLE IF EXISTS Roles;
	CREATE TABLE Roles (
		Id			CHAR(36)			DEFAULT (UUID()) UNIQUE NOT NULL,
		Name		VARCHAR(250)		UNIQUE NOT NULL,
		PRIMARY KEY (Id)			
	);
$$
DELIMITER ;