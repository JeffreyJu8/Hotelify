/**
 * 
 */

 function fetchHotels(latitude, longitude, radius) {
    
    var url = '/your-servlet-path?latitude=' + latitude + '&longitude=' + longitude + '&radius=' + radius;

   //ajax request to search Servlet
   
   // need to handle success + display 
}

function fetchHotelDetails(placeId) {
    var url = '/your-details-servlet-path?place_id=' + placeId;
    
    // ajax request to details Servlet
    // need to handle success + display 
}

//displaying the results for initial list of hotels:
function displayHotels(hotels) {

    hotels.forEach(function(hotel) {
        
		// button for more details that will allow user to pull up a details page
        var detailsButton = document.createElement('button');
        detailsButton.textContent = 'View Details';
        // EMBEDS THE RETURNED HOTEL ID into the button 
        detailsButton.dataset.placeId = hotel.place_id;
        detailsButton.onclick = function() {
			// calls the ajax call which will initiate the 2nd google hotel detail api search (hotelDetailServelet.java)
            fetchHotelDetails(this.dataset.placeId);
        };

        
    });
}
