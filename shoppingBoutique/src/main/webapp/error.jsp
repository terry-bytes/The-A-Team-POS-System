<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        /* Add some basic styling for better presentation */
        body {
            font-family: Arial, sans-serif;
            margin: 50px;
        }
        form {
            width: 300px;
            margin: 0 auto;
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="image"] {
            display: block;
            margin: 20px auto;
            width: 50px;
            height: 50px;
        }
    </style>
</head>
<body>
    <h2>Error occurred</h2>
    <p>${errorMessage}</p>
    <form action =replenishStock.jsp>
    <input type="image" src="button.png" alt="Submit">
    </form>
</body>
</html>
