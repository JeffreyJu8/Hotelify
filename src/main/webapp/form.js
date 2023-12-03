function submitForm(event) {
  log.textContent = 'Review Submitted!';

  event.preventDefault();

  var stars;

  if (document.getElementById('star5').checked == true) {
    stars = 5;
  }
  else if (document.getElementById('star4').checked == true) {
    stars = 4;
  }
  else if (document.getElementById('star3').checked == true) {
    stars = 3;
  }
  else if (document.getElementById('star2').checked == true) {
    stars = 2;
  }
  else if (document.getElementById('star1').checked == true) {
    stars = 1;
  }
  else {
    stars = 0;
  }
  document.getElementById(rating).value = stars;
  
  var params = new URLSearchParams(window.location.search);
  
  var hotelID = params.get('hotelID');
  
  var formData = new FormData();
  formData.append('hotelID', hotelID);
  formData.append('rating', stars);
  formData.append('comment', document.getElementById('comment').value);
  formData.append('clean', document.getElementById('clean').value);
  formData.append('pet-friendly', document.getElementById('pet-friendly').value);
  formData.append('eco-friendly', document.getElementById('eco-friendly').value);
  formData.append('dirty', document.getElementById('dirty').value);
  formData.append('eco-friendly', document.getElementById('eco-friendly').value);
  formData.append('missing-amenities', document.getElementById('missing-amenities').value);
  formData.append('hidden-fees', document.getElementById('hidden-fees').value);
  formData.append('poor-service', document.getElementById('poor-service').value);
  
  
  
  fetch('submit-review', {
	  method: 'post',
	  body: formData
  })
  .then(response => response.json());
}





const form = document.getElementById("form");
const log = document.getElementById("form-body");
form.addEventListener("submit", submitForm);


