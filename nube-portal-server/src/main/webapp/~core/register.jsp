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
							<h1>Create new account</h1>
						</div>
						<p>
						<div id="msg"></div>
						<form class="regform" id="regForm" method="post" onsubmit="return false;">
						 <fieldset>
								First name<span class="mandatory">*</span> <input type="text"  id="fn" class="reg-input txt"/>
								Last name<span class="mandatory">*</span> <input type="text"  id="ln" class="reg-input txt"/>
								Email address<span class="mandatory">*</span><input type="email" id="email" class="reg-input txt"/>
								Password<span class="mandatory">*</span> <input type="password" id="pwd"  class="reg-input pwd" />
								Re-enter password<span class="mandatory">*</span> <input type="password" id="re-pwd"  class="reg-input pwd"/>
								Business category<span class="mandatory">*</span> 
								<select id="biz" class="reg-input txt">
								  <option value="personal">Personal</option>
								  <option value="small-restaurant">Business - Restaurant</option>
								  <option value="small-restaurant">Business - Retail Shops</option>
								  <option value="small-restaurant">Business - Other</option>
								</select>
							<div class="reg-button right">
								<input type="submit"  id="signup" name="signup" value="Sign up"/>
							</div>
						 </fieldset>
						</form>
						
						</p>
						<p class="right">
							<a href="/auth/login">Log in</a> to existing account 
						</p>
					</li>
				</ul>
			</div>

		</div>
		<jsp:include page="common/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="common/footer-include.jsp"></jsp:include>
	<script type="text/javascript" src="/~core/js/idm/auth.js"></script>
	<script type="text/javascript" src="/~core/js/idm/register.js"></script>
</body>

</html>