<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>

<jsp:useBean id="bean" class="jetbrains.buildServer.softinstall.server.SoftInstallPropertiesBean"/>

<script type="text/javascript" src="${teamcityPluginResourcesPath}js/soft-install-conf.js"></script>



<tr id="soft-to-install-list">
  <th><label for="SoftToInstall">Python script source:</label></th>
  <td>
    <props:multilineProperty name="softToInstall"
                             linkTitle="Names of software to install"
                             cols="30" rows="10"
                             expanded="${true}"/>
  </td>
</tr>




