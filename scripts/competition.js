let currentEvent = "None";

window.addEventListener("load", () => {
    // _("welcome-user").innerHTML = `Welcome, ${user.name}`;
});

function loadCompetition() {
    userDoc.get().then((doc) => {
        for (let e = 1; e < 5; e++) {
            if (doc.data()[`event${e}`] != "None") {
                _("competitions").innerHTML +=
                `
                    <div>
                        <a href="test.html?test=${doc.data().event1}">${doc.data().event1}</a>
                    </div>
                `;
            }
        }
    });
}

function loadTest(test) {
    tests.get(test).then((doc) => {
        console.log(doc.data());
    });
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