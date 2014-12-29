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
		<jsp:include page="common/header.jsp"></jsp:include>
		<div id="container">
			<div id="main">
				<jsp:include page="common/sidenav.jsp"></jsp:include>
				<div>
					<ul class="posts">
			
						<li class="post text">
							<div class="post-head clearfix">
								<h1>Reports</h1>
							</div>

							<p>
								<div id="content">
								    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
								        <li class="active"><a href="#app-upload" data-toggle="tab">Site Reports</a></li>
								        <li><a href="#app-history" data-toggle="tab">API Reports</a></li>
								        <li><a href="#app-preview" data-toggle="tab">Expense Reports</a></li>
								    </ul>
								    <div id="my-tab-content" class="tab-content">
								        <div class="tab-pane active" id="app-upload">
								            <h3>TBD</h3>
								     		<div id="msg">${msg}</div>
									   			
								        </div>
								        <div class="tab-pane" id="app-history">
								            <h3>TBD</h3>
								            <p></p>
								        </div>
								        <div class="tab-pane" id="app-preview">
								            <h3>TBD</h3>
								            <p>TBD</p>
								        </div>
								      </div>
								</div>
							</p>
						</li>

					</ul>
				</div>

			</div>

		   <jsp:include page="common/footer.jsp"></jsp:include>
		</div>
<jsp:include page="common/footer-include.jsp"></jsp:include>
<jsp:include page="common/footer-secured-include.jsp"></jsp:include>
</body>
</html>