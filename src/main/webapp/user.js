let userDiv = document.getElementById("user");
let pastReviews = document.getElementById("past");

window.onload = () => {
	fetchReviews();
}

function fetchReviews() {
	const urlParams = new URLSearchParams(window.location.search);
	var username = urlParams.get(userID);

	userDiv.innerHTML += " ";
	userDiv.innerHTML += username;

	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("GET", "get-reviews?index=user&username=" + username, true);

	xmlhttp.onload = function() {
		console.log(this.responseText);
		pastReviews = JSON.parse(this.responseText);
		console.log(pastReviews);
		pastReviews.innerHTML = reviews.map((review) => 
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
	xmlhttp.send();

}
