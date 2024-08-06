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
    
    <script>
        $(document).ready(function() {
    // Initially hide the popup form
    $("#popup-form").hide();

    // Function to handle clicking on "Delivered IBT" button
    $("input[value='Delivered IBT']").click(function(e) {
        e.preventDefault(); // Prevent the default form submission
        $("#popup-form").show(); // Show the popup form
    });

    // Function to handle clicking on the close button
    $("#close-btn").click(function() {
        $("#popup-form").hide(); // Hide the popup form
    });

    // Additional functionality (notifications etc.) remains unchanged

    // Function to handle clicking on "IBT Requests" button
    $("input[value='IBT Requests']").click(function(e) {
        e.preventDefault(); // Prevent the default form submission
        fetchIBTNotifications();
        setInterval(fetchIBTNotifications, 5000); // Fetch notifications every 5 seconds
    });

    
});

        </script>

   <style>
        /* General Styles */
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f2f5;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column; /* Stack elements vertically */
            align-items: center;
            min-height: 100vh; /* Ensure the body takes full height */
        }

        h1 {
            color: #0056b3;
            font-size: 2em;
            margin-top: 20px;
            text-align: center;
            font-weight: 600;
            width: 100%; /* Ensure full width for centering */
        }

        /* Container for horizontal forms */
        .form-container {
            display: flex;
            justify-content: center; /* Center the forms horizontally */
            align-items: center; /* Center the forms vertically */
            gap: 10px; /* Reduce the space between forms */
            width: 100%; /* Full width container */
            max-width: 1000px; /* Adjust as needed */
            margin: 20px 0; /* Spacing above and below the container */
        }

        /* Form Styles */
        form {
            padding: 10px 20px; /* Reduce padding */
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        input[type="submit"], input[type="button"] {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px; /* Adjust button size */
            font-size: 14px; /* Adjust font size */
            border-radius: 6px;
            cursor: pointer;
            margin: 5px; /* Reduce margin around buttons */
            transition: background-color 0.3s, transform 0.2s;
        }

        input[type="submit"]:hover, input[type="button"]:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }

        input[type="submit"]:active, input[type="button"]:active {
            background-color: #003d7a;
            transform: scale(1);
        }

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
        
        
        /*---------------------------------------------*/
        
        
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
    display: flex; /* Use flexbox to center content */
    justify-content: center; /* Center horizontally */
    align-items: center; /* Center vertically */
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
       
        <a href="managerDashboard.jsp">
        <h1>Choose an Option</h1>
        </a>
        <div class="form-container">
        <form action="IBTServlet" method="post">
        <input type="submit" value="Send IBT" name="IBT_switch">
        </form>
        
        <form action="IBTServlet" method="post">
            <input type="submit" value="IBT-Requests" name="IBT_switch">
        </form>
        
        <form>
           <input type="button" value="Delivered IBT">
        </form>
            
        </div>
          
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
