
<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" media="screen" href="css/feuille.css" />
        <title>Login</title>
    </head>
    <body>
        <h1>Espace de connexion</h1>
        
        <%--
	La servlet fait : request.setAttribute("errorMessage", "Login/Password incorrect");
	La JSP récupère cette valeur dans ${errorMessage}
	--%>
	<div id="erreur">${errorMessage}</div>
        
        <form action="<c:url value="/" />" method="POST">
            <label for="email">Identifiant :</label><input type="text" name="email" ><br>
            <label for="pass">Mot de passe :</label><input type="password" name="pass" ><br>
            <input id="button" type='submit' name='action' value='login'>
        </form>

    </body>
</html>
