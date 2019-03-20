// search button
function search(){
  var keyword = document.getElementById("search").value;
  window.location.href = "http://localhost:9000/product-list?keyword=" + keyword;
}

//redirects to individual product page
$('tr[data-href]').on("click", function() {
  document.location = $(this).data('href');
});


carousel();

function carousel() {
  var myIndex = 0;
  var i;
  var x = document.getElementsByClassName("mySlides");
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";  
  }
  myIndex++;
  if (myIndex > x.length) {myIndex = 1}    
  x[myIndex-1].style.display = "block";  
  setTimeout(carousel, 2000); // Change image every 2 seconds
}

function displayCart() {
  window.location.href = "http://localhost:9000/show-cart";
}