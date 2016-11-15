


<export:resource />
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/><meta name="layout" content="main" /><title>Hotel List</title></head><body><div class="nav"><span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span><span class="menuButton"><g:link class="create" action="create">New Hotel</g:link></span></div><div class="body"><h1>Hotel List</h1><g:if test="${flash.message}"><div class="message">${flash.message}</div></g:if><div class="list"><table><thead><tr><g:sortableColumn property="id" title="Id" /><g:sortableColumn property="hotelName" title="Hotel Name" /></tr></thead><tbody><g:each in="${hotelInstanceList}" status="i" var="hotelInstance"><tr class="${(i % 2) == 0 ? 'odd' : 'even'}"><td><g:link action="show" id="${hotelInstance.id}">${fieldValue(bean:hotelInstance, field:'id')}</g:link></td><td>${fieldValue(bean:hotelInstance, field:'hotelName')}</td></tr></g:each></tbody></table></div><div class="paginateButtons"><g:paginate total="${Hotel.count()}" /></div></div>
<export:formats formats="['xml']" />
</body>
</html>
