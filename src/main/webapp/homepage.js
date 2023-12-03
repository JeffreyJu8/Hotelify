// This function will be called when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM fully loaded'); // Check if DOMContentLoaded is working
    //initializeSearchForm();
    initializeUserCustomization();
    updateHomeButtonLink();
    attachLogoutEvent(); // Attach the logout event listener
});

/*function initializeSearchForm() {
    const searchForm = document.getElementById('searchForm');

    searchForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

        // Retrieve form inputs
        const restaurantName = document.getElementById('restaurantName').value;
        
        // Execute the search
        searchRestaurants(restaurantName, latitude, longitude, sort);
    });
}*/

function initializeUserCustomization() {
    const sessionToken = localStorage.getItem('sessionToken');
    if (sessionToken) {
        customizePageForUser();
    } else {
        // Call this function to set the page for non-logged-in users
        customizePageForGuest();
    }
}

function updateHomeButtonLink() {
    const sessionToken = localStorage.getItem('sessionToken');
    const homeLinks = document.querySelectorAll('a[href="homepage.html"]');

    // Change home links based on sessionToken existence
    homeLinks.forEach(link => {
        link.setAttribute('href', sessionToken ? 'homepage.html' : 'homepage.html');
    });
}

function attachLogoutEvent() {
    const logoutButton = document.getElementById('logoutLink');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault(); // Prevent default navigation
            logoutUser();
        });
    }
}
function logoutUser() {
    localStorage.removeItem('sessionToken'); // Remove session token
    window.location.href = 'homepage.html'; // Redirect to home page after logout
}
function customizePageForUser() {
    // Hide the login/sign-up link and show the profile link
    document.getElementById('loginSignupLink').style.display = 'none';
    document.getElementById('profileLink').style.display = 'block';
    document.getElementById('logoutLink').style.display = 'block';

    // Additional customizations for logged-in users can be done here
}

function customizePageForGuest() {
    // Show the login/sign-up link and hide the profile link
    document.getElementById('loginSignupLink').style.display = 'block';
    document.getElementById('profileLink').style.display = 'none';
    document.getElementById('logoutLink').style.display = 'none';

    // Additional customizations for guests can be done here
}
