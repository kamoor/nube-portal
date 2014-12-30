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
			<div class="bx-center">
				<ul class="posts">

					<li class="post text login">
						<div class="post-head clearfix">
							<h1>Login</h1>
						</div>
						<p>
					    <div id="msg"></div>
						<form class="loginForm" id="loginForm" method="post" onsubmit="return false;">
							Email address <input type="text" id="email" class="reg-input txt" />
							Password <input type="password" id="pwd" class="reg-input pwd" />
							<div class="reg-button right">
								<a href="/auth/reset-password">Forgot password</a>
								<input type="submit"  id="login" name="login" value="Login"/>
							</div>
						</form>
						</p>
						<p class="right">
							Nube is free to <a href="/auth/register">register </a> and give a try.
						</p>
					</li>
				</ul>
			</div>

		</div>

		<jsp:include page="common/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="common/footer-include.jsp"></jsp:include>
	<script type="text/javascript" src="/~core/js/idm/auth.js"></script>
</body>
</html>