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
    // Initial check for IBT notifications when the page loads
    checkIBTNotifications();

    // Check if there are pending IBT notifications on page load
    if (${sessionScope.ibtNotifications}) {
        addNotificationSymbol(true);
    }

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
    </style>
    </head>
    <body>
        <h1>Choose an Option</h1>
        <form action="IBTServlet" method="post">
        <input type="submit" value="Send IBT" name="IBT_switch">
        <input type="submit" value="IBT_Requests" name="IBT_switch">
        </form>
    </body>
</html>
