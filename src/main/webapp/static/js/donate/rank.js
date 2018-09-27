var pageSize = 15;

$(document).ready(function () {
    loadDonations(1);
});

function loadDonations(page) {
    console.log(page)
    DonationManager.getPayedCount(function (count) {
        $("#page-list").mengularClear();
        for (var i = 1; i < Math.ceil(count / pageSize + 1); i++) {
            $("#page-list").mengular(page == i ? ".selected-page-template": ".page-template", {
                page: i
            });
        }
    });

    DonationManager.getPayedByPage(page, pageSize, function (donations) {
        console.log(donations)
        $("#donation-list").mengularClear();
        for (var i in donations) {
            var donation = donations[i];
            $("#donation-list").mengular(".donation-template", {
                name: donation.name,
                sex: donation.sex ? "先生" : "女士",
                money: donation.money / 100.0,
                createAt: donation.createAt.format(DATE_HOUR_MINUTE_FORMAT)
            });
        }

        $("html, body").animate({
            scrollTop: 0
        }, 300);
    });
}