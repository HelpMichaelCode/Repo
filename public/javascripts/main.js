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

function showCart() {
  window.location.href = "http://localhost:9000/show-cart";
}

function confirmDel() {
  if(!confirm("Are you sure you want to delete?")){
    event.preventDefault();
  }
}


var logID = 'log',
  log = $('<div id="'+logID+'"></div>');
$('body').append(log);
  $('[type*="radio"]').change(function () {
    var me = $(this);
    log.html(me.attr('value'));
  });


  //showpassword
  function myFunction() {
    var x = document.getElementById("pass");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
  };
  function myFunction2() {
    var x = document.getElementById("pass2");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
  };
  function myFunction3() {
    var x = document.getElementById("pass3");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
  };

  function notifyTimeLimit() {
    alert("You may cancel an order within the next 5 minutes!");
  }