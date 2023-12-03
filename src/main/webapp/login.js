document.addEventListener('DOMContentLoaded', (event) => {
    const loginForm = document.querySelector('.login form');
    const signUpForm = document.querySelector('.signup form');

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // Perform login
        login(username, password);
    });

    signUpForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const email = document.getElementById('signup-email').value;
        const username = document.getElementById('signup-username').value;
        const password = document.getElementById('signup-password').value;
        const confirmPassword = document.getElementById('signup-confirm-password').value;
        const termsChecked = document.getElementById('terms').checked;

        // Validate the passwords
        if (password !== confirmPassword) {
            displayErrorMessage('signup', 'Passwords do not match.');
            return;
        } else if (!termsChecked) {
            displayErrorMessage('signup', 'You must agree to the terms and conditions.');
            return;
        }

        // Perform sign up
        signUp(email, username, password);
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const signupStatus = urlParams.get('SignUpServlet');

    if (signupStatus === 'success') {
        alert('Signup successful. You can now log in.');
    } else if (signupStatus === 'error') {
        alert('Signup failed. Please try again.');
    } else if (signupStatus === 'sqlerror') {
        alert('A database error occurred. Please try again later.');
    }
});
function displayErrorMessage(formType, errorData) {
    let errorMessageElement;
    if (formType === 'login') {
        errorMessageElement = document.getElementById('login-error-message');
    } else if (formType === 'signup') {
        errorMessageElement = document.getElementById('signup-error-message');
    }

    if (errorMessageElement) {
        errorMessageElement.textContent = errorData;
    }
}
function login(username, password) {
    // Construct the URL of your login API endpoint
    const loginUrl = '/CS201-final/LoginServlet';

    // Prepare the data to send in the request
    const data = { username: username, password: password };

    // Make the AJAX request using fetch
    fetch(loginUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        // Check if the request was successful
        if (response.ok) {
            return response.json(); // If successful, parse the JSON
        } else {
            // If not successful, still try to parse the error message
            return response.json().then(errorData => Promise.reject(errorData));
        }
    })
    .then(data => {
        console.log('Login successful:', data);
        // Store the session token in localStorage
        if (data.sessionToken) {
            localStorage.setItem('sessionToken', data.sessionToken);
            window.location.href = 'homepage.html'; // Redirect to homepage
        } else {
            // Handle cases where session token is not provided
            console.error('Session token not provided');
        }
        
    })
    .catch(errorData => {
        // Display the login error message
        displayErrorMessage('login', errorData);
    });
}

function signUp(email, username, password) {
    const signUpUrl = '/CS201-final/SignUpServlet'; // Ensure this URL matches your servlet's URL

    const data = { email: email.toLowerCase(), username: username, password: password };

    fetch(signUpUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        // Check if the request was successful
        if (response.ok) {
            return response.json(); // If successful, parse the JSON
        } else {
            // If not successful, still try to parse the error message
            return response.json().then(errorData => Promise.reject(errorData));
        }
    })
    .then(data => {
        console.log('Signup successful:', data); // 'data' should contain the userID
        window.location.href = 'homepage.html';
    })
    .catch(errorData => {
        // Display the signup error message
        displayErrorMessage('signup', errorData);
    });
}