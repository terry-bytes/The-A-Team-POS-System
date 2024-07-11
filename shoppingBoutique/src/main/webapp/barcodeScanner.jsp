<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Barcode Scanner</title>
  <!-- Include jQuery for AJAX handling -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
  <h1>Barcode Scanner</h1>
  
  <!-- Video feed from webcam -->
  <div id="interactive" class="viewport">
    <video id="videoElement" width="640" height="480" autoplay></video>
  </div>
  
  <br>
  <button onclick="startScan()">Start Scanning</button>
  <button onclick="stopScan()">Stop Scanning</button>

  <script>
    function startScan() {
      $.ajax({
        url: "BarcodeScanServlet",  // URL mapped to BarcodeScanServlet
        type: "GET",
        success: function(response) {
          console.log("Barcode scanning started.");
          startWebcam(); // Start displaying webcam feed
        },
        error: function(xhr, status, error) {
          console.error("Failed to start barcode scanning: " + error);
        }
      });
    }

    // Function to stop barcode scanning
    function stopScan() {
      $.ajax({
        url: "barcodeScan",  // URL mapped to BarcodeScanServlet
        type: "POST",
        success: function(response) {
          console.log("Barcode scanning stopped.");
          stopWebcam(); // Stop displaying webcam feed
        },
        error: function(xhr, status, error) {
          console.error("Failed to stop barcode scanning: " + error);
        }
      });
    }

    // Start displaying webcam feed
    function startWebcam() {
      navigator.mediaDevices.getUserMedia({ video: true })
        .then(function(stream) {
          var videoElement = document.getElementById('videoElement');
          videoElement.srcObject = stream;
        })
        .catch(function(error) {
          console.error('Error accessing webcam:', error);
        });
    }

    // Stop displaying webcam feed
    function stopWebcam() {
      var videoElement = document.getElementById('videoElement');
      var stream = videoElement.srcObject;
      if (stream) {
        var tracks = stream.getTracks();
        tracks.forEach(function(track) {
          track.stop();
        });
        videoElement.srcObject = null;
      }
    }

    // Optional: Handle response from servlet (if needed)
    $(document).ready(function() {
      // Start scanning when page loads (optional)
      startScan();
    });
  </script>
</body>
</html>
