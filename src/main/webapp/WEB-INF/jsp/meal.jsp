<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<jsp:include page="fragments/headTag.jsp" />
<body>
<jsp:include page="fragments/bodyHeader.jsp" />
<section>
    <h2><a href=""><fmt:message key="app.home" /></a></h2>
    <h3>
        <c:choose>
            <c:when test="${param.action == 'create'}">
                <fmt:message key="meal.create" />
            </c:when>
            <c:otherwise>
                <fmt:message key="meal.edit" />
            </c:otherwise>
        </c:choose>
    </h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><fmt:message key="meal.datetime" />:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meal.description" />:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meal.calories" />:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit"><fmt:message key="meal.save" /></button>
        <button onclick="history.go(-1)"><fmt:message key="meal.cancel" /></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp" />
</body>
</html>
