function changePassModal() {
    $('#modal').find('.modal-title').text('Change pass');
    $('#modal').find('.modal-body').load('/user/change-pass');
    $('#modal').modal()
}

function initSmartSelect(id, placeholder) {
    $(id).select2({
        placeholder: placeholder
    });
}

function initMessagesDataTable(admin) {
    var object = {
        "ajax": "/message/find-all",
        "sAjaxDataProp": "",
        "columns": [
            {"data": "id"},
            {"data": "from"},
            {"data": "to"},
            {"data": "createdDate"},
            {"data": "subject"}
        ],
        "columnDefs": [{
            "targets": admin ? 5 : 4,
            "data": null,
            "defaultContent": '<button id="btnView" class="btn btn-info" type="button">View</button>' + ' '
                            + '<button id="btnDelete" class="btn btn-danger"  type="button">Delete</button>'
        }]
    };

    if (!admin) {
        object.columns.splice(2, 1);
    }
    return $('#messages').DataTable(object);
}

function initMessageViewAction(table) {
    $('#messages').find('tbody').on('click', '#btnView', function (e) {
        var data = table.row($(this).parents('tr')).data();

        $('#modal').find('.modal-title').text('View message');
        $('#modal').find('.modal-body').load('/message/read/' + data.id);
        $('#modal').modal()
    });
}

function initMessageDeleteAction(table) {
    $('#messages').find('tbody').on('click', '#btnDelete', function (e) {
        var data = table.row($(this).parents('tr')).data();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        var self = $(this);
        $.ajax({
            url: '/message/delete/' + data.id,
            type: 'DELETE',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (result) {
                table.row(self.parents('tr')).remove().draw();
            }
        });
    });
}

function initMessagePage(admin) {
    initSmartSelect("#recipients", "Select a state");
    var table = initMessagesDataTable(admin);
    initMessageViewAction(table);
    initMessageDeleteAction(table);
}

function initUsersDataTable() {
    return $('#users').DataTable({
        "ajax": "/user/find-all",
        "sAjaxDataProp": "",
        "columns": [
            {"data": "id"},
            {"data": "firstName"},
            {"data": "lastName"},
            {"data": "email"},
            {"data": "login"},
            {
                "data": "authorities",
                render: function (data, type, row) {
                    var roles = '';
                    data.forEach(function (item, i) {
                        if (i > 0) {
                            roles += ', '
                        }
                        roles += item;
                    });
                    return roles;
                }
            }
        ],
        "columnDefs": [{
            "targets": 6,
            "data": null,
            "defaultContent": '<button id="btnView" class="btn btn-info" type="button">View</button>' + ' '
                            + '<button id="btnEdit" class="btn btn-success"  type="button">Edit</button>' + ' '
                            + '<button id="btnDelete" class="btn btn-danger"  type="button">Delete</button>'
        }]
    });
}

function initUserViewAction(table) {
    $('#users').find('tbody').on('click', '#btnView', function (e) {
        var data = table.row($(this).parents('tr')).data();

        $('#modal').find('.modal-title').text('View user');
        $('#modal').find('.modal-body').load('/user/read/' + data.id);
        $('#modal').modal();
    });
}

function initUserEditAction(table) {
    $('#users').find('tbody').on('click', '#btnEdit', function (e) {
        var data = table.row($(this).parents('tr')).data();

        $('#modal').find('.modal-title').text('Edit user');
        $('#modal').find('.modal-body').load('/user/update/' + data.id);
        $('#modal').modal();
    });
}

function initUserDeleteAction(table) {
    $('#users').find('tbody').on('click', '#btnDelete', function (e) {
        var data = table.row($(this).parents('tr')).data();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        var self = $(this);
        $.ajax({
            url: '/user/delete/' + data.id,
            type: 'DELETE',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (result) {
                if (result != "") {
                    showInfo('#errorMessage', result);
                } else {
                    table.row(self.parents('tr')).remove().draw();
                }
            }
        });
    });
}

function initUserPage() {
    var table = initUsersDataTable();
    initUserViewAction(table);
    initUserEditAction(table);
    initUserDeleteAction(table);
}

function showInfo(id, msg) {
    $(id).text(msg);
    $(id).show();
    setTimeout(function () {
        $(id).hide();
    }, 2000);
}