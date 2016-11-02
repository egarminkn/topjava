function inverseEnable() {
    var id = this.closest('tr').attr("id");
    var enabled = this.is(":checked");
    $.ajax({
        url: ajaxUrl + id + '?enabled=' + enabled,
        type: 'PUT',
        //data: 'enabled=' + enabled, // так нельзя, т.е. параметры метода PUT отправляются как и для GET в адресной строке
        success: function() {
            updateTable();
            successNoty('Updated');
        }
    });
}

function makeEditable() {
    $('.delete').click(function () {
        deleteRow($(this).closest('tr').attr("id"));
    });

    $('#detailsForm').submit(function () {
        save();
        return false;
    });

    $('#datatable').find('input[type="checkbox"]').change(function(event) {
        event.preventDefault();
        inverseEnable.call($(this));
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(event, jqXHR, options, jsExc);
    });
}

function add() {
    $('#id').val(null);
    $('#editRow').modal();
}

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: 'DELETE',
        success: function () {
            updateTable();
            successNoty('Deleted');
        }
    });
}

function updateTable() {
    $.get(ajaxUrl, function (data) {
        datatableApi.fnClearTable();
        $.each(data, function (key, item) {
            datatableApi.fnAddData(item);
        });
        datatableApi.fnDraw();
    });
}

function save() {
    var form = $('#detailsForm');
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $('#editRow').modal('hide');
            updateTable();
            successNoty('Saved');
        }
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    noty({
        text: text,
        type: 'success',
        layout: 'bottomRight',
        timeout: 1000
    });
}

function failNoty(event, jqXHR, options, jsExc) {
    closeNoty();
    failedNote = noty({
        text: 'Failed: ' + jqXHR.statusText,
        type: 'error',
        layout: 'bottomRight'
    });
}
