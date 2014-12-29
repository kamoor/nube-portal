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
							<h1>API Management</h1>
						</div>
						<p>
						<div id="content">
							<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
								<li class="active"><a href="#api-subscription"
									data-toggle="tab">API Subscriptions</a></li>
								<li><a href="#api-store" data-toggle="tab">API Store</a></li>
								<li><a href="#api-usage" data-toggle="tab">API Usage</a></li>
							</ul>

							<div id="my-tab-content" class="tab-content">
								<div class="tab-pane active" id="api-subscription">
									<h3>You are subscribed to following API-s</h3>
									<p>
									<div id="api-list-div">
									</div>
									<!-- This is a hidden template to render the list, see api-admin.js for more -->
									<div class="hidden" id="api-list-template">
											<div class="alert alert-info" role="alert" >
												<h4>{{title}}</h4>
												<p>{{help}}</p>
												<button type="button" class="btn btn-info api-admin-btn" data-toggle="modal"
													data-target="#apiAdmin" apiId='{{id}}'>admin</button>
												<button type="button" class="btn btn-info" data-toggle="modal"
													data-target="#apiHelp">help</button>
												<label class='btn btn-warning'>{{price}} USD {{priceTerm}}</label>
												<button type="button" class="btn btn-primary pull-right"
													id="create-context-btn" data-toggle="modal"
													data-target="#apiUnsubscribe">unsubscribe</button>
											</div>
									</div>
								</div>
								<div class="tab-pane" id="api-store">
									<div class="row"></div>
									<div class="tab-pane" id="app-preview">
										<h3>API-s available to subscribe</h3>
										<p>
											Coming soon
										</p>
								</div>
								<div class="tab-pane" id="api-usage">
									<div class="row"></div>
									<div class="tab-pane" id="app-preview">
										<h3>Monthly usage summary</h3>
										<p></p>
									</div>
								</div>
							</div>
							<!-- Popup to render api admin -->
							<div class="modal fade" id="apiAdmin" tabindex="-1" role="dialog"
								aria-labelledby="basicModal" aria-hidden="true">
							</div>
							
							
							<!-- Template to render api admin popup -->
							<div id="api-admin-template" class="hidden">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">X</button>
											<h4 class="modal-title" id="myModalLabel">API Administration</h4>
											<p>
												<div id="api-name">{{error}}</div>
												<div id="api-name">{{name}}</div>
											</p>
										</div>
										<div class="modal-body">
											<div id="msg-api-admin"></div>
											<!-- This ul will get populated by javascript based on schema.json  -->
											<ul id="api-admin-tabs" class="nav nav-tabs" data-tabs="tabs">
											{{#methods}}
												<li id=li-{{index}}><a href='#api-tab-{{index}}' data-toggle='tab'>{{name}}</a></li>
											{{/methods}}
											</ul>
											<div id="api-admin-tab-content" class="tab-content">
												<div class="tab-pane" id=api-tab-0></div>
												<div class="tab-pane" id=api-tab-1></div>
												<div class="tab-pane" id=api-tab-2></div>
												<div class="tab-pane" id=api-tab-3></div>
												<div class="tab-pane" id=api-tab-4></div>
												<div class="tab-pane" id=api-tab-5></div>
												<!-- Create all hidden div for each method -->
												{{#methods}}
												<div class="hidden" id="api-tab-content-{{index}}">
													Description: {{description}}<br/>
													URI: {{uri}}
														{{#request.properties}}
															<p>
																<span class="api-admin-hrd1">{{description}}</span><br/>
																{{#readonly}}	
																	<input type=input id="{{index}}_req_{{name}}" param="{{name}}" value="{{default}}" readonly/>
																{{/readonly}}
																{{^readonly}}
																     <!--  check to see if this object has second level inner properties -->
																    {{#properties}}
																        <div class="api-admin-box-2">
																	         <span class="api-admin-hrd2">{{description}}</span><br/>
																	         <input type=input id="{{index}}_req_{{name}}" param="{{name}}" value="{{default}}" param-type="{{type}}"/>
																         </div>
																           <!-- try to find third level objects -->
																          {{#properties.properties}}
																	        <div class="api-admin-box-3">
																		         <span class="api-admin-hrd3">{{description}}</span><br/>
																		         <input type=input id="{{index}}_req_{{name}}" param="{{name}}" value="{{default}}" param-type="{{type}}"/>
																	         </div>
																  		  {{/properties.properties}}
																    {{/properties}}
																    {{^properties}}
																		<input type=input id="{{index}}_req_{{name}}" param="{{name}}" value="{{default}}"/>
																	{{/properties}}
																{{/readonly}}
															</p>
														{{/request.properties}}
													<button type=button class="btn btn-primary invoke-api-btn-class" id="invoke-api-btn-{{index}}" methodIndex="{{index}}" uri="{{uri}}" httpMethod="{{httpMethod}}">Invoke API</button>	
													
												</div>
												{{/methods}}
												<!-- end hidden div-->
												<hr/>
												<h6>Response</h6>	
												<div id="api-response-content"></div>
											</div>
										</div>
										<div class="modal-footer">
											<!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
										</div>
									</div>
								</div>
							</div>
							<!-- End api popup -->
							<!-- Popup to render api help -->
							<div class="modal fade" id="apiHelp" tabindex="-1" role="dialog"
								aria-labelledby="basicModal" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">X</button>
											<h4 class="modal-title" id="myModalLabel">API
												documentation</h4>
										</div>
										<div class="modal-body">
											<p>No documentation found</p>
										</div>
										<div class="modal-footer"></div>
									</div>
								</div>
							</div>
							<!-- End api popup -->
							<!-- Popup to render api help -->
							<div class="modal fade" id="apiUnsubscribe" tabindex="-1"
								role="dialog" aria-labelledby="basicModal" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">X</button>
											<h4 class="modal-title" id="myModalLabel">Unsubsribe
												from API</h4>
										</div>
										<div class="modal-body">You may loose some capabilities
											in you app by unsubscribing from this API. Please confirm</div>
										<div class="modal-footer">
											<!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
											<button type="button" class="btn btn-primary"
												id="unsubscribe-confirm-btn">Confirm</button>
										</div>
									</div>
								</div>
							</div>
							<!-- End api help -->
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
<script src="/~core/js/api/api-admin.js"></script>
</html>