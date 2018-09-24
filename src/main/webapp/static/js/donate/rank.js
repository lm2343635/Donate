$(document).ready(function () {
    loadDonations(0, 10);
});

function loadDonations(page, pageSize) {
    DonationManager.getPayedCount(function (count) {
        
    });

    DonationManager.getPayedByPage(page, pageSize, function (donations) {
        for (var i in donations) {
            var donation = donations[i];
            $("#donation-list").mengular(".donation-template", {
                name: donation.name,
                sex: donation.sex ? "先生" : "女士",
                money: donation.money / 100.0
            });
        }
    });
}