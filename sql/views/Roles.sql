-- GO
DELIMITER $$
	-- =============================================
	-- View: Roles
	-- Select from Table: SELECT * FROM Roles LIMIT 1000;
	-- Select from View: SELECT * FROM vRoles LIMIT 1000;
	-- =============================================
	-- DROP VIEW IF EXISTS vRoles;
	CREATE VIEW vRoles
	AS
	SELECT
		Roles.Id		AS Id,
		Roles.Name		AS Name
	FROM Roles;
$$
DELIMITER ;