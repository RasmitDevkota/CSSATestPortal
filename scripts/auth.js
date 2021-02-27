function auth() {
    let ue = _("si-ue").value;
    const pwd = _("si-password").value;

    if (!ue.includes("@")) {
        ue = getEmail(ue);
    }

    firebase.auth().signInWithEmailAndPassword(ue, pwd).then(() => {
        console.log("Signed in!");

        window.location.href = "dashboard.html";
    }).catch((error) => {
        console.error(`Error occurred signing in: ${error}`);
        console.log(error.code)

        alert(`Error occurred: ${error.message}`);
    });
}

function getEmail(username) {
    
}

function unauth() {
    if (firebase.auth().currentUser != null) {
        firebase.auth().signOut();
    }

    if (!window.location.href.includes("index.html") || window.location.href != "") {
        window.location.href = "index.html";
    }
};

function sendPasswordReset() {
    var email = _('premail');
    
    if (email != null) {
        firebase.auth().sendPasswordResetEmail(email).then(function () {
            alert('Password reset email sent!');
        }).catch(function (error) {
            var errorCode = error.code;
            var errorMessage = error.message;
            if (errorCode == 'auth/invalid-email') {
                alert(errorMessage);
            } else if (errorCode == 'auth/user-not-found') {
                alert(errorMessage);
            }
            console.error(error);
        });
    } else {
        alert("Please enter an email.");
    }
};