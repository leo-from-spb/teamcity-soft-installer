<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="bean" class="jetbrains.buildServer.softinstall.server.SoftInstallPropertiesBean"/>


<%--
<div class="parameter">
    <div style="display: inline-block; border: 1px solid gray; margin: 1ex 1em 1ex 1em; padding: 4px; min-width: 40em;">
      <pre><c:out value="${bean.softToInstall}"/></pre>
    </div>
</div>
--%>

<div class="parameter">
    <div style="display: inline-block; border: 1px solid gray; margin: 1ex 1em 1ex 1em; padding: 4px; min-width: 40em;">
      <pre><c:out value="${propertiesBean.properties['softToInstall']}"/></pre>
    </div>
</div>


