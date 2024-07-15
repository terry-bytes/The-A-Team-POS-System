<%@ page contentType="text/html" pageEncoding="UTF-8"%>
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

  <h2>Scanned Items</h2>
  <ul id="scannedItems"></ul>
  
   <!-- Audio element for sound notification -->
  <audio id="scanSound" src="path/to/your/soundfile.mp3" preload="auto"></audio>

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
        url: "BarcodeScanServlet",  // URL mapped to BarcodeScanServlet
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

    // Function to add scanned item to the list
    function addScannedItem(product) {
      const scannedItems = document.getElementById('scannedItems');
      const li = document.createElement('li');
      li.textContent = `SKU: ${product.productSKU}, Name: ${product.product_name}, Price: ${product.product_price}, Quantity: ${product.quantity_in_stock}`;
      scannedItems.appendChild(li);
    }

    // Optional: Handle response from servlet (if needed)
    $(document).ready(function() {
      // Handle scanned barcode
      function handleScannedBarcode(barcode) {
        $.ajax({
          url: "BarcodeScanServlet",
          type: "POST",
          data: { barcode: barcode },
          success: function(response) {
            if (response.error) {
              console.error(response.error);
            } else {
              addScannedItem(response);
            }
          },
          error: function(xhr, status, error) {
            console.error("Error fetching product details: " + error);
          }
        });
      }
    });
  </script>
</body>
