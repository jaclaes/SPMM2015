package at.component.framework.services.componentwireadmin;

public class ConnectionInformation {

	private final String consumerPid;
	private final String producerPid;

	public ConnectionInformation(String producerPid, String consumerPid) {
		this.producerPid = producerPid;
		this.consumerPid = consumerPid;
	}

	public String getConsumerPid() {
		return consumerPid;
	}

	public String getProducerPid() {
		return producerPid;
	}

	@Override
	public String toString() {
		return "ProducerPid: " + producerPid + ", ConsumerPid: " + consumerPid;
	}
}
