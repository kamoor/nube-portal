
<toolbox>
<div class="context-menu">
   <div>
        <div class="right">
			    <a href="#" class="btn btn-lg btn-success"  data-toggle="modal" data-target="#create-app-modal">create new app</a>
 		</div>
   </div>
		<ul id="contexts" class="btn-group">
		</ul>
		
</div>
<div class="modal fade" id="create-app-modal" tabindex="-1" role="dialog" aria-labelledby="create-app-modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
            <h4 class="modal-title" id="myModalLabel">Create a new application</h4>
            </div>
            <div class="modal-body">
                <div id="msg-app"></div>
                <h3>In order to upload a new app, you need to assign a unique id & name first</h3>
                App Id<br/> 
                <input type="input" maxlength="15" id="new-context"></input>
                <p>Maximum 15 characters, each word seperated by "-"(hyphen). For example: nube-noodles</br>
                </p>
                
                App Name<br/> 
                <input type="input" maxlength="25" id="context-descr"></input>
                <p>Maximum 25 characters. For example: Nube Noodles Inc</p>
            </div>
            <div class="modal-footer">
                <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
                <button type="button" class="btn btn-primary" id="create-context-btn">Create app</button>
        </div>
    </div>
  </div>
</div>
</toolbox>
