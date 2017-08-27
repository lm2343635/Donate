$(document).ready(function () {
    $("#donate-register").click(function() {
        var name = $("#donate-name").val();
        var sex = $("#donate-sex").val();
        var year = $("#donate-year").val();
        var email = $("#donate-email").val();
        if (name == "" || sex == "" || year == "" || email == "") {
            weui.alert("请填写所有表单项！");
            return;
        }

        DonationManager.register(name, sex, year, email, function (did) {
            if (did == null) {
                return;
            }

            location.href = "pay.html?did=" + did;
        });
    });
});