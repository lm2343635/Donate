var did = request("did");
var min = 0;

$(document).ready(function () {

    DonationManager.get(did, function (donation) {
        if (donation == null) {
            location.href = "link.html";
            return;
        }
    });

    ConfigManager.getConfigObject(function (config) {
        min = config["global"]["min"];
        $("#pay-defaults span").text(min);
    });

    $("#pay-defaults").click(function () {
        setPayMoney(min * 100);
    });

    $("#pay-others").click(function () {
        weui.confirm("<input id='pay-money' type='number' placeholder='请填写整数金额'>", {
            title: "捐款金额需大于" + min + "元",
            buttons: [{
                label: "取消",
                type: "default",
                onClick: function () {}
            }, {
                label: "确定",
                type: "primary",
                onClick: function () {
                    var money = $("#pay-money").val();
                    if (!isNum(money)) {
                        weui.topTips("金额必须为合法数字！");
                        return;
                    }
                    if (money < min) {
                        weui.topTips("捐款金额需大于" + min + "元！");
                        return;
                    }
                    if (money > 10000000) {
                        weui.topTips("捐款金额不能超过一千万元之间！");
                        return;
                    }
                    setPayMoney(parseInt(money * 100));
                }
            }]
        });

        $("#pay-money").focus(function () {
            $("#foot").hide();
        }).blur(function () {
            $("#foot").show();
        });
    });



});

function setPayMoney(money) {
    location.href = "/wechat/pay?did=" + did + "&money=" + money;
}