let currentEvent = "None";
let time = 3600;

if (window.location.href.includes("test")) {
    window.addEventListener('beforeunload', (event) => {
        if (currentEvent != "None") {
            event.preventDefault();

            event.returnValue = '';
        }
    });

    window.addEventListener('unload', () => {
        if (currentEvent != "None") {
            submit(true);
        }
    });
}

function loadCompetition() {
    userDoc.get().then((doc) => {
        for (let e = 1; e < 5; e++) {
            if (doc.data()[`event${e}`] != "None" && !doc.data()[`event${e}`].includes("!")) {
                _("competitions").innerHTML += `
                    <div>
                        <a href="test.html?test=${doc.data()["event" + e]}">\> ${doc.data()["event" + e]}</a>
                    </div>
                `;
            }
        }
    });
}

var answers = new Map();

function loadTest(test) {
    userDoc.get().then((doc) => {
        if (Object.values(doc.data()).includes(test + "!")) {
            alert("Sorry, you already finished this test!");

            return window.location.href = "dashboard.html";
        } else if (!Object.values(doc.data()).includes(test)) {
            alert("Sorry, you don't have this event!");

            return window.location.href = "dashboard.html";
        }
    });

    currentEvent = test;

    _("title").innerHTML = test;
    _("details").innerHTML = `UID: ${user.uid} | Time Remaining: 60 minutes and 0 seconds`;

    var testContainer = _("test-container");

    tests.doc(test).collection("questions").get().then((querySnapshot) => {
        var docs = [];

        querySnapshot.forEach((doc) => {
            docs[Number(doc.id.split("question")[1]) - 1] = doc;
        });

        var q = 0;
        docs.forEach((doc) => {
            if (q != 0) {
                testContainer.innerHTML += `<hr>`;
            }

            q++;

            const data = doc.data();

            switch (data.type) {
                case "mcq":
                    var question = `
                        <div class="question mcq">
                    `;

                    question += `
                            <div class="question-text">
                                ${q}. (${data.value} point${(data.value > 1) ? "s" : ""}) ${data.text}
                            </div>
                    `;

                    question += `
                            <div class="answer mcq-options">
                    `;

                    for (i in data.options) {
                        question += `
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="${doc.id}" id="${doc.id}-option${i}" value="${data.options[i]}" onchange="answer(this.id, this.value)">

                                    <label class="form-check-label" for="${doc.id}-option${i}">
                                        ${data.options[i]}
                                    </label>
                                </div>
                        `;
                    }

                    question += `
                            </div>
                        </div>
                    `;

                    testContainer.innerHTML += question;
                    break;
                case "msq":
                    var question = `
                        <div class="question msq">
                    `;

                    question += `
                            <div class="question-text">
                                ${q}. (${data.value} point${(data.value > 1) ? "s" : ""}) ${data.text}
                            </div>
                    `;

                    question += `
                            <div class="answer msq-options">
                    `;

                    for (i in data.options) {
                        question += `
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="${doc.id}option${i}" id="${doc.id}-moption${i}" value="${data.options[i]}" onchange="answer(this.id, this.value)">

                                    <label class="form-check-label" for="${doc.id}-moption${i}">
                                        ${data.options[i]}
                                    </label>
                                </div>
                        `;
                    }

                    question += `
                            </div>
                        </div>
                    `;

                    testContainer.innerHTML += question;
                    break;
                case "mq":
                    var question = `
                        <div class="question mq">
                    `;

                    question += `
                            <div class="question-text">
                                ${q}. (${data.value} point${(data.value > 1) ? "s" : ""}) ${data.text}
                            </div>
                    `;

                    question += `
                            <div class="answer mq-options-container">
                    `;

                    question += `
                                <div class="mq-optionsA">
                    `;

                    for (i in data.optionsA) {
                        question += `
                                    <div class="form-check">
                                        <input class="form-check-input mq-input" type="text" name="${doc.id}optionA${i}" id="${doc.id}-optionA${i}" maxlength="1" onchange="answer(this.id, this.value)">

                                        <label class="form-check-label" for="${doc.id}-optionA${i}">
                                            &nbsp;${data.optionsA[i]}
                                        </label>
                                    </div>
                        `;
                    }

                    question += `
                                </div>

                                <div class="mq-optionsB">
                    `;

                    for (i in data.optionsB) {
                        question += `
                                    <div class="form-check">
                                        ${Number(i) + 1}. ${data.optionsB[i]}
                                    </div>
                        `;
                    }


                    question += `
                                </div>
                            </div>
                        </div>
                    `;

                    testContainer.innerHTML += question;
                    break;
                case "lrq":
                    var question = `
                        <div class="question lrq">
                    `;

                    question += `
                            <div class="question-text">
                                ${q}. (${data.value} point${(data.value > 1) ? "s" : ""}) ${data.text}
                            </div>
                    `;

                    question += `
                            <div class="answer lrq-answer">
                    `;

                    question += `
                                <div class="form-group">
                                    <textarea class="form-control" id="${doc.id}-response" rows="5" onchange="answer(this.id, this.value)"></textarea>
                                </div>
                    `;
                    
                    question += `
                            </div>
                        </div>
                    `;

                    testContainer.innerHTML += question;
                    break;
                case "fitb":
                    var question = `
                        <div class="question fitb">
                    `;

                    question += `
                            <div>
                                ${q}. (${data.value} point${(data.value > 1) ? "s" : ""})
                                &nbsp;
                                ${data.text.split("|~~~~|")[0]}
                                <input type="text" class="fitb-answer" id="${doc.id}-blank" onchange="answer(this.id, this.value)">
                                ${data.text.split("|~~~~|")[1]}
                            </div>
                    `;
                    
                    question += `
                        </div>
                    `;

                    testContainer.innerHTML += question;
                    break;
                default:
                    alert("An error occurred getting the test! Please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!");
            }
        });
    }).catch((error) => {
        console.error(error);
    });

    if (["Capture the Flag", "Website Design", "Tech Support", "Programming Challenges", "Golf", "Web Scraping"].includes(test)) {
        _("details").innerHTML = `UID: ${user.uid} | Deadline: March 31st, 11:59 PM`;
    } else {
        userDoc.collection("answers").doc(test).get().then((doc) => {
            console.log(doc.data().time);

            if (doc.data().time != undefined) {
                time -= (new Date()).getTime() - doc.data().time;
            } else {
                let startTime = (new Date()).getTime();

                userDoc.collection("answers").doc(currentEvent).set({
                    time: startTime
                }, { merge: true }).then(() => {
                    console.log(startTime);
                }).catch((e) => {
                    console.error(e);
                });
            }

            setTimeout(() => {
                timer();
            }, 1000);
        });
    }
}

function timer() {
    var minutes = Math.floor(time / 60);
    var seconds = time % 60;

    var timeText = `${minutes} minute${(minutes == 1) ? "" : "s"} and ${seconds} second${(seconds == 1) ? "" : "s"}`;
    
    _("details").innerHTML = `UID: ${user.uid} | Time Remaining: ${timeText}`;

    if (time % 90 == 0) {
        saveAnswers();
    }

    time -= 1;

    if (time <= 0) {
        submit(true);
    } else {
        return setTimeout(timer, 1000);
    }
}

function answer(id, answer) {
    if (id.includes("optionA")) {
        var answerList;

        if (answers.get(id.substr(0, id.indexOf("-"))) == undefined) {
            var answerList = [];
        } else {
            var answerList = answers.get(id.substr(0, id.indexOf("-")));
        }

        answerList[id.split("optionA")[1]] = answer;
        
        answers.set(id.substr(0, id.indexOf("-")), answerList);
    } else if (id.includes("moption")) {
        var answerlist;

        if (answers.get(id.substr(0, id.indexOf("-"))) == undefined) {
            answerList = [];
        } else {
            answerList = answers.get(id.substr(0, id.indexOf("-")));
        }

        if (_(id).checked) {
            answerList.push(answer);
        } else {
            answerList.splice(answerList.indexOf(answer), 1);
        }

        answers.set(id.substr(0, id.indexOf("-")), answerList);
    } else {
        answers.set(id.substr(0, id.indexOf("-")), answer);
    }

    console.log(answers);
}

function saveAnswers() {
    const data = {};

    answers.forEach((answer, question) => {
        data[question] = answer.toString();
    });

    userDoc.collection("answers").doc(currentEvent).set(data, { merge: true }).then(() => {
        console.log(`Saved ${currentEvent} answers`);
    }).catch((e) => {
        console.error(e);

        alert("Error occurred saving answers, please refresh the page and try again!");
    });
}

function submit(confirmed = false) {
    if (!confirmed) {
        if (!confirm("Are you sure you want to submit the test? You won't be able to access it again!")) {
            return;
        }
    }

    userDoc.get().then((doc) => {
        let data = {};

        let finishedEvent = Object.keys(doc.data()).find(key => doc.data()[key] === currentEvent);

        data[finishedEvent] = currentEvent + "!";

        saveAnswers();

        userDoc.set(data, { merge: true }).then(() => {
            console.log(`Submitted ${currentEvent} answers`);

            alert(`Successfully submitted the test!`);

            currentEvent = "None";

            window.location.href = "dashboard.html";
        }).catch((e) => {
            console.error(e);
    
            alert("Error occurred submitting answers, please refresh the page and try again!");
        });
    }).catch((e) => {
        console.error(e);

        alert("Error occurred submitting answers, please refresh the page and try again!");
    });
}

function testRedirect(dest) {
    if (confirm("Are you sure you want to exit the test? If you do, your answers will be saved and submitted!")) {
        submit(true);

        if (dest == "dashboard") {
            window.location.href = "dashboard.html";
        } else {
            if (firebase.auth().currentUser != null) {
                firebase.auth().signOut();
            }
    
            if (!window.location.href.includes("index.html") || window.location.href != "") {
                window.location.href = "index.html";
            }
        }
    }
}