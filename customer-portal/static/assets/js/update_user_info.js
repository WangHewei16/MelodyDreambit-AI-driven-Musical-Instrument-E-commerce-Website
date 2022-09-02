$(document).ready(function () {


    $("#setting_submit_btn").on("click", function () {
            // nick_name = $("#setting_nick_name").val()
            country = $("#setting_country").val()
            address1 = $("#setting_address1").val()
            address2 = $("#setting_address2").val()
            first_name = $("#setting_first_name").val()
            last_name = $("#setting_last_name").val()
            phone_number = $("#setting_phone_number").val()
        if (country === "" || address1 === "" || address2 === "" || first_name === "" ||
            last_name === "" || phone_number === "") {
            warningFill();
        } else {
            zlajax.post({
                url: "/add_setting",
                data: {
                    // "nick_name": nick_name,
                    "type":"add setting",
                    "country": country,
                    "address1": address1,
                    "address2": address2,
                    "phone": phone_number,
                    "first_name": first_name,
                    "last_name": last_name
                },
                success: function (result) {
                    if (result['code'] === 200) {
                        success();

                        let nation_name = result['nation_name'];
                        let address_id = result['address-id'];

                        let tbody = $("#history-address-tbody");
                        let tr = $("<tr>");
                        tbody.append(tr);
                        let td1 = $("<td>");
                        td1.html(first_name + " " + last_name);
                        tr.append(td1);
                        let td2 = $("<td>")
                        td2.html(phone_number);
                        tr.append(td2);
                        let td3 = $("<td>");
                        td3.html(address2 + ", " + address1 + ", " + nation_name);
                        tr.append(td3);
                        let td4 = $("<td>")
                        td4.html("<p class=\"setting-address-id\" style=\"display: none\">" + address_id +
                        "</p><a class=\"btn btn-danger delete-address\">Delete</a>")
                        tr.append(td4);

                        // let p = $("<p>");
                        // p.attr('class', 'setting-address-id');
                        // p.style.display = "none";
                        // p.html(address_id);
                        // td4.append(p);
                        // let a = $("<a>");
                        // a.addClass("btn btn-danger delete-address");
                        // a.html("Delete");
                        // td4.append(a);

                    } else {
                        error();
                    }
                }, fail(result) {
                    error();
                }
            })
        }

    })
})