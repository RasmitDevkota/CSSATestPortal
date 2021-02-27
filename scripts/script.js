firebase.initializeApp({
    apiKey: "AIzaSyAPTvz8weUBIMyjl6ekC1uegX-j4u2Z1sc",
    authDomain: "cssa-dev.firebaseapp.com",
    databaseURL: "https://cssa-dev-default-rtdb.firebaseio.com",
    projectId: "cssa-dev",
    storageBucket: "cssa-dev.appspot.com",
    messagingSenderId: "921024173703",
    appId: "1:921024173703:web:46f4a35d815964ddf44a22",
    measurementId: "G-WBN11JNGTN"
});

firebase.analytics();

var db = firebase.firestore();
db.enablePersistence();

const users = db.collection("users");
const tests = db.collection("tests");

firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL)

firebase.auth().onAuthStateChanged((user) => {
    if (user) {
        pageLoad(true);
    } else {
        pageLoad(false);
    }
});

function pageLoad(u) {
    if (u) {
        window.user = firebase.auth().currentUser;

        if (!user) {
            console.error("Auth error occurred, pageLoad(true) called even though firebase.auth().currentUser is " + user);

            pageLoad(false);
        }

        window.userDoc = users.doc(user.uid);

        if (window.location.href.includes("dashboard.html")) {
            loadCompetition();
        } else if (window.location.href.includes("test.html")) {
            var urlParams = new URLSearchParams(decodeURIComponent(window.location.search));
            var test = urlParams.get('test');

            loadTest(test);
        }
    } else {
        if (!window.location.href.includes("index.html") && window.location.href != "") {
            window.location.href = "index.html";
        }
    }
}

function _(id) {
    return document.getElementById(id);
}

function display(id) {
    if (_(id).style.display == "flex") {
        _(id).style.display = "none";
    } else {
        _(id).style.display = "flex";
    }

    console.log(_(id).style.display);
}