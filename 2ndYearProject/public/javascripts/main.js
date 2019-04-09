// search function
function search(){
  var word = document.getElementById("search").value;
  var keyword = word.toLowerCase();
  window.location.href = "http://localhost:9000/product-list?keyword=" + keyword;
}

$('#search').keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
  if(keycode == '13'){  //Enter is equal to 13 in ASCII so this function is executed whenever user presses Enter
                        // with the cursor positioned inside the search textbox
		search();	
	}
});


// var myIndex = 0;
// carousel();

// function carousel() {
  
//   var i;
//   var x = document.getElementsById("mySlides");
//   for (i = 0; i < x.length; i++) {
//     x[i].style.display = "none";  
//   }
//   myIndex++;
//   if (myIndex > x.length) {myIndex = 1}    
//   x[myIndex-1].style.display = "block";  
//   setTimeout(carousel, 4000); // Change image every 2 seconds
// }

function showCart() {
  window.location.href = "http://localhost:9000/show-cart";
}

function confirmDel() {
  if(!confirm("Are you sure you want to delete?")){
    event.preventDefault();
  }
}

$(document).ready(function(){
  $(window).on('scroll',function(){
      if($(window).scrollTop()){
          $(".sub-header").addClass('active');
      }else{
          $(".sub-header").removeClass('active');
      }
  });
});