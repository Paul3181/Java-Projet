<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <link rel="stylesheet" type="text/css" media="screen" href="css/feuille.css" />
		<title>Espace admin</title>
                
                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
          
       var donnee = [['Category', 'Profit'],
          ['Software',     ${SW}],
          ['Hardware',      ${HW}],
          ['Firmware',  ${FW}],
          ['Books', ${BK}],
          ['Cables',    ${CB}],
          ['Misc',    ${MS}]] ;

        var data = google.visualization.arrayToDataTable(donnee);

        var options = {
          title: 'Profit by category'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart1'));

        chart.draw(data, options);
      }
    </script>
    
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
          
       var donnee = [['State', 'Profit'],
          ['Florida',${fl}],
          ['Texas',${tx}],
          ['Georgia',${ga}],
          ['California',${ca}],
          ['Michigan',${mi}],
          ['New York',${ny}]] ;

        var data = google.visualization.arrayToDataTable(donnee);

        var options = {
          title: 'Profit by state'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart2'));

        chart.draw(data, options);
      }
    </script>
    
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
          
       var donnee = [['Customer', 'Profit'],
          ['Jumbo Eagle Corp',${cli_1}],
          ['New Enterprises',${cli_2}],
          ['Small Bill Company',${cli_3}],
          ['Wren Computers',${cli_25}],
          ['Bob Hosting Corp.',${cli_36}],
          ['Early CentralComp',${cli_106}],
          ['John Valley Computers',${cli_149}],
          ['Old Media Productions',${cli_409}],
          ['Yankee Computer Repair Ltd',${cli_410}],
          ['Big Car Parts',${cli_722}],
          ['Zed Motor Co', ${cli_753}],
          ['West Valley Inc.',${cli_777}],
          ['Big Network Systems',${cli_863}]] ;

        var data = google.visualization.arrayToDataTable(donnee);

        var options = {
          title: 'Profit by customer'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart3'));

        chart.draw(data, options);
      }
    </script>

	</head>
	<body>
		<%--  On montre un éventuel message d'erreur --%>
		<div><h4>${message}</h4></div>
                
                
                <h1>Espace Admin</h1>
                             
                <form action="<c:url value="/"/>" method="POST"> 
			<input type='submit' name='action' value='Logout'>
		</form>
                        
                    <p>Veuillez choisir une période ou porterons les statistiques</p>
                    <p>Attention , aucune commande n'a été passé avant le 2011-05-24</p>
                        
              <form action="Admin" method="POST"> 
                    <%--  On sur linux avec firefox on ne peut sélectionner la date sur un calendrier, contrairement aux autres navigateurs. 
                    Dans ce cas la date est de type AAAA-MM-JJ --%>
                    
                    <label for="dateDebut">Date début:</label><input type="date" name="dateDebut" required pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}"> <br>
                    <label for="dateFin">Date fin:</label><input type="date" name="dateFin" required pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}"><br>
                    <input type='submit' name='action' value='SeeStats'>
                
		</form>     
                    
                    
             
                
            
                <div id="piechart1" style="width: 900px; height: 500px;"></div>
      
                <div id="piechart2" style="width: 900px; height: 500px;"></div>
     
                <div id="piechart3" style="width: 900px; height: 500px;"></div>
             
               
	</body>        
</html>