

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create FlugDatum</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">FlugDatum List</g:link></span>
        </div>
        <div class="body">
            <h1>Create FlugDatum</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${flugDatumInstance}">
            <div class="errors">
                <g:renderErrors bean="${flugDatumInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="datum">Datum:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:flugDatumInstance,field:'datum','errors')}">
                                    <input type="text" id="datum" name="datum" value="${fieldValue(bean:flugDatumInstance,field:'datum')}" />
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
