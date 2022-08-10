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
                    SubmitAddTour();
                }
                form.classList.add('was-validated');

            }, false);
        });
    }, false);
})();
var table = null;
$(document).ready(function () {
    table = $('#dataTable').DataTable({
        "ajax": {
            "url": '/touristattractions/GetTouristAttraction',
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
                "data": "name",
            },
            {
                "data": "location",
                render: function (data, type, row) {
                    if (data.length >20) {
                        return data.substr(0, 20) + '  ...';
                    }
                    return data;
                }
            },
            {
                "data": "description",
                render: function (data, type, row) {
                    if (data.length >150) {
                        return data.substr(0, 150) + '  ...';
                    }
                    return data;x
                }
                
            },
            {
                "data": "category",
                render: function (data, type, row) {
                    if (data == 1) {
                        return "Natural Tourism";
                    } else {
                        return "Social Culture";
                    }
                }
            },
            {
                "render": function (data, type, full, row) {
                    return `<a href="#" class="fas fa-pencil-alt text-warning mr-3" title="Borrow" onclick="DetailTour(${full.id})"></a>
                            <a href="#" class="fas fa-trash-alt mr-3 text-danger" title="Borrow"onClick="DeleteTour(${full.id})"></a>`
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
$(document).ready(function () {
    table = $('#dataTable_food').DataTable({
        "ajax": {
            "url": '/touristattractions/GetFoodDestination',
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
                "data": "name",
            },
            {
                "data": "location",
                render: function (data, type, row) {
                    if (data.length > 20) {
                        return data.substr(0, 20) + '  ...';
                    }
                    return data;
                }
            },
            {
                "data": "description",
                render: function (data, type, row) {
                    if (data.length > 150) {
                        return data.substr(0, 150) + '  ...';
                    }
                    return data; x
                }

            },
            {
                "data": "category",
                render: function (data, type, row) {
                    if (data == 1) {
                        return "Natural Tourism";
                    } else {
                        return "Social Culture";
                    }
                }
            },
            {
                "render": function (data, type, full, row) {
                    return `<a href="#" class="fas fa-pencil-alt text-warning mr-3" title="Borrow" onclick="DetailTour(${full.id})"></a>
                            <a href="#" class="fas fa-trash-alt mr-3 text-danger" title="Borrow"onClick="DeleteTour(${full.id})"></a>`
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



const DetailTour = (tour_id) => {
    $("#tour #SubmitInsert").hide() 
    $("#tour #SubmitUpdate").show()
    console.log("Berhasil");
    $.ajax({
        url: '/touristattractions/Get/' + tour_id
    }).done(data => {
        console.log(data);
        $("#tour #id").val(data.id);
        $("#tour #location").val(data.location);
        $("#tour #name").val(data.name);
        $("#tour #description").val(data.description);
        if (data.category == 1) {
            childr = 3;
        } else if (data.category == 0) {
            childr = 2;
        }
        console.log(childr)
        $(`#tour #category :nth-child(${childr})`).prop('selected', true);
        $("#tour").modal("show");
    });
}
const DeleteTour = (id) => {
    Swal.fire({
        title: 'Apa Anda Yakin menghapus ? ',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Delete',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/touristattractions/Delete/' + id,
                method: 'DELETE',
                success: function (data) {
                    Swal.fire('Berhasil Dihapus', '', 'success')
                    window.location.reload()
                }
            });
        } else if (result.isDenied) {
            Swal.fire('Data Tidak Jadi Dihapus', '', 'info')
        }
    })
}

function AddTour() {
    $("#tour #SubmitUpdate").hide()
    $("#tour").modal("show");

}
function SubmitUpdateAccount() {
    var v_location = $("#tour #location").val();
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


function SubmitAddTour() {
    var v_location = "";
    v_location = $("#tour #location").val();
    console.log(v_location);
    $.ajax({
        url: 'https://maps.googleapis.com/maps/api/geocode/json?address=' + v_location + '&key=AIzaSyA_tnjz2VftxDKrPRVudR-p5bM4Tj_pvEI',
        type: 'get',
        success: function (data) {
           
            if (data.status === 'OK') {
                console.log("Berhasil kali")
                var lat = data.results[0].geometry.location.lat;
                var lng = data.results[0].geometry.location.lng;
                Add(lat, lng);

            }
        },
        error: function (msg) {
        }
    });
}
function Update(lat , lng) {
    $("#account #SubmitUpdate").show();
    var tour = new Object();
    tour.id = $("#tour #id").val();
    tour.accountId = parseInt($("#tour #id_account").val());
    tour.location = $("#tour #location").val();
    tour.name = $("#tour #name").val();
    tour.description = $("#tour #description").val();
    tour.latitude = lat;
    tour.longitude = lng;
    tour.category = $("#tour #category").val();
    $.ajax({
        url: "/touristattractions/UpdateTouristAttractions",
        data: tour,
         type : 'PUT'
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
    $("#tour #SubmitUpdate").hide();
    var tour = new Object();
    tour.location = $("#tour #location").val();
    tour.accountId = parseInt($("#tour #id_account").val());
    tour.name = $("#tour #name").val();
    tour.description = $("#tour #description").val();
    tour.latitude = lat;
    tour.longitude = lng;
    tour.categoryId = parseInt($("#tour #category").val());
    console.log(tour);
    $.ajax({
        url: "/touristattractions/PostAdd",
        data: tour,
        type: 'POST'
    }).then((result) => {
        console.log(result);
        if (result == 200) {
            Swal.fire(
                'Good job!',
                'Your data has been saved!',
                'success'
            )
            $("#tour").hide();
            window.location.reload()
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


