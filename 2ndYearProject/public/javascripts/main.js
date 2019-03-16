// search button
function search(){
  var keyword = document.getElementById("search").value;
  window.location.href = "http://localhost:9000/product-list?keyword=" + keyword;
}

//redirects to individual product page
$('tr[data-href]').on("click", function() {
  document.location = $(this).data('href');
});