$(document).ready(function () {

    checkAdminSession(function () {
        DonationManager.getPayedDonations(function (donations) {
            if (donations == null) {
                location.href = "session.html";
                return;
            }

            for (var i in donations) {
                var donation = donations[i];
                $("#donation-list tbody").mengular(".donation-list-template", {
                    payAt: donation.payAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                    name: donation.name,
                    sex: donation.sex ? "先生" : "女士",
                    year: donation.year,
                    email: donation.email,
                    money: donation.money / 100,
                    tradeNo: donation.tradeNo
                });
            }

        });
    });

});