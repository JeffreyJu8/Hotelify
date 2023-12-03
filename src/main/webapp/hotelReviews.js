
let reviewsDiv = document.getElementById("reviews");
let hotelTitle = document.getElementById("hotel-title");

window.onload = () => {
	fetchReviews();
}


function fetchReviews() {
	
	const urlParams = new URLSearchParams(window.location.search);
	var hotelId = urlParams.get('hotelID');
	var hotelName = urlParams.get('name');
	
	hotelTitle.innerHTML = hotelName + " Reviews";
	
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "get-reviews?index=hotel&hotelId=" + hotelId, true);
	xhttp.onload = function() {
		console.log(this.responseText);
		reviews = JSON.parse(this.responseText);
		console.log(reviews);
		reviewsDiv.innerHTML = reviews.map((review) => 
			`<div class="review-card columns-2 flex-row">
				<div class="card-half-left">
					<h3>Rating: ${review.star_rating}</h3>
					<h5>Date: ${review.date}</h5>
					<p>Review: ${review.review}</p>
				</div>
				<div class="card-half-right">
					<h5>Details</h5>
					<hr>
					<p>Cleanliness: ${review.cleanliness}</p>
					<p>Pet Friendly: ${review.pet_friendly}</p>
					<p>Eco Friendly: ${review.eco_friendly}</p>
					<p>Excellent Service: ${review.excellent_service}</p>
					<p>Dirty: ${review.dirty}</p>
					<p>Missing Amenities: ${review.missing_amenities}</p>
					<p>Hidden Fees: ${review.hidden_fees}</p>
					<p>Poor Service: ${review.poor_service}</p>
				</div>
			</div>`
		).join('');
	
	}
	xhttp.send();
	
}

function createReview() {
	const urlParams = new URLSearchParams(window.location.search);
	var hotelId = urlParams.get('hotelID');
	window.location.replace("form.html?hotelID=" + hotelId);
}