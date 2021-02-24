function startQuery() {
    let c = window.setInterval("getMap()", 10000);
}

function getMap() {
    jQuery.ajax({
        type: "POST",
        url: "../log/ajax",
        dataType: "json",
        async: true,
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                const address = data[i].address;
                const method =data[i].method;
                const date = data[i].date;
                $('tbody').prepend('<tr>'+'<td>'+address+'</td>'+'<td>'+method+'</td>'+'<td>'+date+'</td>'+'</tr>')
            }
        }, error: function () {
            console.error();
        }
    });
}