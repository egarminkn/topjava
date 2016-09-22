<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meal list</title>
    <meta charset="utf-8">
</head>
<body>
    <h2><a href="index.html">Home</a></h2>
    <h2>Meal list</h2>
    <hr>
    <table>
        <tr>
            <th width="200px" align="center">Дата-Время</th>
            <th width="200px" align="center">Описание</th>
            <th width="200px" align="center">Калории</th>
        </tr>
        <c:forEach items="${mealsWithExceeded}" var="mealWithExceeded">
            <c:set var="mealColor" value="${mealWithExceeded.exceed ? 'red' : 'green'}" />
            <tr style="color: <c:out value="${mealColor}" />" align="center">
                <td>
                    <c:out value="${mealWithExceeded.formattedDateTime}" />
                </td>
                <td>
                    <c:out value="${mealWithExceeded.description}" />
                </td>
                <td>
                    <c:out value="${mealWithExceeded.calories}" />
                </td>
            </tr>    
        </c:forEach>
    </table>
</body>
</html>