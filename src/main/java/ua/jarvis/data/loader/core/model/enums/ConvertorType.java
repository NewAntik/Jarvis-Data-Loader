package ua.jarvis.data.loader.core.model.enums;

public enum ConvertorType {
	DIA("Системный номер|email|first_name|last_name|middle_name|birthday|phone|ipn|address|");
	private final String value;

	ConvertorType(final String value) {
		this.value = value;
	}

	public String getValue() {return value;}
}
