var did = request("did");

$(document).ready(function () {

    DonationManager.get(did, function (donation) {
        if (donation == null) {
            location.href = "link.html";
            return;
        }

        $("#certificate").fillText({
            tradeNo: donation.tradeNo,
            money: donation.money / 100,
            name: donation.name,
            sex: donation.sex ? "先生" : "女士"
        });

        weui.topTips("请截图保存此证书！");
    });

});