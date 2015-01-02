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
<body id="" class="">
	<jsp:include page="common/header.jsp"></jsp:include>
	<div id="container">
		<div id="main">
			<jsp:include page="common/sidenav.jsp"></jsp:include>
			<div>
				<ul class="posts">

					<li class="post text">
						<div class="post-head clearfix">
							<h1>Settings</h1>
						</div>

						<p>
						<div id="content">
							<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
								<li class="active"><a href="#site-settings"
									data-toggle="tab">Site Settings</a></li>
								<li class="active"><a href="#api-settings"
									data-toggle="tab">API Settings</a></li>
								<li><a href="#personal-settings" data-toggle="tab">Personal
										Settings</a></li>
								<li><a href="#adv-settings" data-toggle="tab">Advanced
										Settings</a></li>
							</ul>
							<div id="my-tab-content" class="tab-content">
								<div class="tab-pane active" id="site-settings">
									<h3>TBD</h3>
									

								</div>
								<div class="tab-pane active" id="api-settings">
									<h3>TBD</h3>
									

								</div>
								<div class="tab-pane" id="personal-settings">
									
									<p>
									
									<form name="basicInfoForm" method="post"
										onsubmit="return false;">
										<div>
											<label>First name</label> 
											<input type="text" id="fName" class="reg-input txt" />
										</div>
										<div>
											<label>Last name</label>
											<input type="text" id="lname" class="reg-input txt" />
										</div>
										<div>
											<label>Email(User name)</label>
											<input type="text" id="email" class="reg-input txt" />
										</div>
										<div class="reg-button right">
											<input type="submit" id="basicinfo"
												name="basicinfo" value="Update Personal Information" />
										</div>
									</form>
									<div class="post-head clearfix"></div>
									<div id="msg"></div>
									<form name="changePwdForm" method="post"
										onsubmit="return false;">
										<div>
											<label>Current password</label> 
											<input type="password" id="currentPwd" class="reg-input txt" />
										</div>
										<div>
											<label>New password</label>
											<input type="password" id="newPwd" class="reg-input txt" />
										</div>
										<div class="reg-button right">
											<input type="submit" id="changepwd"
												name="resetpwd" value="Change Password" />
										</div>
									</form>
									</p>
									<div class="post-head clearfix"></div>
								</div>
								<div class="tab-pane" id="adv-settings">
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
<script type="text/javascript" src="/~core/js/idm/personal-settings.js"></script>
</body>

</html>