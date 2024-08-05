<%@page import="ateam.DTO.SalesDTO"%>
<%@page import="ateam.Models.Sale"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Barcode Scanner</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/store.css" />
          <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report.css">
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            List<Employee> employees = (List<Employee>) request.getSession(false).getAttribute("employees");
            List<SalesDTO> sales = (List<SalesDTO>) request.getSession(false).getAttribute("myStoreSales");
           String message = (String) request.getAttribute("message");
        %>
        <jsp:include page="sidebar.jsp" />
        <div class="container">
            <div class="store-form" style="display: none;">
                <div class="heading">
                    <h4>Add Store</h4>
                    <% if(message != null){%>
                    <p> <%=message%></p>
                       <% }%>
                </div>
                <form action="StoreServlet" method="post" class="">
                    <div class="input-box">
                        <input type="text"
                               placeholder='Branch'
                               name='storeName'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                  
                    <div class="input-box">
                        <input type="email"
                               placeholder='Branch email address'
                               name='storeEmailAddress'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    
                    <div class="input-box">
                        <input type="text"
                               placeholder='Branch Contact number'
                               name='storePhone'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    
                    <div class="input-box">
                        <input type="text"
                            placeholder='Address'
                            name='storeAddress'
                            class='input-field'
                            autocomplete="on" required
                        />

                    </div>
                    
                    <div class="input-box">
                        <input type="text"
                            placeholder='City'
                            name='storeCity'
                            class='input-field'
                            autocomplete="on" required
                            />
                         
                    </div>
                    
                    <div class="input-box">
                        <input type="text"
                            placeholder='province'
                            name='storeProvince'
                            class='input-field'
                            autocomplete="off" required
                            />
                         
                    </div>
                    
                    <div class="input-box">
                        <input type="text"
                               placeholder='Zipcode'
                               name='storeZipcode'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    
                    <div class="input-submit">
                        <input name="submit" value="Submit_Store" hidden>

                        <button class="submit-btn" id="submit">Add Store</button>

                    </div>
                </form>
            </div>
                
                <% if (sales != null && !sales.isEmpty()){%>
                    <table >
        <tr>
          <th>Sale Id</th>
          <th>Teller name</th>
          <th>Payment Method</th>
          <th>Sales Date</th>
          <th>Total Amount</th>
          <th>Action</th>
        </tr>
        <tbody>
            <% for (SalesDTO sale : sales){%>
            <tr>
                <td><%= sale.getSaleId() %></td>
                <td><%= sale.getTeller() %></td>
                <td><%= sale.getPaymentMethod() %></td>
                <td><%= sale.getSalesDate() %></td>
                <td>R <%= sale.getTotalAmount() %></td>
                <td>
                    <form action="AdminServlet" method="post">
                        <input name="saleId" value="<%= sale.getSaleId()%>" type="hidden">
                        <button name="admin_switch" value="viewSaleItems" type="submit" >View Items</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
        </table><% } %>
                
        </div>
    </body>
</html>
