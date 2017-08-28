var did = request("did");

$(document).ready(function () {

    DonationManager.get(did, function (donation) {
        if (donation == null) {
            location.href = "link.html";
            return;
        }

        $("#certificate-content").fillText({
            money: donation.money / 100,
            name: donation.name,
            sex: donation.sex ? "先生" : "女士"
        })
    });

});