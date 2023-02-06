<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Registration and Login Page</title>
	<style>
		body {
			background-color: #f2f2f2;
			font-family: Arial, sans-serif;
		}
		.container {
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			align-items: center;
			height: 100vh;
		}
		.form-container {
			background-color: #ffffff;
			padding: 2rem;
			border-radius: 1rem;
			box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
			width: 30rem;
			text-align: center;
		}
		h1 {
			margin-bottom: 2rem;
			color: #0077c9;
		}
		input[type="text"],
		input[type="email"],
		input[type="password"] {
			width: 100%;
			padding: 0.5rem;
			margin-bottom: 1.5rem;
			border: none;
			border-bottom: 2px solid #0077c9;
			font-size: 1.2rem;
		}
		input[type="submit"] {
			background-color: #0077c9;
			color: #ffffff;
			padding: 0.8rem;
			border-radius: 0.5rem;
			border: none;
			font-size: 1.2rem;
			cursor: pointer;
		}
		input[type="submit"]:hover {
			background-color: #005dab;
		}
		a {
			color: #0077c9;
			margin-top: 1rem;
			font-size: 1.2rem;
			text-decoration: none;
		}
	</style>
</head>
<body>
<div class="container">
	<div class="form-container">
		<h1>Registration</h1>
		<form>
			<input type="text" placeholder="First Name" required />
			<input type="text" placeholder="Surname" required />
			<input type="email" placeholder="Email" required />
			<input type="password" placeholder="Password" required />
			<input type="password" placeholder="Confirm Password" required />
			<input type="submit" value="Register" />
		</form>
	</div>
	<div class="form-container">
		<h1>Login</h1>
		<form action="/myLogin" method="post">
			User Name : <input type="text" name="username" /><br/>
			Password: <input type="password" name="password" /><br/>
			<input type="submit" value="Sign In" /><br/>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</div>
</div>
</body>
</html>
