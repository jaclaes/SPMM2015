package at.component.event;

public class EventPropertyDefinition {

	public static final int STRING = 1;
	public static final int LONG = 2;
	public static final int INTEGER = 3;
	public static final int SHORT = 4;
	public static final int CHARACTER = 5;
	public static final int BYTE = 6;
	public static final int DOUBLE = 7;
	public static final int FLOAT = 8;
	public static final int BOOLEAN = 9;

	private String propertyKey;
	private String propertyName;
	private int propertyType;
	private String propertyDefaultValue;
	private final String propertyDescription;

	public EventPropertyDefinition(String propertyKey, String propertyName, String propertyDescription, int propertyType,
			String propertyDefaultValue) {
		this.propertyKey = propertyKey;
		this.propertyName = propertyName;
		this.propertyDescription = propertyDescription;
		this.propertyType = propertyType;
		this.propertyDefaultValue = propertyDefaultValue;
	}

	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public int getPropertyType() {
		return propertyType;
	}

	public String getPropertyTypeAsString() {
		switch (propertyType) {
		case STRING:
			return "String";
		case BOOLEAN:
			return "Boolean";
		case BYTE:
			return "Byte";
		case CHARACTER:
			return "Character";
		case DOUBLE:
			return "Double";
		case FLOAT:
			return "Float";
		case INTEGER:
			return "Integer";
		case LONG:
			return "Long";
		case SHORT:
			return "Short";
		}

		return null;
	}

	public String getPropertyDescription() {
		return propertyDescription;
	}
}
