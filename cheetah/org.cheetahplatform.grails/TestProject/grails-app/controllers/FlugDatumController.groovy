class FlugDatumController {
    
    
def exportService
def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
if(params?.format && params.format != "html"){ OutputStream output = new FileOutputStream("F:\\My Documents\\eclipse_workspace\\cheetah_workspace\\org.cheetahplatform.grails\\export\\FlugDatum.${params.format}"); exportService.export(params.format, output, FlugDatum.list(params), [:], [:]) }

        [ flugDatumInstanceList: FlugDatum.list( params ) ]
    }

    def show = {
        def flugDatumInstance = FlugDatum.get( params.id )

        if(!flugDatumInstance) {
            flash.message = "FlugDatum not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ flugDatumInstance : flugDatumInstance ] }
    }

    def delete = {
        def flugDatumInstance = FlugDatum.get( params.id )
        if(flugDatumInstance) {
            flugDatumInstance.delete()
            flash.message = "FlugDatum ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "FlugDatum not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def flugDatumInstance = FlugDatum.get( params.id )

        if(!flugDatumInstance) {
            flash.message = "FlugDatum not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ flugDatumInstance : flugDatumInstance ]
        }
    }

    def update = {
        def flugDatumInstance = FlugDatum.get( params.id )
        if(flugDatumInstance) {
            flugDatumInstance.properties = params
            if(!flugDatumInstance.hasErrors() && flugDatumInstance.save()) {
                flash.message = "FlugDatum ${params.id} updated"
                redirect(action:show,id:flugDatumInstance.id)
            }
            else {
                render(view:'edit',model:[flugDatumInstance:flugDatumInstance])
            }
        }
        else {
            flash.message = "FlugDatum not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def flugDatumInstance = new FlugDatum()
        flugDatumInstance.properties = params
        return ['flugDatumInstance':flugDatumInstance]
    }

    def save = {
        def flugDatumInstance = new FlugDatum(params)
        if(!flugDatumInstance.hasErrors() && flugDatumInstance.save()) {
            flash.message = "FlugDatum ${flugDatumInstance.id} created"
            redirect(action:show,id:flugDatumInstance.id)
        }
        else {
            render(view:'create',model:[flugDatumInstance:flugDatumInstance])
        }
    }
}
