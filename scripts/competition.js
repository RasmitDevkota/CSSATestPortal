let currentEvent = "None";

function loadCompetition() {
    userDoc.get().then((doc) => {
        for (let e = 1; e < 5; e++) {
            if (doc.data()[`event${e}`] != "None") {
                _("competitions").innerHTML +=
                `
                    <div>
                        <a href="test.html?test=${doc.data().event1}">${doc.data().event1}</a>
                    </div>
                `
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
            
        }).catch((e) => {

        });
    }
}