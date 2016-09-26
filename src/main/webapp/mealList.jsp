<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>Meal list</h3>
    <a href="meals?action=create&userId=${userId}">Add Meal</a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${mealList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}&userId=${userId}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}&userId=${userId}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
    <hr>
    <form method="get" action="meals">
        <input type="hidden" name="userId" value="${userId}" />
        <table>
            <tr>
                <td>
                    <label for="startDate">
                        Start date:
                    </label>
                    <input type="date" id="startDate" name="startDate" value="${param.startDate}"/>
                </td>
                <td>
                    <label for="endDate">
                        End date:
                    </label>
                    <input type="date" id="endDate" name="endDate" value="${param.endDate}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="startTime">
                        Start time:
                    </label>
                    <input type="time" id="startTime" name="startTime" value="${param.startTime}"/>
                </td>
                <td>
                    <label for="endTime">
                        End time:
                    </label>
                    <input type="time" id="endTime" name="endTime" value="${param.endTime}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Отфильтровать" />
                </td>
            </tr>
        </table>
    </form>
</section>
</body>
</html>