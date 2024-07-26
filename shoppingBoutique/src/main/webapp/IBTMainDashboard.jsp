<%-- 
    Document   : IBTMainDashboard
    Created on : 16 Jul 2024, 11:26:36 AM
    Author     : carme
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>IBT Main Dashboard Page</title>
          <!-- Include jQuery library -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>
        $(document).ready(function() {
        // Function to handle clicking on "Delivered IBT" button
        $("input[value='Delivered IBT']").click(function(e) {
            e.preventDefault(); // Prevent the default form submission
            $("#popup-form").show(); // Show the popup form
        });

        // Function to handle clicking on the close button
        $("#close-btn").click(function() {
            $("#popup-form").hide(); // Hide the popup form
        });
        
     
    
    // Initial check for IBT notifications when the page loads
    checkIBTNotifications();

    

    // Function to handle clicking on "IBT Requests" button
    $("input[value='IBT Requests']").click(function(e) {
        e.preventDefault(); // Prevent the default form submission
        fetchIBTNotifications();
        setInterval(fetchIBTNotifications, 5000); // Fetch notifications every 5 seconds
    });

    // Function to fetch IBT notifications using AJAX
    function fetchIBTNotifications() {
        $.ajax({
            url: "IBTServlet",
            type: "POST",
            data: { action: "handleIBTNotification" },
            success: function(response) {
                if (response === "true") {
                    addNotificationSymbol(true); // True: Red dot
                } else {
                    addNotificationSymbol(false); // False: Blue dot
                }
            },
            error: function(xhr, status, error) {
                console.error("Error checking IBT notifications:", error);
            }
        });
    }

    // Function to add or remove notification symbol based on boolean value
    function addNotificationSymbol(hasNotifications) {
        var ibtRequestsButton = $("input[value='IBT Requests']");
        ibtRequestsButton.removeClass("notification-red notification-blue"); // Remove existing classes

        if (hasNotifications) {
            ibtRequestsButton.addClass("notification-red"); // Add red dot class
        } else {
            ibtRequestsButton.addClass("notification-blue"); // Add blue dot class
        }
    }

    // Function to initially check for IBT notifications when page loads
    function checkIBTNotifications() {
        $.ajax({
            url: "IBTServlet",
            type: "POST",
            data: { action: "handleIBTNotification" },
            success: function(response) {
                if (response === "true") {
                    addNotificationSymbol(true);
                } else {
                    addNotificationSymbol(false);
                }
            },
            error: function(xhr, status, error) {
                console.error("Error checking IBT notifications:", error);
            }
        });
    }
    
    // Check if there are pending IBT notifications on page load
            if (${sessionScope.ibtNotifications}) {
                addNotificationSymbol(true);
            }
});
    </script>

    <style>
    /* CSS style for red dot */
    .notification-red::after {
    content: '';
    display: inline-block;
    width: 8px;
    height: 8px;
    background-color: red;
    border-radius: 50%;
    margin-left: 5px;
    vertical-align: middle;
    }

    /* CSS style for blue dot */
    .notification-blue::after {
    content: '';
    display: inline-block;
    width: 8px;
    height: 8px;
    background-color: blue;
    border-radius: 50%;
    margin-left: 5px;
    vertical-align: middle;
    }

 /* CSS for popup form */
      #popup-form {
          display: none; /* Hidden by default */
          position: fixed;
          left: 0;
          top: 0;
          width: 100%;
          height: 100%;
          background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
          z-index: 1000;
          justify-content: center;
          align-items: center;
         
      }

      #popup-form .form-content {
          background: white;
          padding: 20px;
          border-radius: 5px;
          width: 300px;
          position: relative;
      }

      #popup-form .form-content #close-btn {
         position: absolute;
          top: 10px;
          right: 10px;
          cursor: pointer;
          font-size: 20px; /* Size for the close button */
      }
    </style>
    </head>
    <body>
        <h1>Choose an Option</h1>
        <form action="IBTServlet" method="post">
        <input type="submit" value="Send IBT" name="IBT_switch">
        <input type="submit" value="IBT_Requests" name="IBT_switch">
        </form>
        
        <form>
           <input type="button" value="Delivered IBT">
        </form>
        
         <!-- Popup Form -->
    <div id="popup-form">
        <div class="form-content">
            <span id="close-btn">&times;</span>
            <form action="IBTServlet" method="post">
                <label for="ibt-id">IBT ID:</label>
                <input type="text" id="layaway-id" name="ibt-id" required>
                <br><br>
                <input type="submit" value="Send SMS" name="IBT_switch">
            </form>
        </div>
    </div>
    </body>
</html>
