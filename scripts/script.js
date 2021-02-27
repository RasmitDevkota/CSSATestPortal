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

}

var googleUser = {};
gapi.load('auth2', function(){
  auth2 = gapi.auth2.init({
    client_id: '834594227639-bk92pnvbohf2kp9t93gncs6h7to0n8mj.apps.googleusercontent.com',
    cookiepolicy: 'single_host_origin',
  });
  attachSignin(document.getElementById('google-row'));
});

function attachSignin(element) {
    auth2.attachClickHandler(element, {},
    function(googleUser) {
        var profile = googleUser.getBasicProfile();
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "https://cssa-backend.herokuapp.com/checkEmail", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(profile.getEmail()); 
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                if(this.responseText == "1") {
                    var xhttp = new XMLHttpRequest();
                    xhttp.open("POST", "https://cssa-backend.herokuapp.com/check", true);
                    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    var values = {Unknown: profile.getEmail(),  Password: profile.getId()};
                    xhttp.send(JSON.stringify(values));
                    xhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200) {
                            let valueArray = JSON.parse(this.responseText).info;
                           /* setCookie('email',valueArray[0],365);
                            setCookie('User',valueArray[1],365);
                            setCookie('fName',valueArray[2],365);
                            setCookie('lName', valueArray[3], 365);
                            setCookie('hashedAuthCred', valueArray[4],365);
                            setCookie('googleToken',  profile.getId(), 365);
                            firebaseAuth(valueArray[0], valueArray[1], valueArray[4]);
                            window.location = "dashboard.html";*/
                        } 
                    }; 
                } else 	{
                    let username = profile.getGivenName() + "#" + (Math.floor(Math.random() * 9000) + 1000);
                    let password = generatePassword();
                    if(profile.getFamilyName()) {
                        var values = {Email: profile.getEmail(), Username: username, First: profile.getGivenName(), Last: profile.getFamilyName() , Password:password, Google:  profile.getId()};
                    } else {
                        var values = {Email: profile.getEmail(), Username: username, First: profile.getGivenName(), Last: "-" , Password:password, Google:  profile.getId()};
                    }
                    var xhttp = new XMLHttpRequest();
                    xhttp.open("POST", "https://cssa-backend.herokuapp.com/registration", true);
                    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    xhttp.send(JSON.stringify(values)); 
                    xhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200) {
                            if(this.responseText.includes("argon")) {
                               /* setCookie('email',profile.getEmail(),365);
                                setCookie('User',username,365);
                                setCookie('fName',profile.getGivenName(),365);
                                setCookie('lName',profile.getFamilyName(),365);
                                setCookie('hashedAuthCred',this.responseText,365);
                                setCookie('googleToken',  profile.getId(), 365);
                                firebaseAuth(profile.getEmail(), username, this.responseText);
                                window.location = "dashboard.html";*/
                            } else {
                                alert(this.responseText);
                            }
                        } 
                    };  	
                }
            } 
        }; 
    })
}