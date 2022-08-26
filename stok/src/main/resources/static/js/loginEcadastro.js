console.log("iniciou")
$('.message a').click(function(){
   $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
   console.log("rodou")
});