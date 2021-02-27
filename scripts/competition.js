let currentEvent = "None";
let time = 3600;

window.addEventListener("load", () => {
    // _("welcome-user").innerHTML = `Welcome, ${user.name}`;
});

function loadCompetition() {
    userDoc.get().then((doc) => {
        for (let e = 1; e < 5; e++) {
            if (doc.data()[`event${e}`] != "None") {
                _("competitions").innerHTML += `
                    <div>
                        <a href="test.html?test=${doc.data()["event" + e]}">${doc.data()["event" + e]}</a>
                    </div>
                `;
            }
        }
    });
}

function loadTest(test) {
    _("title").innerHTML = test;
    _("details").innerHTML = `UID: ${user.uid} | Time Remaining: 60 minutes and 0 seconds`;

    console.log(test);

    tests.get(test).then((doc) => {
        console.log(doc);
        console.log(doc.data());
    }).catch((error) => {
        console.error(error);

        // alert("Error occurred loading test.");
    });

    setTimeout(() => {
        timer();
    }, 1000);
}

function timer() {
    var minutes = Math.floor(time / 60);
    var seconds = time % 60;

    var timeText = `${minutes} minute${(minutes == 1) ? "" : "s"} and ${seconds} second${(seconds == 1) ? "" : "s"}`;
    
    _("details").innerHTML = `UID: ${user.uid} | Time Remaining: ${timeText}`;

    return setTimeout(timer, 1000);
}

function saveAnswer(data) {
    if (currentEvent != "None") {
        userDoc.collection("answers").doc(currentEvent).set(data, { merge: true }).then(() => {
            console.log(`Set ${currentEvent} answers: ${data}`);
        }).catch((e) => {
            console.error(e);

            alert("Error occurred saving answers, please refresh the page and try again!");
        });
    }
}