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
                    SubmitAddHarbor();
                }
                form.classList.add('was-validated');

            }, false);
        });
    }, false);
})();
var table = null;
var id =parseInt($("#harbor #id_account").val());
$(document).ready(function () {
    table = $('#dataTable').DataTable({
        "ajax": {
            "url": '/harbors/GetHarborUser/' + id,
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
                "data": "harbor_Name",
            },
            {
                "data": "location",
            },
            {
                "data": "description",
              
            },
            {
                "render": function (data, type, full, row) {
                    return `<a href="#" class="fas fa-pencil-alt text-warning mr-3" title="Borrow" onclick="DetailHarbor(${full.id})"></a>
                            <a href="#" class="fas fa-trash-alt mr-3 text-danger" title="Borrow"onClick="DeleteHarbor(${full.id})"></a>`
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

const DeleteHarbor = (harbor_id) => {
    Swal.fire({
        title: 'Apa Anda Yakin menghapus ? ',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Delete',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/harbors/Delete/' + harbor_id,
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

const DetailHarbor = (harbor_id) => {
    $("#harbor #SubmitInsert").hide()
    $("#harbor #SubmitUpdate").show()
    $.ajax({
        url: '/harbors/Get/' + harbor_id
    }).done(data => {
        console.log(data);
        $("#harbor #id").val(data.id)
        $("#harbor #name").val(data.harbor_Name)
        $("#harbor #location").val(data.location)
        $("#harbor #description").val(data.description)
        $("#harbor #phone").val(data.phone);
        $("#harbor #harbor_type").val(data.harbor_type);
        $("#harbor #harbor_activity").val(data.harbor_activity);
        $("#harbor").modal("show");
    });
}

function Update(lat, lng) {
    var harbor = new Object();
    harbor.id = $("#harbor #id").val();;
    harbor.harbor_Name = $("#harbor #name").val();
    harbor.description = $("#harbor #description").val();
    harbor.location = $("#harbor #location").val();
    harbor.phone = $("#harbor #phone").val();
    harbor.harbor_type = $("#harbor #harbor_type").val();
    harbor.harbor_activity = $("#harbor #harbor_activity").val();
    harbor.latitude = lat;
    harbor.longitude = lng;
    harbor.AccountId = parseInt($("#harbor #id_account").val());
    console.log(harbor);
    $.ajax({
        url: "/Harbors/UpdateHarbor/",
        data: harbor,
        type: 'PUT'
    }).then((result) => {
        console.log(result);
        if (result == 200) {
            Swal.fire(
                'Good job!',
                'Your data has been saved!',
                'success'
            )
            $("#account").modal("toggle");
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
function Add(lat, lng) {
    $("#harbor #SubmitUpdate").hide();
    var harbor = new Object();
    harbor.harbor_name = $("#harbor #name").val();
    harbor.location = $("#harbor #location").val();
    harbor.description = $("#harbor #description").val();
    harbor.phone = $("#harbor #phone").val();
    harbor.harbor_type = $("#harbor #harbor_type").val();
    harbor.harbor_activity = $("#harbor #harbor_activity").val();
    harbor.latitude = lat;
    harbor.longitude = lng;
    harbor.AccountId = parseInt($("#harbor #id_account").val());
    console.log(harbor);
    $.ajax({
        url: "/harbors/Post",
        data: harbor,
        type: 'POST'
    }).then((result) => {
        
        if (result == 200) {
            console.log(JSON.stringify(harbor));
            $('#harbor').modal('toggle');
            Swal.fire(
                'Good job!',
                'Your data has been saved!',
                'success'
            )

           
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

function SubmitUpdateHarbor() {
    var v_location = "";
    v_location = $("#harbor #location").val();
    $.ajax({
        url: 'https://maps.googleapis.com/maps/api/geocode/json?address=' + v_location + '&key=AIzaSyA_tnjz2VftxDKrPRVudR-p5bM4Tj_pvEI',
        type: 'get',
        success: function (data) {
            if (data.status === 'OK') {
                var lat = data.results[0].geometry.location.lat;
                var lng = data.results[0].geometry.location.lng;
                Update(lat, lng);

            }
        },
        error: function (msg) {
        }
    });
   
}
function ModalAddHarbor() {
    console.log("Martin");
    $("#harbor #SubmitUpdate").hide()

    $("#harbor").modal("show");

}

function SubmitAddHarbor() {
    var v_location = "";
    v_location = $("#harbor #location").val();
    $.ajax({
        url: 'https://maps.googleapis.com/maps/api/geocode/json?address=' + v_location + '&key=AIzaSyA_tnjz2VftxDKrPRVudR-p5bM4Tj_pvEI',
        type: 'get',
        success: function (data) {
            if (data.status === 'OK') {
                // Get the lat/lng from the response
                var lat = data.results[0].geometry.location.lat;
                var lng = data.results[0].geometry.location.lng;
                Add(lat, lng);

            }
        },
        error: function (msg) {
        }
    });
}
