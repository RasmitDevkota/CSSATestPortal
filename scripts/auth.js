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