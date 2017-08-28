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
        weui.confirm("<input id='pay-money' type='number' placeholder='单位：元'>", {
            title: "请输入捐款金额",
            buttons: [{
                label: "取消",
                type: "default",
                onClick: function () {

                }
            }, {
                label: "确定",
                type: "primary",
                onClick: function () {
                    var money = $("#pay-money").val();
                    if (!isInteger(money)) {
                        weui.alert("请输入合法整数！");
                        return;
                    }
                    if (money <= 0) {
                        weui.alert("捐款金额必须大于0！");
                        return;
                    }
                    setPayMoney(money * 100);
                }
            }]
        });
    });

});

function setPayMoney(money) {
    location.href = "/wechat/pay?did=" + did + "&money=" + money;
}