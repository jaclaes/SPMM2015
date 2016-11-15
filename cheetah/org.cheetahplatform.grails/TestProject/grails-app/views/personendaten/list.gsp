


<export:resource />
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/><meta name="layout" content="main" /><title>Personendaten List</title></head><body><div class="nav"><span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span><span class="menuButton"><g:link class="create" action="create">New Personendaten</g:link></span></div><div class="body"><h1>Personendaten List</h1><g:if test="${flash.message}"><div class="message">${flash.message}</div></g:if><div class="list"><table><thead><tr><g:sortableColumn property="id" title="Id" /><g:sortableColumn property="nachname" title="Nachname" /><g:sortableColumn property="vorname" title="Vorname" /></tr></thead><tbody><g:each in="${personendatenInstanceList}" status="i" var="personendatenInstance"><tr class="${(i % 2) == 0 ? 'odd' : 'even'}"><td><g:link action="show" id="${personendatenInstance.id}">${fieldValue(bean:personendatenInstance, field:'id')}</g:link></td><td>${fieldValue(bean:personendatenInstance, field:'nachname')}</td><td>${fieldValue(bean:personendatenInstance, field:'vorname')}</td></tr></g:each></tbody></table></div><div class="paginateButtons"><g:paginate total="${Personendaten.count()}" /></div></div>
<export:formats formats="['xml']" />
</body>
</html>
