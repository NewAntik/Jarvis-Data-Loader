package ua.jarvis.data.loader.service.impl;

import ua.jarvis.data.loader.core.model.Address;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.service.UserConverter;

import java.time.format.DateTimeFormatter;

public abstract class AbstractUserConverter implements UserConverter {
	protected static final DateTimeFormatter DATE_YYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@SuppressWarnings("checkstyle:FinalParameters")
	protected String getNormalizedPhoneNumber(String number) {
		String normalizedNumber = null;
		number = number.replaceAll("[^\\d]", "");

		if (number.startsWith("38")) {
			number = number.substring(2);
		} else if (number.startsWith("8")) {
			number = number.substring(1);
		}

		if (number.length() == 10) {
			normalizedNumber = number;
		}

		return normalizedNumber;
	}

	protected boolean isEmptyUser(final User user){
		return user.getFirstNames().isEmpty() && user.getMiddleNames().isEmpty() && user.getSurNames().isEmpty() &&
			user.getRnokpp() == null;
	}

	protected boolean isValidString(final String inputString){
		return inputString != null &&
			!inputString.equals("0") &&
			!inputString.isBlank() &&
			!inputString.equals("null") &&
			!inputString.equals("\"\"");
	}

	protected boolean isNum(final String inputString){
		return inputString.matches(".*\\d+.*");
	}

	protected void addAddress(final User user, final String addressLine){
		final String[] addressParts = addressLine.split(",");
		final Address address = new Address();
		for(int i = 0; i < addressParts.length; i++){
			setAddressData(addressParts, address, i);
		}
		address.addUser(user);
		user.addAddress(address);
	}

	private void setAddressData(final String[] addressParts, final Address address, final int index){
		final String addressPart = addressParts[index];
		if(addressPart.toLowerCase().contains("кв.")){
			final String flat = addressPart.replace("(?i)кв\\.?", "").trim();
			final String[] num = flat.split("\\.");
			if(isNum(num[1])){
				address.setFlatNumber(num[1]);
			}
		}
		if(addressPart.toLowerCase().contains("обл")){
			final String cleaned = addressPart.replace("(?i)обл\\.?", "").trim();
			address.setRegion(cleaned);
		}
		if(addressPart.toLowerCase().contains("м.")){
			final String cleaned = addressPart.replace("(?i)м\\.?", "").trim();
			address.setCity(cleaned);
		}
		if(addressPart.toLowerCase().contains("с.")){
			final String cleaned = addressPart.replace("(?i)с\\.?", "").trim();
			address.setCity(cleaned);
		}
		if(addressPart.toLowerCase().contains("смт")){
			final String cleaned = addressPart.replace("(?i)смт\\.?", "").trim();
			address.setCity(cleaned);
		}
		if(addressPart.toLowerCase().contains("р-н")){
			final String cleaned = addressPart.replace("(?i)р-н\\.?", "").trim();
			address.setDistrict(cleaned);
		}
		if(addressPart.toLowerCase().contains("корпус")){
			final String cleaned = addressPart.replace("(?i)корпус\\.?", "").trim();
			address.setCorpus(cleaned);
		}
		if(addressPart.toLowerCase().contains("інше")){
			final String cleaned = addressPart.replace("(?i)інше\\.?", "").trim();
			address.setOther(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("вулиця") || addressPart.toLowerCase().contains("вул.")){
			final String cleaned = addressPart.replace("(?i)вулиця\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("проспект")){
			final String cleaned = addressPart.replace("(?i)проспект\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("провулок")){
			final String cleaned = addressPart.replace("(?i)провулок\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("бульвар")){
			final String cleaned = addressPart.replace("(?i)бульвар\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("шосе")){
			final String cleaned = addressPart.replace("(?i)шосе\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("мікрорайон")){
			final String cleaned = addressPart.replace("(?i)мікрорайон\\.?", "").trim();
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
	}

	private void setHomeNum(final String[] addressParts, final Address address, final int index){
		final int next = index + 1;
		if(addressParts.length >= next && isNum(addressParts[next])){
			address.setHomeNumber(addressParts[next]);
		}
	}
}
