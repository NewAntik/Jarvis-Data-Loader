package ua.jarvis.data.loader.service.impl;

import ua.jarvis.data.loader.core.model.Address;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.service.UserConverter;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String addressPart = addressParts[index];
		if(addressPart.toLowerCase().contains("прописка")){
			addressPart = deletePart(addressPart, "Прописка - ");
		}
		if(addressPart.toLowerCase().contains("кв.") && address.getFlatNumber() == null){
			address.setFlatNumber(getNumFlatHomeNum(addressPart));
			return;
		}
		if(addressPart.toLowerCase().contains("обл") || addressPart.toLowerCase().contains("обл.")  && address.getRegion() == null){
			final String cleaned;
			if(addressPart.toLowerCase().contains("обл.")){
				cleaned = deletePart(addressPart, "обл.");
			} else {
				cleaned = deletePart(addressPart,"обл");
			}
			address.setRegion(cleaned);
			return;
		}
		if(addressPart.toLowerCase().contains("м.") && address.getCity() == null){
			address.setCity(deletePart(addressPart, "м."));
			return;
		}
		if(addressPart.toLowerCase().contains("с.") && address.getCity() == null){
			final String cleaned = deletePart(addressPart, "с.");
			address.setCity(cleaned);
			return;
		}
		if(addressPart.toLowerCase().contains("смт") && address.getCity() == null){
			final String cleaned = deletePart(addressPart, "смт");
			address.setCity(cleaned);
			return;
		}
		if(addressPart.toLowerCase().contains("р-н") && address.getDistrict() == null){
			final String cleaned = deletePart(addressPart, "р-н");
			address.setDistrict(cleaned);
			return;
		}
		if(addressPart.toLowerCase().contains("корпус") || addressPart.toLowerCase().contains("корп.")
		&& address.getCorpus() == null){
			final String cleaned;
			if(addressPart.toLowerCase().contains("корпус")){
				cleaned = deletePart(addressPart, "корпус");
			} else {
				cleaned = deletePart(addressPart,"корп.");
			}
			address.setCorpus(cleaned);
			return;
		}
		if(addressPart.toLowerCase().contains("інше") && address.getOther() == null){
			final String cleaned = deletePart(addressPart, "інше");
			address.setOther(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("вулиця") ||
			addressPart.toLowerCase().contains("вул.") &&
				address.getStreet() == null
		){
			final String cleaned;
			if(addressPart.toLowerCase().contains("вулиця")){
				cleaned = deletePart(addressPart, "вулиця");
			} else {
				cleaned = deletePart(addressPart,"вул.");
			}
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("проспект") || addressPart.toLowerCase().contains("просп.") &&
			address.getStreet() == null
		){
			final String cleaned;
			if(addressPart.toLowerCase().contains("проспект")){
				cleaned = deletePart(addressPart, "проспект");
			} else {
				cleaned = deletePart(addressPart,"просп.");
			}			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("провулок") && address.getStreet() == null){
			final String cleaned = deletePart(addressPart, "провулок");
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("бульвар") && address.getStreet() == null){
			final String cleaned = deletePart(addressPart, "бульвар");
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("шосе") && address.getStreet() == null){
			final String cleaned = deletePart(addressPart, "шосе");
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
			return;
		}
		if(addressPart.toLowerCase().contains("мікрорайон") && address.getStreet() == null){
			final String cleaned = deletePart(addressPart, "мікрорайон");
			address.setStreet(cleaned);
			setHomeNum(addressParts, address, index);
		}
	}

	private void setHomeNum(final String[] addressParts, final Address address, final int index){
		final int next = index + 1;
		if(addressParts.length > next){
			address.setHomeNumber(getNumFlatHomeNum(addressParts[next]));
		}
	}

	protected String getNumFlatHomeNum(final String rawNum){
		final Pattern pattern = Pattern.compile("\\b\\d+"); // Находим числа в начале слова
		final Matcher matcher = pattern.matcher(rawNum);
		final String num =  matcher.find() ? matcher.group() : "";
		if(num.length() > 5){
			return num.substring(0, 5);
		}

		return num;
	}

//	protected String getNumFlatHomeNum(final String rawNum){
//		final String flatNum = rawNum.replaceAll("[^0-9].*", "").trim();
//		if(flatNum.length() > 5){
//			return flatNum.substring(0, 5);
//		}
//
//		return flatNum;
//	}

	private static String deletePart(final String inputString, final String part) {
		final String regex = "(?i)\\s*" + part + "\\.?\\s*";
		return inputString.replaceAll(regex, "").replaceAll("\\s+", " ").trim();
	}
}
