var did = request("did");

$(document).ready(function () {
    
    DonationManager.usingEmail(function (using) {
        if (!using) {
            $("#cell-email").remove();
        }
    });

    DonationManager.get(did, function (donation) {
        if (donation == null) {
            location.href = "link.html";
            return;
        }

        $("#donation-info").fillText({
            money: donation.money / 100,
            name: donation.name,
            sex: donation.sex ? "先生" : "女士",
            year: donation.year,
            email: donation.email
        });

        if (donation.payed) {
            weui.confirm("已支付，查看证书！", function () {
                location.href = "certificate.html?did=" + did;
            });
            return;
        }

        WechaterManager.getJsConfig(window.location.href, function(config) {
            wx.config({
                debug: false,
                appId: config.appId,
                timestamp: config.timestamp,
                nonceStr: config.nonceStr,
                signature: config.signature,
                jsApiList: ["chooseWXPay"]
            });

            wx.ready(function(){
                DonationManager.pay(did, function(result) {
                    if (result == null) {
                        weui.confirm("支付错误，返回首页！", function () {
                            location.href = "/";
                        });
                        return;
                    }

                    wx.chooseWXPay({
                        timestamp: result.timestamp,
                        nonceStr: result.nonceStr,
                        package: "prepay_id=" + result.prepayId,
                        signType: "MD5",
                        paySign: result.paySign,
                        success: function (res) {
                            weui.confirm("支付成功！", {
                                buttons: [{
                                    label: "查看捐赠证书",
                                    type: "primary",
                                    onClick: function () {
                                        location.href = "certificate.html?did=" + did;
                                    }
                                }]
                            });
                        }
                    });
                });
            });
        });
    });

});