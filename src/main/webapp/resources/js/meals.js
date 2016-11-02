var ajaxUrl = "ajax/profile/meals";
var dataTableApi;
var columnsInfo = [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description",
                        "orderable": false
                    },
                    {
                        "data": "calories",
                        "orderable": false
                    },
                    {
                        "data": "updateLink",
                        "defaultContent": "Update",
                        "orderable": false
                    },
                    {
                        "data": "deleteLink",
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ];

$(function () {
    dataTableApi = $('table').DataTable({
        "paging": false,
        "info": false,
        "searching": false,
        "columns": columnsInfo,
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    createEventHandlers();
});

function createEventHandlers() {
    createClickEventHandlersForAddMeal();
    createClickEventHandlerForUpdateMeal();
    createClickEventHandlerForDeleteMeal();
    createClickEventHandlerForFilter();
}

function createClickEventHandlerForUpdateMeal() {
    $('[data-update]').click(function(event) {
        event.preventDefault();

        var row = $(this).parents('tr');

        var id = row.attr("id");
        $('#mealRow').find('input[name="id"]').val(id);

        var dateTime = row.find('[id^="dateTime"]').text();
        dateTime = dateTime.split(" ").join("T");
        $('#mealRow').find('input[name="dateTime"]').val(dateTime);

        $('#mealRow').find('input[name="description"]').val(row.find('[id^="description"]').text());
        $('#mealRow').find('input[name="calories"]').val(row.find('[id^="calories"]').text());

        $('#addTitle').css("display", "none");
        $('#updateTitle').css("display", "block");

        $('#mealRow form').off('.mealForm').on('submit.mealForm', function(event) {
            sendJson($(this), id);
            return false; // это альтернатива event.preventDefault(), но только для события submit (для click return false писать нельзя)
        });

        $('#mealRow').modal();
    });
}

function createClickEventHandlersForAddMeal() {
    $('[data-add]').click(function(event) {
        /**
         * Наш обработчик ложится в стек обработчиков события click на ссылке
         * Строка event.preventDefault() позволяет остановить распространение события на ранее подключенные обработчики,
         * в том числе и на самый корневой, т.е. перехода по ссылке не будет, а будет только показ модального окна
         */
        event.preventDefault();

        $('#mealRow').find('input[name="id"]').val(null);
        $('#mealRow').find('input[name="dateTime"]').val(now());
        $('#mealRow').find('input[name="description"]').val("");
        $('#mealRow').find('input[name="calories"]').val(1000);

        $('#addTitle').css("display", "block");
        $('#updateTitle').css("display", "none");

        $('#mealRow form').off('.mealForm').on('submit.mealForm', function(event) {
            sendJson($(this));
            return false; // это альтернатива event.preventDefault(), но только для события submit (для click return false писать нельзя)
        });

        $('#mealRow').modal();
    });
}

function createClickEventHandlerForFilter() {
    $('#filterForm').submit(function() {
        updateTable();
        return false;
    });
}

function createClickEventHandlerForDeleteMeal() {
    $('[data-delete]').click(function(event) {
        event.preventDefault();
        var id = $(this).parents('tr').attr("id");
        var url = ajaxUrl + "/" + id;
        $.ajax({
            url: url,
            type: 'DELETE',
            success: function(responseBody, type, response) {
                updateTable();
                notify(responseBody, type, response, url);
            },
            error: function(response, type, responseBody) {
                notify(responseBody, type, response, url);
            }
        });
    });
}

function sendJson(jQueryObject, id) {
    var json = {};
    jQueryObject.serializeArray().map(function(item) {
        json[item.name] = item.value;
    });
    var url = ajaxUrl + (id ? "/" + id : "");
    var method = id ? 'PUT' : 'POST';
    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify(json),
        success: function(responseBody, type, response) {
            updateTable();
            notify(responseBody, type, response, url);
            $('#mealRow').modal('hide');
        },
        error: function(response, type, responseBody) {
            notify(responseBody, type, response, url);
        }
    });
}

function notify(responseBody, type, response, url) {
    noty({
        text: 'Status code: ' + response.status + "<br/>Url: " + url,
        type: type,
        layout: 'bottomRight',
        timeout: 1000
    });
}

function now() {
    function to2digits(number) {
        var length = number.toString().length;
        return ("00" + number).slice(length);
    }

    var dateTime = new Date();
    var year = dateTime.getFullYear();
    var month = to2digits(dateTime.getMonth() + 1);
    var day = to2digits(dateTime.getDate());
    var hours = to2digits(dateTime.getHours());
    var minutes = to2digits(dateTime.getMinutes());

    return year + "-" + month + "-" + day + "T" + hours + ":" + minutes;
}

function updateTable() {
    var params = {
        startDate: $('#startDate').val(),
        startTime: $('#startTime').val(),
        endDate:   $('#endDate').val(),
        endTime:   $('#endTime').val()
    };
    var url = ajaxUrl + "/between?" + $.param(params);
    $.get(url, function(data) {
        // один способо
        // dataTableApi.clear();
        // dataTableApi.rows.add(data);
        // dataTableApi.draw();

        // второй способ - конвеер
        //dataTableApi.clear().rows.add(data).draw();

        // третий способ - с сохранением стилей
        dataTableApi.clear();
        $.each(data, function(i, json) {
            json.updateLink = '<a href="meals/update?id=' + json.id + '" class="btn btn-xs btn-primary" data-update>' + updateLinkText + '</a>';
            json.deleteLink = '<a href="meals/delete?id=' + json.id + '" class="btn btn-xs btn-danger" data-delete>' + deleteLinkText + '</a>';

            json.dateTime = json.dateTime.split("T").join(" ").slice(0, 16);

            var row = dataTableApi.row.add(json);
            var tr = row.node();
            $(tr).addClass(json.exceed ? 'exceeded' : 'normal');
            $(tr).attr("id", json.id);
            $.each($(tr).find('td'), function(i, td) {
                var tdId = columnsInfo[i].data + "_" + json.id;
                $(td).attr("id", tdId);
            });
        });
        dataTableApi.draw();
        createClickEventHandlerForUpdateMeal();
        createClickEventHandlerForDeleteMeal();
    });
}