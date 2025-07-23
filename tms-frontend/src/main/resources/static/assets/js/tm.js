    function removeAlertSuccess(){
        $('#alertSuccess').fadeOut(1000, function() {
           $( "#alertSuccess" ).remove();
        });
    }
    window.onload = function () {
         setTimeout(removeAlertSuccess,1800);
    };

