package ua.jarvis.data.loader.core.model.enums;

public enum ConvertorType {
	DIA("Системный номер|email|first_name|last_name|middle_name|birthday|phone|ipn|address|passport_series|passport_number|passport_issue_date|passport_issued_by"),
	DRFO18("|Системный номер|ФИО|фамилия|имя|отчество|дата рождения|инн|другая фамилия|адрес прописки|адрес - 1|адрес - 2|все адреса|номер авто|вид авто|все авто|Дата рождения|");

	private final String value;

	ConvertorType(final String value) {
		this.value = value;
	}

	public String getValue() {return value;}
}
