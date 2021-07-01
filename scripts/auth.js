function auth() {
    let ue = _("si-ue").value;
    const pwd = _("si-password").value;

    var values = { Unknown: ue, Password: pwd };
    
    var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "https://cssa-backend.herokuapp.com/check", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send(JSON.stringify(values));
    
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (this.responseText == "Invalid Credentials" || this.responseText == "Account with that Username/Email Does Not Exist. Please create an account instead.") {
				alert(this.responseText);
			} else {
                let valueArray = JSON.parse(this.responseText).info;
                
                firebase.auth().signInWithEmailAndPassword(valueArray[0], valueArray[4]).then(() => {
                    console.log("Signed in!");
                    setCookie('username',valueArray[1],365);
                    window.location.href = "dashboard.html";
                }).catch((error) => {
                    if (error.code == "auth/wrong-password") {
                        firebase.auth().signInWithEmailAndPassword(valueArray[0], pwd).then(() => {
                            firebase.auth().currentUser.updatePassword(valueArray[4]).then(function() {
                                setCookie('username',valueArray[1],365);
                                console.log("Success");
                            }).catch(function(error) {
                                console.error(error);
                            });

                            console.log("Signed in!");
        
                            window.location.href = "dashboard.html";
                        }).catch((error) => {
                            console.error(`Error occurred signing in: ${error}`);

                            alert(`Error occurred: ${error.message}`);
                        });
                    } else {
                        console.error(`Error occurred signing in: ${error}`);

                        alert(`Error occurred: ${error.message}`);
                    }
                });
			}
		} 
	}; 
}

function signUp() {
   
	let emailC = document.getElementById("su-email").value;
	var usr = document.getElementById("su-username").value;
	var fName = document.getElementById("su-firstname").value;
	var lName = document.getElementById("su-lastname").value;
    var pwd = document.getElementById("su-pwd").value;
    var values = { Email: emailC, Username: usr, First: fName, Last: lName, Password: pwd, Google: "-" };    
    
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "https://cssa-backend.herokuapp.com/registration", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send(JSON.stringify(values)); 
    
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if(this.responseText.includes("argon")) {
                var hashed = this.responseText;
                firebase.auth().createUserWithEmailAndPassword(emailC, hashed).then(function () {
                    firebase.auth().signInWithEmailAndPassword(emailC, hashed).then(() => {
                        console.log("Signed in!");
                        alert(usr);
                        setCookie('username',usr,365);
                        //window.location.href = "dashboard.html";
                    }).catch(function (error) {
                        console.log(error);
                    });
                }).catch((error) => {
                    console.log(error);
                });
			} else {
			//	alert(this.responseText);
			}
		} 
	};  
}

function unauth() {
    if (confirm("Are you sure you want to log out?")) {
        if (firebase.auth().currentUser != null) {
            firebase.auth().signOut();
        }

        if (!window.location.href.includes("index.html") || window.location.href != "") {
            window.location.href = "index.html";
        }
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

                    var values = { Unknown: profile.getEmail(), Password: profile.getId() };
                    
                    xhttp.send(JSON.stringify(values));

                    xhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200) {
                            let valueArray = JSON.parse(this.responseText).info;

                            firebase.auth().signInWithEmailAndPassword(valueArray[0], valueArray[4]).then(() => {
                                console.log("Signed in!");
                                setCookie('username',valueArray[1],365);
                                
                                window.location.href = "dashboard.html";
                            }).catch((error) => {
                                if (error.code == "auth/wrong-password") {
                                    alert("There's a small problem with your account, but don't worry, we can fix it very easily!\n\nContact crewcssa@gmail.com or join our Discord at bit.ly/cssa-discord for assistance!")
                                } else {
                                    console.error(`Error occurred signing in: ${error}`);
            
                                    alert(`Error occurred: ${error.message}`);
                                }
                            });
                        }
                    }; 
                } else 	{
                    let username = profile.getGivenName() + "#" + (Math.floor(Math.random() * 9000) + 1000);
                    let password = generatePassword();

                    if (profile.getFamilyName()) {
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
                            if (this.responseText.includes("argon")) {
                                var hashed = this.responseText;

                                firebase.auth().createUserWithEmailAndPassword(profile.getEmail(), hashed).then(function () {
                                    firebase.auth().signInWithEmailAndPassword(profile.getEmail(), hashed).then(() => {
                                        console.log("Signed in!");
                                        setCookie('username',username,365);
                                        window.location.href = "dashboard.html";
                                    }).catch(function (error) {
                                        console.log(error);
                                    });
                                }).catch((error) => {
                                    console.log(error);
                                });
                            } else {
                                alert(this.responseText);
                            }
                        } 
                    };  	
                }
            } 
        }; 
    });
}

function generatePassword() {
    var length = 8,
        charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
        retVal = "";

    for (var i = 0, n = charset.length; i < length; ++i) {
        retVal += charset.charAt(Math.floor(Math.random() * n));
    }

    return retVal;
}