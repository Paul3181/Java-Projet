<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <link href="css/customer.css" rel="stylesheet">
		<title>Espace client</title>
	</head>
	<body>
            
            <div id="customer">
                <h3>${userName}</h3>
                <form action="<c:url value="/"/>" method="POST"> 
			<input type='submit' id="logout" name='action' value='Logout'>
		</form>
            </div>
             
            <div id="main">
                <article>
                <div>
                    <form>
                        Categories : <select name='category' onchange='this.form.submit()'>
                            <c:forEach var="category" items="${existingCategories}">
                                <option value='${category}'
                                        <c:if test='${category eq selectedCategory}'> selected </c:if>                                
                                        >${category}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
                    
                <br>
                
                <div><h4>${message2}</h4></div>
                <div><h4>${message3}</h4></div>
                
                <%-- On montre la liste des produits --%>
                <div id="tableau">
                    <table>
			<tr><th>Products</th><th>Price</th><th>Available Quantity</th><th>Discount Rate</th><th></th></tr>
                            <c:forEach var="record" items="${products}" >
                            <tr>
                                <td>${record.product}</td>
                                <td><fmt:formatNumber value="${record.price}"/></td>
                                <td><fmt:formatNumber value="${record.qte}"/></td>
                                <td><fmt:formatNumber value="${record.rate}"/></td>
                                <td>
                                    <form method="GET" id="SubmitForm">
                                        <input type="number" id="qte" name="quantity1" step="1" min="1" max="99999">
                                        <input type="hidden" name="product1" value="${record.product}"</input>
                                        <input type="hidden" name="price1" value="${record.price}"</input>
                                        <input type="hidden" name="qte1" value="${record.qte}"</input>
                                        <input type="hidden" name="id1" value="${record.id}"</input>
                                        <input type="submit" id="addButton" name="action" value="Add"</input>
                                    </form>
                                </td>
                            </tr>	
                            </c:forEach>
                    </table>
		</div>
                </article>
                    
                <article id="orders">
                    <h3>My orders </h3>
                    <%--  On montre un éventuel message d'erreur --%>
                    <div><h4>${message}</h4></div>
                    <div><h4>${message4}</h4></div>
                    <table>
                        <tr><th>N°</th><th>Product</th><th>Price</th><th>Quantity</th><th>Shipping Cost</th><th>Sales date</th><th>Shipping date</th><th>Company</th><th></th></tr>
                            <c:forEach var="record" items="${purchase}">
                                <tr>
                                    <td>${record.numP}</td>
                                    <td>${record.productP}</td>
                                    <td>${record.priceP}</td>
                                    <td>${record.qteP}</td>
                                    <td>${record.costP}</td>
                                    <td>${record.salesP}</td>
                                    <td>${record.shippingP}</td>
                                    <td>${record.companyP}</td>
                                    <td>
                                        <form method="GET">
                                            <input type="number" name="qte3" step="1" min="1" max="99999">
                                            <input type="hidden" name="numD" value="${record.numP}"</input>
                                            <input type="submit" name="action" value="Modify"</input>
                                            <input type="submit" name="action" value="Delete"</input>
                                        </form>
                                    </td>
                                </tr>	
                            </c:forEach>
                    </table>
                </article>
            </div>
                
        <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script> 
        <script>
            $("#tableau td:nth-child(3)").each(function () {
                if ($(this).html()==0){
                        $(this).html('<span id="erreur">Quantity not available</span>');
                }
        });
        
        </script>
	</body>        
</html>
