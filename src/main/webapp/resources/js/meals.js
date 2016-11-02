var ajaxUrl = "ajax/profile/meals";

$(document).ready(function () {
    $('table').dataTable({
        "paging": false,
        "info": false,
        "searching": false,
        "columns": [
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
                "defaultContent": "Update",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
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

function createClickEventHandlerForDeleteMeal() {
    $('[data-delete]').click(function(event) {
        event.preventDefault();
        var id = $(this).parents('tr').attr("id");
        $.ajax({
            url: ajaxUrl + "/" + id,
            type: 'DELETE',
            success: function(responseBody, type, response) {
                updateTable();
                notify(responseBody, type, response);
            },
            error: function(response, type, responseBody) {
                notify(responseBody, type, response);
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
            notify(responseBody, type, response);
            $('#mealRow').modal('hide');
        },
        error: function(response, type, responseBody) {
            notify(responseBody, type, response);
        }
    });
}

function notify(responseBody, type, response) {
    noty({
        text: 'Status code: ' + response.status,
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

}