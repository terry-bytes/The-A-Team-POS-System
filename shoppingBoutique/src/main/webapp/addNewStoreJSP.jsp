<%-- 
    Document   : addNewStoreJSP
    Created on : 11 Jul 2024, 2:33:15 PM
    Author     : carme
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Store Page</title>
    </head>
    <body>
        <h1>Add a new Store</h1>
        <form action="addNewStoreServlet" method="post">
            <label>Enter the new store name</label>
            <input type="text" value="storeName"><br><br>
                   
            <label>Enter the new store address</label>
            <input type="text" value="storeAddress"><br><br>
            
            <label>Enter the new store city</label>
            <input type="text" value="storeCity"><br><br>
            
            <label>Enter the new store province</label>
            <input type="text" value="storeProvince"><br><br>
            
            <label>Enter the new store zipcode</label>
            <input type="text" value="storeZipcode"><br><br>
            
            <label>Enter the new store phone</label>
            <input type="text" value="storePhone"><br><br>
            
            <label>Enter the new store email address</label>
            <input type="text" value="storeEmailAddress"><br><br>
            
            <input type="submit" value="Add_Store" name="storeSwitch">
        </form>
    </body>
</html>
