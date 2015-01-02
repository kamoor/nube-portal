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
								<h1>Help & Support</h1>
							</div>

							<p>
								<div id="content">
								    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
								        <li class="active"><a href="#help" data-toggle="tab">Help</a></li>
								        <li><a href="#support" data-toggle="tab">Support</a></li>
								    </ul>
								    <div id="my-tab-content" class="tab-content">
								        <div class="tab-pane active" id="help">
								            <h3>Nube App</h3>
								     		<p id="help">
								     			<h4>Static App</h4>
									   			An app may contain static files including html, scripts, styles, images, videos etc.
									   			Root folder should also contain a <a href="/~core/sample/lic.key">lic.key</a> to make it active in cloud.
									   			Package all the contents in to a zip file and upload to cloud. 
									   			</p>
									   			<h4>Dynamic App</h4>
									   			<p id="help">
									   			Future version of app cloud will allow server side javascripting to access any http resources(REST).
									   	  </p>
									   			
								        </div>
								        <div class="tab-pane" id="support">
								            <h3>Customer Support</h3>
								            <p>Coming Soon</p>
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
	</body>
<jsp:include page="common/footer-include.jsp"></jsp:include>
<jsp:include page="common/footer-secured-include.jsp"></jsp:include>
</html>