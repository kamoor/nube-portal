<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" style="position: relative; -webkit-transition: right 0.25s ease-in-out; transition: right 0.25s ease-in-out; right: 0px;">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Nube administration console</title>
		<!-- css -->
		<jsp:include page="common/header-include.jsp"></jsp:include>
		<meta name="keywords" content="Nube Cloud Administration console">
	</head>
	<body id="" class="">
		
		<div id="container">
		<jsp:include page="common/header.jsp"></jsp:include>
			<div id="main">
			
				<jsp:include page="common/sidenav.jsp"></jsp:include>
				
				<div>
					<ul class="posts">
						<li class="post text">
							<div class="post-head clearfix">
								<h1>Nube administration console</h1>
							</div>

							<p>
								Nube administration console will provide an easy way to manage your application and api-s. You can also view
								various usage reports in this console.
								
							</p>
							<!--   <h5>Applications</h5> -->
							<p>
							 <jsp:include page="common/context.jsp" />
							</p>
							<h5>Usage summary</h5>
							<p>
							 <jsp:include page="common/report-summary-1.jsp" />
							</p>
							<p>
								<img alt="image" src="/~core/img/avatar.png"/>
								<div class="footer"><a href="#">Learn More</a></div>
							</p>
						</li>
					</ul>
				</div>

			</div>

		   <jsp:include page="common/footer.jsp"></jsp:include>
		</div>
		
	</body>
    <jsp:include page="common/footer-include.jsp"></jsp:include>
    <script type="text/javascript" src="/~core/js/idm/secured.js"></script>
	<script type="text/javascript" src="/~core/js/apps/context.js"></script>
</html>