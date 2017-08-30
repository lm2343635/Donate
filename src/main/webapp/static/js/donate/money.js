var did = request("did");

$(document).ready(function () {

    DonationManager.get(did, function (donation) {
        if (donation == null) {
            location.href = "link.html";
            return;
        }
    });

    $("#pay-defaults").click(function () {
        setPayMoney(11000);
    });

    $("#pay-others").click(function () {
        weui.confirm("<input id='pay-money' type='number' pattern='[0-9]*' placeholder='请填写整数金额'>", {
            title: "请输入捐款金额",
            buttons: [{
                label: "取消",
                type: "default",
                onClick: function () {}
            }, {
                label: "确定",
                type: "primary",
                onClick: function () {
                    var money = $("#pay-money").val();
                    if (!isInteger(money)) {
                        weui.topTips("金额必须为合法整数！");
                        return;
                    }
                    if (money <= 0 || money > 10000000) {
                        weui.topTips("捐款金额在一元到一千万元之间！");
                        return;
                    }
                    setPayMoney(money * 100);
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