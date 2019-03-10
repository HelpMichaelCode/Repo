// search button
function search(){
  var keyword = document.getElementById("search").value;
  window.location.href = "http://localhost:9000/product-list?keyword=" + keyword;
}
