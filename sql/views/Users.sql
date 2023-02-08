-- GO
DELIMITER $$
	-- =============================================
	-- View: Users
	-- Select from Table: SELECT * FROM Users LIMIT 1000;
	-- Select from View: SELECT * FROM vUsers LIMIT 1000;
	-- =============================================
	-- DROP VIEW IF EXISTS vUsers;
	CREATE VIEW vUsers
	AS
	SELECT
		Users.Id			AS Id,
		Users.Username		AS Username,
		Users.Password		AS Password,
		Users.FullName		AS FullName,
		Users.FirstName		AS FirstName,
		Users.LastName		AS LastName,
		Users.EmailAddress	AS EmailAddress,
        Users.RoleId		AS RoleId,
        Roles.Name			AS RoleName
	FROM Users
    LEFT OUTER JOIN Roles  	ON Roles.Id = Users.RoleId;

$$
DELIMITER ;