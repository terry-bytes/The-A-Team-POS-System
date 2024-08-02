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
           String message = (String) request.getAttribute("message");
        %>
        <jsp:include page="navbar.jsp" />
        <div class="container">
            <div class="store-form">
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
                    <div class="input-box">
                        <input type="text"
                               placeholder='Branch Contact number'
                               name='storePhone'
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
                    
                    <div class="input-submit">
                        <input name="submit" value="Submit_Store" hidden>

                        <button class="submit-btn" id="submit">Add Store</button>

                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
