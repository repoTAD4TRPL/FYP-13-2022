(function () {
    console.log("Berhasil");
    'use strict';
    window.addEventListener('load', function () {
        var forms = document.getElementsByClassName('needs-validation');
        var validation = Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();

                }
                if (form.checkValidity() === true) {
                    event.preventDefault();
                    Add();
                }
                form.classList.add('was-validated');

            }, false);
        });
    }, false);
})();

var table = null;
var data = $("#id_route").val();
console.log(data);
$.ajax({
    "url": '/Schedules/GetScedule/' +data,
        }).done(res => {
        let htmlContent = "";
        console.log(res)
        htmlContent +=
            `<h2>${res[0].routeName}</h2>`
        $('#RouteName').html(htmlContent)
    })

 

$(document).ready(function () {
    table = $('#dataTable').DataTable({
        "ajax": {
            "url": '/Schedules/GetScedule/' +data,
    "datatype": "json",
        "dataSrc": ""
},
    "columns": [
    {
        render: function (data, type, row, meta) {
            return meta.row + meta.settings._iDisplayStart + 1;
        }
    },
    {
        "data": "session",
    },
    {
        "data": "time",
        render: function (data, type, row) {
            return data.substr(0, 5);
        }
    },
    {
        "render": function (data, type, full, row) {
            return `<a href="#" class="fas fa-trash-alt mr-3 text-danger" title="Borrow"onClick="DeleteSession(${full.id})"></a>`
        }
    }
],
    'columnDefs': [{
        'targets': [0, 2],
        'orderable': false,
    }]

           });
table.on('order.dt search.dt', function () {
    table.column(0, { search: 'applied', order: 'applied' }).nodes().each(function (cell, i) {
        cell.innerHTML = i + 1;
    });
}).draw();

});

function ModalAddSession() {
    $("#session #SubmitUpdate").hide()
    $("#session").modal("show");
}
function SubmitAddSession() {
    Add();
}
function Add() {
    $("#session #SubmitUpdate").hide();
    var session = new Object();
    session.Session= $("#session #session").val();
    session.Time = $("#session #timepicker").val();
    session.PortRoute_id = $("#id_route").val();
    console.log(session);
    $.ajax({
        url: "https://localhost:44329/Schedules/AddSchedule",
        data: session,
        type: 'POST'
    }).then((result) => {
        console.log(result);
        if (result == 200) {
            Swal.fire(
                'Good job!',
                'Your data has been saved!',
                'success'
            )
            $("#myModal").modal("toggle").hide();
            table.ajax.reload();
        } else if (result == 400) {
            Swal.fire(
                'Watch Out!',
                'Duplicate Data!',
                'error'
            )
        }
    }).catch((error) => {
        console.log(error);
    })
}

const DeleteSession = (session_id) => {
    Swal.fire({
        title: 'Apa Anda Yakin menghapus ? ',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Delete',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/Schedules/DeleteSession/' + session_id,
                method: 'DELETE',
                success: function (data) {
                    Swal.fire('Berhasil Dihapus', '', 'success')
                    table.ajax.reload()
                }
            });
        } else if (result.isDenied) {
            Swal.fire('Data Tidak Jadi Dihapus', '', 'info')
        }
    })
}

const DetailSession = (session_id) => {
    $.ajax({
        url: '/accounts/GetDetail/' + session_id
    }).done(data => {
        console.log(data.role);
        $("#account #SubmitInsert").hide()
        $("#account #id").val(data.id)
        $("#account #username").val(data.username)
        $("#account #email").val(data.email)
        $("#account #password").val(data.password).hide();
        if (data.role == "ADMIN") {
            childr = 2;
        } else if (data.role == "SUPERADMIN") {
            childr = 3;
        }
        $(`#account #role :nth-child(${childr})`).prop('selected', true);
        if (data.status == "ACTIVE") {
            child = 2;
        } else if (data.status == "EXPIRED") {
            child = 3;
        }
        console.log(childr)
        $(`#account #status :nth-child(${child})`).prop('selected', true);
        $("#account").modal("show");
    });
}