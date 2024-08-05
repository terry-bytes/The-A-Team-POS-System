<!DOCTYPE html>
<html>
<head>
    <title>Add Product Variant</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            font-family: 'Poppins', sans-serif;
            box-sizing: border-box;
        }
        body{
            background: #aecdf0;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            height: 100vh;
        }
        h1{
            font-weight: 600;
            color: #fff;
        }
        
    </style>
</head>
<body>
    <h1>Add Product Variant</h1>
    <form action="productVariants" method="post">
        <input type="hidden" name="action" value="add">
        <label for="productSKU">Product SKU:</label>
        <input type="text" id="productSKU" name="productSKU" required><br>
        <label for="size">Size:</label>
        <input type="text" id="size" name="size" required><br>
        <label for="color">Color:</label>
        <input type="text" id="color" name="color" required><br>
        <label for="storeID">Store ID:</label>
        <input type="number" id="storeID" name="storeID" required><br>
        <input type="submit" value="Add Variant">
    </form>
    <a href="productVariants">Back to list</a>
</body>
</html>
