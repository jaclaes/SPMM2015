

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Personendaten</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Personendaten List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Personendaten</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${personendatenInstance}">
            <div class="errors">
                <g:renderErrors bean="${personendatenInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nachname">Nachname:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:personendatenInstance,field:'nachname','errors')}">
                                    <input type="text" id="nachname" name="nachname" value="${fieldValue(bean:personendatenInstance,field:'nachname')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="vorname">Vorname:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:personendatenInstance,field:'vorname','errors')}">
                                    <input type="text" id="vorname" name="vorname" value="${fieldValue(bean:personendatenInstance,field:'vorname')}"/>
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
