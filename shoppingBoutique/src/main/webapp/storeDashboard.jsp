<%-- 
    Document   : storeDashboard
    Created on : 12 Jul 2024, 10:01:21 AM
    Author     : carme
--%>

<%@page import="java.util.List"%>
<%@page import="ateam.Models.Store"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Store Dashboard Page</title>
    </head>
    <body>
        <h1>Store Dashboard</h1>
        <h2>Choose an option Chapati</h2>
        <h1>Add a new Store</h1>
        <form action="StoreServlet" method="post">
            <label>Enter the new store name</label>
            <input type="text"  name="storeName"><br><br>
                   
            <label>Enter the new store address</label>
            <input type="text"  name="storeAddress"><br><br>
            
            <label>Enter the new store city</label>
            <input type="text"  name="storeCity"><br><br>
            
            <label>Enter the new store province</label>
            <input type="text"  name="storeProvince"><br><br>
            
            <label>Enter the new store zipcode</label>
            <input type="text"  name="storeZipcode"><br><br>
            
            <label>Enter the new store phone</label>
            <input type="text"  name="storePhone"><br><br>
            
            <label>Enter the new store email address</label>
            <input type="text"  name="storeEmailAddress"><br><br>
            
            <input type="submit" value="Submit_Store" name="storeSwitch"><br><br>
           
            <p><%=request.getAttribute("message")%></p>
           
        </form>
            
            <h1>Update a Store</h1>
        <form action="StoreServlet" method="post">
            <label>Enter store ID to update</label>
            <input type="text"  name="storeIDUpdate"><br><br>
            
            <label>Enter the new store name</label>
            <input type="text"  name="storeName"><br><br>
                   
            <label>Enter the new store address</label>
            <input type="text"  name="storeAddress"><br><br>
            
            <label>Enter the new store city</label>
            <input type="text"  name="storeCity"><br><br>
            
            <label>Enter the new store province</label>
            <input type="text"  name="storeProvince"><br><br>
            
            <label>Enter the new store zipcode</label>
            <input type="text"  name="storeZipcode"><br><br>
            
            <label>Enter the new store phone</label>
            <input type="text"  name="storePhone"><br><br>
            
            <label>Enter the new store email address</label>
            <input type="text"  name="storeEmailAddress"><br><br>
            
            <input type="submit" value="Update_Store" name="storeSwitch"><br><br>
           
            <p><%=request.getAttribute("message")%></p>
           
        </form>
 
        <form action="StoreServlet" method="post">
            <h2>Search for a store using Store ID</h2>
            <input type="text" name="storeID"><br><br>  
            <input type="submit" value="Search_Store" name="storeSwitch"><br><br>
            <% Store store = (Store)request.getAttribute("Store");
           if(store != null) {
            %>
            <p><%=store.getStore_name()%></p>
            <%}%>
            
        </form>
        
        <form action="StoreServlet" method="post">
            <h2>All Stores on the System</h2>
            <input type="submit" value="View_Stores" name="storeSwitch">
             <% 
        List<Store> stores = (List<Store>) request.getAttribute("stores");
        if (stores != null && !stores.isEmpty()) {
            for (Store s : stores) {
    %>
    <div style="border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;">
        <h2>Store ID: <%= s.getStore_ID() %></h2>
        <p>Name: <%= s.getStore_name() %></p>
        <p>Address: <%= s.getStore_address() %></p>
        <p>City: <%= s.getStore_city() %></p>
        <p>Province: <%= s.getStore_province() %></p>
        <p>Zip Code: <%= s.getStore_zipcode() %></p>
        <p>Phone: <%= s.getStore_phone() %></p>
        <p>Email: <%= s.getStore_email() %></p>
    </div>
    <% 
            }
        } else {
    %>
    <p>No stores found.</p>
    <% } %>
        </form>
            
        <form action="StoreServlet" method="post">
            <h2>Delete a store from system using store ID</h2>
            <input type="text" name="StoreIDDelete"><br><br>
            <input type="submit" value="Delete_Store" name="storeSwitch">
            <p><%=request.getAttribute("message")%></p>
        </form>
    </body>
</html>
