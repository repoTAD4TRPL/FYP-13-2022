$.ajax({
    url: '/harbors/getAll'
}).done(res => {
    let htmlContent = "";
    console.log(res)

$.each(res, (key, value) => {
    console.log(key);
    htmlContent +=
        `<div class="col-xl-6 col-md-6 mt-5">
                            <div class="card border-left-primary shadow h-100">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center mb-2">
                                        <div class="col mr-2">
                                            <div class="h5 mb-0 font-weight-bold text-gray-800">${value.harbor_Name}</div>
                                        </div>
                                    </div>
                                    <div></div>
                                </div>
                            </div>
                        </div>`
});

    $('#card_harbor').html(htmlContent)
})