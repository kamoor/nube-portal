<%@page import="com.nube.portal.local.apps.LocalSetup"%>
<% if(LocalSetup.isEnabled()){ %>

<p>
<hr/>
<h4>Local application update</h4>
Folder name you want to update from local<br/>
<%=LocalSetup.LOCAL_ROOTDIR%><input type="text" name="appfolder" id="appfolder"/>
<input type="submit"  id="updateAppBtn" name="updateAppBtn" value="Update"/>
</p>
<%}%>