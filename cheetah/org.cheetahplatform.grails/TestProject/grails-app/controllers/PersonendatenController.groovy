class PersonendatenController {
    
    
def exportService
def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
if(params?.format && params.format != "html"){ OutputStream output = new FileOutputStream("F:\\My Documents\\eclipse_workspace\\cheetah_workspace\\org.cheetahplatform.grails\\export\\Personendaten.${params.format}"); exportService.export(params.format, output, Personendaten.list(params), [:], [:]) }

        [ personendatenInstanceList: Personendaten.list( params ) ]
    }

    def show = {
        def personendatenInstance = Personendaten.get( params.id )

        if(!personendatenInstance) {
            flash.message = "Personendaten not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ personendatenInstance : personendatenInstance ] }
    }

    def delete = {
        def personendatenInstance = Personendaten.get( params.id )
        if(personendatenInstance) {
            personendatenInstance.delete()
            flash.message = "Personendaten ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Personendaten not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def personendatenInstance = Personendaten.get( params.id )

        if(!personendatenInstance) {
            flash.message = "Personendaten not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ personendatenInstance : personendatenInstance ]
        }
    }

    def update = {
        def personendatenInstance = Personendaten.get( params.id )
        if(personendatenInstance) {
            personendatenInstance.properties = params
            if(!personendatenInstance.hasErrors() && personendatenInstance.save()) {
                flash.message = "Personendaten ${params.id} updated"
                redirect(action:show,id:personendatenInstance.id)
            }
            else {
                render(view:'edit',model:[personendatenInstance:personendatenInstance])
            }
        }
        else {
            flash.message = "Personendaten not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def personendatenInstance = new Personendaten()
        personendatenInstance.properties = params
        return ['personendatenInstance':personendatenInstance]
    }

    def save = {
        def personendatenInstance = new Personendaten(params)
        if(!personendatenInstance.hasErrors() && personendatenInstance.save()) {
            flash.message = "Personendaten ${personendatenInstance.id} created"
            redirect(action:show,id:personendatenInstance.id)
        }
        else {
            render(view:'create',model:[personendatenInstance:personendatenInstance])
        }
    }
}
