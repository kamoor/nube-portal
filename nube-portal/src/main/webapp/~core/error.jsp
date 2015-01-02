<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	style="position: relative; -webkit-transition: right 0.25s ease-in-out; transition: right 0.25s ease-in-out; right: 0px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Nube administration console</title>
<!-- css -->
<jsp:include page="common/header-include.jsp"></jsp:include>
<meta name="keywords" content="Nube Cloud Administration console">
</head>

<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%
            ErrorData ed =  pageContext.getErrorData();
           	String url = ed.getRequestURI();
            int errorCode = ed.getStatusCode();
            Throwable e = ed.getThrowable();
            String msgHeader = "Something went wrong...";
            if(errorCode == 403){
            	msgHeader = "Access denied";
            }
%>

<body id="" class="">
	<jsp:include page="common/header.jsp"></jsp:include>
	<div id="container">
		<div id="main">
			<div class="bx-center">
				<ul class="posts">

					<li class="post text login">
						<div class="post-head clearfix">
							<h1><%=msgHeader%></h1>
						</div>
						<p>
						 We apologize for the inconvenience. Try again later or Please contact administrator if you don't recognize this error.
						</p>
						<p>
							<hr/>
							Error code: <%=errorCode%>
						</p>
						<p class="right">
							<a href="/admin/login">Log in</a> 
						</p>
					</li>
				</ul>
			</div>

		</div>

		<jsp:include page="common/footer.jsp"></jsp:include>
	</div>
</body>
<jsp:include page="common/footer-include.jsp"></jsp:include>
</html>