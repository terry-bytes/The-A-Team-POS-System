<%-- 
    Document   : salesItems
    Created on : 05 Aug 2024, 13:11:05
    Author     : T440
--%>

<%@page import="ateam.Models.SalesItem"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
            *{
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Poppins', sans-serif;
            }

            body{
                background-image: linear-gradient(to right, #277af6, #0098f7, #48b2ef, #85c7e5, #bfd9e0);
            }
            .emp-tb {
            font-family: sans-serif;
            width: 100%;
            background-color: rgb(255, 255, 255);
            border-collapse: collapse;
            font-size: 1em;
            margin: 20px 0;
            border-radius: 5px 5px 0 0;
            overflow: hidden;
        }
        .emp-tb thead tr{
            font-family: sans-serif;
            background-color: #007bff;
            color: #fff;
            text-align: left;
            padding-top: 12px;
            padding-bottom: 12px;
        }
        
        .emp-tb th,
        .emp-tb td{ 
            padding: 12px 15px;
        }
        .emp-tb tbody tr{
            border-bottom: 1px solid #ddd;
        }
        .emp-tb tbody tr:nth-of-type(even){
            background-color: #f3f3f3;
        }
        .emp-tb tbody tr:last-of-type{
            border-bottom: 2px solid #007bff;
        }
        .emp-tb .icon{
            min-width: 60px;
            border-radius: 6px;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            color: #5ba4e7;
            transition: 0.3s ease;
        }
        .icon-button {
            background: none;
            border: none;
            cursor: pointer;
        }

        .icon-button i {
            font-size: 1.2em;
        }
        .emp-tb tbody tr:hover{
            color: #007bff;
            font-weight: 400;
        }
        </style>
    </head>
    <body>
        <% Employee  employee = (Employee) request.getSession(false).getAttribute("Employee");
            List<SalesItem> sales = (List<SalesItem>) request.getSession(false).getAttribute("SalesItems");
            %>
            <div class="manager-container">
        <div>
            <jsp:include page="sidebar.jsp"></jsp:include>
        </div>
        <div class="main">
            <div class="login-box">
                <div class="login-header">
                    <h3>View sales Item</h3>
                </div>
               
                <div class="two-forms">
                    <% if (sales != null && !sales.isEmpty()){%>
                    <table class="emp-tb">
        <thead>
                <tr>
                    <th>Sales Item Id</th>
                    <th>Sales Id</th>
                    <th>Product Id</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                    
                </tr>
            </thead>
            <tbody>
                <% for(SalesItem item : sales){%>
                <tr>
                    <td><%= item.getSales_item_ID() %></td>
                    <td><%= item.getSales_ID() %></td>
                    <td><%= item.getProduct_ID() %></td>
                    <td><%= item.getQuantity() %></td>
                    <td><%= item.getUnit_price() %></td>
                </tr>
                <%}%>
        </table><%}%>S
                </div>
                     
            </div>
        </div>
    </div>
    </body>
</html>
