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
<body>
	<jsp:include page="common/header.jsp"></jsp:include>
	<div id="container">
		<div id="main">
			<jsp:include page="common/sidenav.jsp"></jsp:include>
			<div>
				<ul class="posts">

					<li class="post text">
						<div class="post-head clearfix">
							<h1>Application Management</h1>
						</div>

						<div id="content">
							<div id="msg"></div>
							<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
								<li class="active"><a href="#app-versions"
									data-toggle="tab">App info & settings</a></li>
								<li><a href="#app-upload" data-toggle="tab">Deploy new version</a></li>
							</ul>

							<div id="my-tab-content" class="tab-content">
								<div class="tab-pane active" id="app-versions">
								    
									App id:  <span id="app-id"></span><br/>
									Active version:  <span id="app-curr-version">Not available</span>
									
									<div class="right">
			  							  <a href="#" class="btn btn-lg btn-danger"  data-toggle="modal" data-target="#delete-app-modal">Delete app</a>
 									</div>
 									<p>
									<div class="post-head clearfix"><h5>Version History</h5></div>
									
									<ul id="versions">
									</ul>
									</p>
								</div>

								<div class="tab-pane" id="app-upload">
									<p>
										Upload new version of application and go to <b>App Preview</b>
										to validate and activate it.
									</p>

									<form method="post" enctype="multipart/form-data"
										name="appUploadForm" id="appUploadForm"
										onsubmit="return false;">
										<p>
											<label for="app"></label><input type="file" name="app"
												id="app" />
										</p>
										<input type="button" name="submitApp" id="submitApp"
											value="Upload" />
									</form>

									<p>
										Learn how to <a href="/admin/help/apps">build an
											application</a>
									</p>
									
									<!-- This is only for local setup -->
								<jsp:include page="common/local-update-app.jsp"></jsp:include>
	
									<!-- Local setup -->
								</div>



							</div>
						</div>
					</li>

				</ul>
			</div>

		</div>
		<div class="modal fade" id="delete-app-modal" tabindex="-1" role="dialog" aria-labelledby="delete-app-modal" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
	            <h4 class="modal-title" id="myModalLabel">Delete App</h4>
	            </div>
	            <div class="modal-body">
	                <div id="msg-app"></div>
	                <p class="btn-danger"><b>BE CAREFUL..You are about to delete <span id="app-to-be-deleted"></span></b><br/><br/>
	                Once you delete your app, there is no going back. You will loose all app specific contents, history and data.<br/>
	                </p>
	            </div>
	            <div class="modal-footer">
	                <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
	                <button type="button" class="btn btn-danger" id="delete-app-btn">Confirm</button>
	        </div>
	    </div>
	  </div>
	</div>

		<jsp:include page="common/footer.jsp"></jsp:include>
	</div>
<jsp:include page="common/footer-include.jsp"></jsp:include>
<jsp:include page="common/footer-secured-include.jsp"></jsp:include>
<script type="text/javascript" src="/~core/js/apps/manageapps.js"></script>
<script type="text/javascript" src="/~core/js/apps/context.js"></script>
</body>
</html>