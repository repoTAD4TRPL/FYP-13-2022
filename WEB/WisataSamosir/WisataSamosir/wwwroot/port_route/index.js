(function () {
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

function ModalAddRoute() {
    $("#route #SubmitUpdate").hide()

    $("#route").modal("show");
}


const Delete = (portRoute_id) => {
    Swal.fire({
        title: 'Apa Anda Yakin menghapus ? ',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Delete',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/PortRoutes/Delete/' + portRoute_id,
                method: 'DELETE',
                success: function (data) {
                    Swal.fire('Berhasil Dihapus', '', 'success')
                    location.reload()
                }
            });
        } else if (result.isDenied) {
            Swal.fire('Data Tidak Jadi Dihapus', '', 'info')
        }
    })
}

function SubmitAddHarbor() {
    Add();
}

function Add() {
    $("#route #SubmitUpdate").hide();
    var port_route = new Object();
    port_route.Harbor_Start = $("#route #harbor_start").val();
    port_route.Harbor_End = $("#route #harbor_end").val();
    port_route.Description = $("#route #description").val();
    console.log(port_route);
    $.ajax({
        url: "/PortRoutes/addPortRoute",
        data: port_route,
        type: 'POST'
    }).then((result) => {
        console.log(result);
        if (result == 200) {
            Swal.fire(
                'Good job!',
                'Your data has been saved!',
                'success'
            )
            $("#myModal").modal("toggle");
           location.reload();
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