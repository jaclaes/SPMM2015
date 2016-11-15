class ReisezielController {
    
    
def exportService
def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
if(params?.format && params.format != "html"){ OutputStream output = new FileOutputStream("F:\\My Documents\\eclipse_workspace\\cheetah_workspace\\org.cheetahplatform.grails\\export\\Reiseziel.${params.format}"); exportService.export(params.format, output, Reiseziel.list(params), [:], [:]) }

        [ reisezielInstanceList: Reiseziel.list( params ) ]
    }

    def show = {
        def reisezielInstance = Reiseziel.get( params.id )

        if(!reisezielInstance) {
            flash.message = "Reiseziel not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ reisezielInstance : reisezielInstance ] }
    }

    def delete = {
        def reisezielInstance = Reiseziel.get( params.id )
        if(reisezielInstance) {
            reisezielInstance.delete()
            flash.message = "Reiseziel ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Reiseziel not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def reisezielInstance = Reiseziel.get( params.id )

        if(!reisezielInstance) {
            flash.message = "Reiseziel not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ reisezielInstance : reisezielInstance ]
        }
    }

    def update = {
        def reisezielInstance = Reiseziel.get( params.id )
        if(reisezielInstance) {
            reisezielInstance.properties = params
            if(!reisezielInstance.hasErrors() && reisezielInstance.save()) {
                flash.message = "Reiseziel ${params.id} updated"
                redirect(action:show,id:reisezielInstance.id)
            }
            else {
                render(view:'edit',model:[reisezielInstance:reisezielInstance])
            }
        }
        else {
            flash.message = "Reiseziel not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def reisezielInstance = new Reiseziel()
        reisezielInstance.properties = params
        return ['reisezielInstance':reisezielInstance]
    }

    def save = {
        def reisezielInstance = new Reiseziel(params)
        if(!reisezielInstance.hasErrors() && reisezielInstance.save()) {
            flash.message = "Reiseziel ${reisezielInstance.id} created"
            redirect(action:show,id:reisezielInstance.id)
        }
        else {
            render(view:'create',model:[reisezielInstance:reisezielInstance])
        }
    }
}
