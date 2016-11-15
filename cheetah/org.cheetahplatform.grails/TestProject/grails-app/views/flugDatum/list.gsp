


<export:resource />
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/><meta name="layout" content="main" /><title>FlugDatum List</title></head><body><div class="nav"><span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span><span class="menuButton"><g:link class="create" action="create">New FlugDatum</g:link></span></div><div class="body"><h1>FlugDatum List</h1><g:if test="${flash.message}"><div class="message">${flash.message}</div></g:if><div class="list"><table><thead><tr><g:sortableColumn property="id" title="Id" /><g:sortableColumn property="datum" title="Datum" /></tr></thead><tbody><g:each in="${flugDatumInstanceList}" status="i" var="flugDatumInstance"><tr class="${(i % 2) == 0 ? 'odd' : 'even'}"><td><g:link action="show" id="${flugDatumInstance.id}">${fieldValue(bean:flugDatumInstance, field:'id')}</g:link></td><td>${fieldValue(bean:flugDatumInstance, field:'datum')}</td></tr></g:each></tbody></table></div><div class="paginateButtons"><g:paginate total="${FlugDatum.count()}" /></div></div>
<export:formats formats="['xml']" />
</body>
</html>
