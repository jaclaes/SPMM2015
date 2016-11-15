package at.component;

import org.osgi.service.event.EventHandler;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Producer;

public interface IComponentUiController extends Consumer, Producer, EventHandler {

}
