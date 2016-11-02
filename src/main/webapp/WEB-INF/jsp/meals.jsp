<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<link rel="stylesheet" href="webjars/datatables/1.10.12/css/jquery.dataTables.min.css"> <!-- Подключение сразу даст красивые стрелочки сортировки колонок-->

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section class="jumbotron"> <!-- bootstrap - серый фон -->
    <div class="container"> <!-- bootstrap - центровка -->
        <h3><fmt:message key="meals.title"/></h3>

        <form class="form-horizontal" method="post" action="meals/filter" id="filterForm">
            <div class="form-group">
                <label for="startDate" class="control-label col-xs-3">
                    <fmt:message key="meals.startDate"/>:
                </label>
                <div class="col-xs-9">
                    <input class="form-control" type="date" name="startDate" id="startDate" value="${param.startDate}">
                </div>
            </div>
            <div class="form-group">
                <label for="endDate" class="control-label col-xs-3">
                    <fmt:message key="meals.endDate"/>:
                </label>
                <div class="col-xs-9">
                    <input class="form-control" type="date" name="endDate" id="endDate" value="${param.endDate}">
                </div>
            </div>
            <div class="form-group">
                <label for="startTime" class="control-label col-xs-3">
                    <fmt:message key="meals.startTime"/>:
                </label>
                <div class="col-xs-9">
                    <input class="form-control" type="time" name="startTime" id="startTime" value="${param.startTime}">
                </div>
            </div>
            <div class="form-group">
                <label for="endTime" class="control-label col-xs-3">
                    <fmt:message key="meals.endTime"/>:
                </label>
                <div class="col-xs-9">
                    <input class="form-control" type="time" name="endTime" id="endTime" value="${param.endTime}">
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-offset-3 col-xs-9">
                    <button type="submit" class="btn btn-xs btn-primary"><fmt:message key="meals.filter"/></button>
                    <!--
                        btn         - bootstrap общий стиль для кнопок
                        btn-xs      - bootstrap узкая кнопка
                        btn-primary - bootstrap синий цвет для кнопки
                    -->
                </div>
            </div>
        </form>
        <hr>
        <a href="meals/create" class="btn btn-xs btn-info" data-add><fmt:message key="meals.add"/></a>
        <!--
            btn      - bootstrap общий стиль для кнопок
            btn-xs   - bootstrap узкая кнопка
            btn-info - bootstrap голубой цвет для кнопки
        -->
        <hr>
        <table class="table table-striped display">
            <!--
                table         - bootstrap между строками проводит линии
                table-striped - bootstrap делает нечетные строки серыми
                display       - datatable затемняет колонку по которой идет сортировка
            -->
            <thead>
            <tr>
                <th><fmt:message key="meals.dateTime"/></th>
                <th><fmt:message key="meals.description"/></th>
                <th><fmt:message key="meals.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${meals}" var="mealWithExceed">
                <jsp:useBean id="mealWithExceed" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                <tr class="${mealWithExceed.exceed ? 'exceeded' : 'normal'}" id="${mealWithExceed.id}">
                    <td id="dateTime_${mealWithExceed.id}">
                        ${fn:formatDateTime(mealWithExceed.dateTime)}
                    </td>
                    <td id="description_${mealWithExceed.id}">
                        ${mealWithExceed.description}
                    </td>
                    <td id="calories_${mealWithExceed.id}">
                        ${mealWithExceed.calories}
                    </td>
                    <td>
                        <a href="meals/update?id=${mealWithExceed.id}" class="btn btn-xs btn-primary" data-update><fmt:message key="common.update"/></a>
                    </td>
                    <td>
                        <a href="meals/delete?id=${mealWithExceed.id}" class="btn btn-xs btn-danger" data-delete><fmt:message key="common.delete"/></a>
                    </td>
                    <!--
                        btn         - bootstrap общий стиль для кнопок
                        btn-xs      - bootstrap узкая кнопка
                        btn-primary - bootstrap синий цвет для кнопки
                        btn-danger  - bootstrap красный цвет для кнопки
                    -->
                </tr>
            </c:forEach>
        </table>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>

<section class="modal fade" id="mealRow">
    <!--
        modal - bootstrap скрыть со страницы
        fade  - bootstrap делает показ/закрытие модального окна плавным
    -->
    <div class="modal-dialog">
        <!--
            modal-dialog - bootstrap центрирование модального окна
        -->
        <div class="modal-content">
            <!--
                modal-content - bootstrap делает содержимое модального окна непрозрачным
            -->
            <div class="modal-header">
                <!--
                    modal-header - bootstrap отчеркивает заголовок
                -->
                <button type="button" class="close" data-dismiss="modal">
                    <!--
                        close                - bootstrap делает кнопку не похожей на кнопку
                        data-dismiss="modal" - bootstrap добавляем функционал закрытия модального окна по нажатию
                    -->
                    &times;
                </button>
                <h2 class="modal-title" id="addTitle">
                    <!--
                        modal-title - bootstrap делает заголовок более узким
                    -->
                    <fmt:message key="meals.add"/>
                </h2>
                <h2 class="modal-title" id="updateTitle">
                    <!--
                        modal-title - bootstrap делает заголовок более узким
                    -->
                    <fmt:message key="meals.edit"/>
                </h2>
            </div>
            <div class="modal-body">
                <!--
                    modal-body - bootstrap делает отступы по краям
                -->
                <form class="form-horizontal" method="post" action="meals">
                    <input type="hidden" name="id">

                    <div class="form-group">
                        <!--
                            form-group - bootstrap делает каждое поле формы на своей строке
                        -->
                        <label class="control-label col-xs-3" for="dateTime">
                            <!--
                                form-horizontal control-label - bootstrap делают совместно выравнивание label по правому краю колонки label-ов
                                col-xs-3                      - bootstrap делает красивые отступы у label
                            -->
                            <fmt:message key="meals.dateTime"/>:
                        </label>
                        <div class="col-xs-9">
                            <input class="form-control" type="datetime-local" name="dateTime" id="dateTime">
                            <!--
                                form-control - bootstrap растягивает input на всю ширину
                            -->
                        </div>
                    </div>

                    <div class="form-group">
                        <!--
                            form-group - bootstrap делает каждое поле формы на своей строке
                        -->
                        <label class="control-label col-xs-3" for="description">
                            <!--
                                form-horizontal control-label - bootstrap делают совместно выравнивание label по правому краю колонки label-ов
                                col-xs-3                      - bootstrap делает красивые отступы у label
                            -->
                            <fmt:message key="meals.description"/>:
                        </label>
                        <div class="col-xs-9">
                            <input class="form-control" type="text" size=40 name="description" id="description">
                            <!--
                                form-control - bootstrap растягивает input на всю ширину
                            -->
                        </div>
                    </div>

                    <div class="form-group">
                        <!--
                            form-group - bootstrap делает каждое поле формы на своей строке
                        -->
                        <label class="control-label col-xs-3" for="calories">
                            <!--
                                form-horizontal control-label - bootstrap делают совместно выравнивание label по правому краю колонки label-ов
                                col-xs-3                      - bootstrap делает красивые отступы у label
                            -->
                            <fmt:message key="meals.calories"/>:
                        </label>
                        <div class="col-xs-9">
                            <input class="form-control" type="number" name="calories" id="calories">
                            <!--
                                form-control - bootstrap растягивает input на всю ширину
                            -->
                        </div>
                    </div>

                    <div class="form-group">
                        <!--
                            form-group - bootstrap делает каждое поле формы на своей строке
                        -->
                        <div class="col-xs-offset-3 col-xs-9">
                            <!--
                                col-xs-offset-3 col-xs-9 - bootstrap совместно выравнивают кнопку под input-ами
                            -->
                            <button type="submit" class="btn btn-primary">
                                <!--
                                    btn         - bootstrap общий стиль для кнопок
                                    btn-primary - bootstrap синий цвет для кнопки
                                -->
                                <fmt:message key="common.save"/>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script> <!-- для показа модального окна -->
<script type="text/javascript" src="webjars/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.3.8/js/noty/packaged/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="resources/js/meals.js"></script>
<script type="text/javascript">
    var updateLinkText = '<fmt:message key="common.update"/>';
    var deleteLinkText = '<fmt:message key="common.delete"/>';
</script>
</html>
