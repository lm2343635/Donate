var pickerData = [];
for (var year = 1920; year <= 2017; year++) {
    pickerData[pickerData.length] = {
        label: year + "年",
        value: year
    };
}

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

            location.href = "money.html?did=" + did;
        });
    });


    $("#donate-year-selector").click(function () {
        weui.picker(pickerData, {
            className: 'custom-classname',
            container: 'body',
            defaultValue: [3],
            onConfirm: function (result) {
                $("#donate-year-selector").text(result[0].label);
                $("#donate-year").val(result[0].value);
            }
        });

    });
    
    $("#donate-sex-selector").click(function () {
        weui.actionSheet([{
            label: "先生",
            onClick: function () {
                setSex(true);
            }
        }, {
            label: "女士",
            onClick: function () {
                setSex(false);
            }
        }], [{
            label: "取消",
            onClick: function () {
                weui.topTips("请选择性别！");
            }
        }]);
    });

    $("#donate-email, #donate-name").focus(function () {
        $("#foot").hide();
    }).blur(function () {
        $("#foot").show();
    });

});

function setSex(sex) {
    $("#donate-sex-selector").text(sex ? "先生" : "女士");
    $("#donate-sex").val(sex);
}