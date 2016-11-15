class HotelController {
    
    
def exportService
def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
if(params?.format && params.format != "html"){ OutputStream output = new FileOutputStream("F:\\My Documents\\eclipse_workspace\\cheetah_workspace\\org.cheetahplatform.grails\\export\\Hotel.${params.format}"); exportService.export(params.format, output, Hotel.list(params), [:], [:]) }

        [ hotelInstanceList: Hotel.list( params ) ]
    }

    def show = {
        def hotelInstance = Hotel.get( params.id )

        if(!hotelInstance) {
            flash.message = "Hotel not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ hotelInstance : hotelInstance ] }
    }

    def delete = {
        def hotelInstance = Hotel.get( params.id )
        if(hotelInstance) {
            hotelInstance.delete()
            flash.message = "Hotel ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Hotel not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def hotelInstance = Hotel.get( params.id )

        if(!hotelInstance) {
            flash.message = "Hotel not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ hotelInstance : hotelInstance ]
        }
    }

    def update = {
        def hotelInstance = Hotel.get( params.id )
        if(hotelInstance) {
            hotelInstance.properties = params
            if(!hotelInstance.hasErrors() && hotelInstance.save()) {
                flash.message = "Hotel ${params.id} updated"
                redirect(action:show,id:hotelInstance.id)
            }
            else {
                render(view:'edit',model:[hotelInstance:hotelInstance])
            }
        }
        else {
            flash.message = "Hotel not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def hotelInstance = new Hotel()
        hotelInstance.properties = params
        return ['hotelInstance':hotelInstance]
    }

    def save = {
        def hotelInstance = new Hotel(params)
        if(!hotelInstance.hasErrors() && hotelInstance.save()) {
            flash.message = "Hotel ${hotelInstance.id} created"
            redirect(action:show,id:hotelInstance.id)
        }
        else {
            render(view:'create',model:[hotelInstance:hotelInstance])
        }
    }
}
