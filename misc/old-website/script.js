const apiUrl = "https://localhost:8443/"
let user = null

const doSomething = () => {
    console.log("working")
    return false
}

const login = () => {
    const username = document.getElementById("username").value
    const password = document.getElementById("password").value
    console.log(password)
    
    // fetch(
    //     apiUrl+"login", 
    //     { method: 'POST', body: JSON.stringify({username: usernm, password: pass}) }
    //     ).then((response) => {
    //         if (response == "Login Success") {
    //             window.location.href = "/login_page.html"
    //         }
    //     })

    fetch( apiUrl+"login"+"?username="+username+"&password="+password )
        .then((response) => {
            console.log(response.ok)
            if (response.ok) {
                user = username
                window.location.href = "/home.html"
            }
        })

    return false
}

function isLoggedIn() {
    console.log(user)
    if (user == null) {
        // window.location.href = "login_page.html"
    }
} 