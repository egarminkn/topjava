<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<link rel="stylesheet" href="webjars/datatables/1.10.12/css/dataTables.bootstrap.min.css">
<link rel="stylesheet" href="webjars/datetimepicker/2.4.7/jquery.datetimepicker.css">

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <h3><fmt:message key="meals.title"/></h3>

            <div class="view-box">
                <form method="post" class="form-horizontal" role="form" id="filter">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="startDate"><fmt:message key="meals.startDate"/>:</label>

                        <div class="col-sm-2">
                            <input class="form-control" type="text" name="startDate" id="startDate">
                        </div>

                        <label class="control-label col-sm-2" for="endDate"><fmt:message key="meals.endDate"/>:</label>

                        <div class="col-sm-2">
                            <input class="form-control" type="text" name="endDate" id="endDate">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="startTime"><fmt:message key="meals.startTime"/>:</label>

                        <div class="col-sm-2">
                            <input class="form-control" type="text" name="startTime" id="startTime">
                        </div>

                        <label class="control-label col-sm-2" for="endTime"><fmt:message key="meals.endTime"/>:</label>

                        <div class="col-sm-2">
                            <input class="form-control" type="text" name="endTime" id="endTime">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-8">
                            <button class="btn btn-primary pull-right"  type="button" onclick="updateTable()"><fmt:message key="meals.filter"/></button>
                        </div>
                    </div>
                </form>
                <a class="btn btn-sm btn-info" onclick="add('<fmt:message key="meals.add"/>')"><fmt:message key="meals.add"/></a>
                <table class="table table-striped display" id="datatable">
                    <thead>
                    <tr>
                        <th><fmt:message key="meals.dateTime"/></th>
                        <th><fmt:message key="meals.description"/></th>
                        <th><fmt:message key="meals.calories"/></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>

<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title" id="modalTitle"></h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" method="post" id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><fmt:message key="meals.dateTime"/></label>

                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="dateTime"
                                   name="dateTime" placeholder="<fmt:message key="meals.dateTime"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="control-label col-xs-3"><fmt:message
                                key="meals.description"/></label>

                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="description" name="description"
                                   placeholder="<fmt:message key="meals.description"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-3"><fmt:message key="meals.calories"/></label>

                        <div class="col-xs-9">
                            <input type="number" class="form-control" id="calories" name="calories"
                                   placeholder="1000">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button class="btn btn-primary" type="button" onclick="save()"><fmt:message key="common.save"/></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="webjars/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.3.8/js/noty/packaged/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="webjars/datetimepicker/2.4.7/build/jquery.datetimepicker.full.min.js"></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js"></script>
<script type="text/javascript">
    var ajaxUrl = 'ajax/profile/meals/';
    var datatableApi;

    function updateTable() {
        $.ajax({
            type: "POST",
            url: ajaxUrl + 'filter',
            data: $('#filter').serialize(),
            success: updateTableByData
        });
    }

    $(function () {
        $.datetimepicker.setLocale('ru');
        $('#startDate').datetimepicker({
            timepicker: false,
            format: 'Y-m-d',
            dayOfWeekStart: 1
        });
        $('#endDate').datetimepicker({
            timepicker: false,
            format: 'Y-m-d',
            dayOfWeekStart: 1
        });
        $('#startTime').datetimepicker({
            datepicker: false,
            format: 'H:i'
        });
        $('#endTime').datetimepicker({
            datepicker: false,
            format: 'H:i'
        });
        $('#dateTime').datetimepicker({
            format: 'Y-m-d H:i',
            dayOfWeekStart: 1
        });

        datatableApi = $('#datatable').DataTable({
            "ajax": {
                "url": ajaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": false,
            "searching": false,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function(data, type, row) {
                        if (type === 'display') {
                            return data.substr(0,16).replace("T", " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description",
                    "render": function(data, type, row) {
                        if (type === 'display') {
                            return data;
                        }
                        return data;
                    }
                },
                {
                    "data": "calories",
                    "render": function(data, type, row) {
                        if (type === 'display') {
                            return data;
                        }
                        return data;
                    }
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function(row_tag, row, dataIndex) {
                $(row_tag).addClass(row.exceed ? 'exceeded' : 'normal');
            },
            "initComplete": makeEditable
        });
    });
</script>
<script type="text/javascript">
    var edit_title = '<fmt:message key="meals.edit"/>';

    var i18n = [];
    <c:forEach var="key" items="${['common.failed', 'common.update', 'common.delete', 'common.deleted', 'common.saved']}">
        i18n['${key}'] = '<fmt:message key="${key}"/>';
    </c:forEach>
</script>
</html>