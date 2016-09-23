<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://javawebinar.ru/formatter" %>
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
            <th width="100px" align="center">ID</th>
            <th width="200px" align="center">Дата-Время</th>
            <th width="200px" align="center">Описание</th>
            <th width="200px" align="center">Калории</th>
        </tr>
        <c:forEach items="${mealsWithExceeded}" var="mealWithExceeded">
            <c:set var="mealColor" value="${mealWithExceeded.exceed ? 'red' : 'green'}" />
            <tr style="color: <c:out value="${mealColor}" />" align="center">
                <td>
                    <c:out value="${mealWithExceeded.id}" />
                </td>
                <td>
                    <c:out value="${f:formatLocalDateTime(mealWithExceeded.dateTime)}" />
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
    <hr>
    <form method="post">
        <table>
            <tr>
                <td>
                    <label for="id">
                        ID
                    </label>
                </td>
                <td>
                    <input id="id" name="id" type="number" value="0" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="dateTime">
                        Дата-Время
                    </label>
                </td>
                <td>
                    <input id="dateTime" name="dateTime" type="datetime-local" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="description">
                        Описание
                    </label>
                </td>
                <td>
                    <input id="description" name="description" type="text" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="calories">
                        Калории
                    </label>
                </td>
                <td>
                    <input id="calories" name="calories" type="number" value="0" style="width: 200px;">
                </td>
            </tr>
            <tr align="center">
                <td colspan="2">
                    <input type="submit" value="Послать">
                </td>
            </tr>
        </table>
    </form>
</body>
</html>