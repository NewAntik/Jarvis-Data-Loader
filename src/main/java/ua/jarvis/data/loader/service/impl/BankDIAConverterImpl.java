package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.Address;
import ua.jarvis.data.loader.core.model.BirthCertificate;
import ua.jarvis.data.loader.core.model.Email;
import ua.jarvis.data.loader.core.model.Passport;
import ua.jarvis.data.loader.core.model.Phone;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;
import ua.jarvis.data.loader.service.UserConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BankDIAConverterImpl implements UserConverter {
	private static final Logger LOG = LoggerFactory.getLogger(BankDIAConverterImpl.class);
	private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private String[] lineArray;

	private User user;

	@Override
	public ConvertorType getType() {
		return ConvertorType.DIA;
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("BankDIAConverterImpl.convert(lines) was called with {} lines", lines.size());
		final List<User> users = new ArrayList<>();
		for(int i = 0; i < lines.size(); i++){
			lineArray = lines.get(i).split("\\|");
			user = new User();

			if(checkArrayLength(2) && checkString(lineArray[1])){
				user.addEmail(new Email(lineArray[1]));
			}
			if(checkArrayLength(3) && checkString(lineArray[2])){
				user.setName(lineArray[2]);
			}
			if(checkArrayLength(4) && checkString(lineArray[3])){
				user.setSurName(lineArray[3]);
			}
			if(checkArrayLength(5) && checkString(lineArray[4])){
				user.setMiddleName(lineArray[4]);
			}
			if(checkArrayLength(6) && checkString(lineArray[5])){
				addBirthCertificate();
			}
			if(checkArrayLength(7) && checkString(lineArray[6])){
				final Phone phone = new Phone();

				if(lineArray[6].length() > 10){
					phone.setNumber(getNormalizedPhoneNumber(lineArray[6]));
				}else{
					phone.setNumber(lineArray[6]);
				}

				phone.setUser(user);
				user.addPhone(phone);
			}
			if(checkArrayLength(8) && checkString(lineArray[7]) && lineArray[7].length() == 10){
				user.setRnokpp(lineArray[7]);
			}

			if(checkArrayLength(9) && checkString(lineArray[8])){
				addAddress();
			}

			if(lineArray.length > 9){
				addPassport();
			}
			if(!isEmptyUser()){
				users.add(user);
			}
		}

		return users;
	}

	private void addBirthCertificate(){
		final String[] dateParts = lineArray[5].split("-");
		final BirthCertificate certificate = new BirthCertificate();

		if(dateParts.length == 1){
			certificate.setYear(dateParts[0]);
		}
		if(dateParts.length == 2){
			certificate.setYear(dateParts[1]);
		}
		if(dateParts.length == 3){
			certificate.setYear(dateParts[2]);
		}

		certificate.setUser(user);
		user.setBirthCertificate(certificate);
	}

	private void addPassport(){
		final Passport passport = new Passport();
		passport.setUser(user);

		if(checkString(lineArray[9])){
			passport.setPassportNumber(lineArray[9]);
		}
		if(checkArrayLength(11) && checkString(lineArray[10])){
			if(passport.getPassportNumber() != null){
				passport.setPassportNumber(passport.getPassportNumber() + lineArray[10]);
			}
			passport.setPassportNumber( lineArray[10]);
		}
		if(checkArrayLength(12) && checkString(lineArray[11])){
			passport.setIssueDate(LocalDate.parse(lineArray[11], DATEFORMATTER).atStartOfDay());
		}
		if(checkArrayLength(13) && checkString(lineArray[12])){
			passport.setAuthority(lineArray[12]);
		}
	}

	private void addAddress(){
		final String[] addressParts = lineArray[8].split(",");
		final Address address = new Address();
		for(int i=0; i < addressParts.length; i++){
			setAddressData(addressParts, address, i);
		}
		address.addUser(user);
		user.addAddress(address);
	}

	private void setAddressData(final String[] addressParts, final Address address, final int index){
		final String addressPart = addressParts[index];
		if(addressPart.toLowerCase().contains("обл")){
			final String cleaned = addressPart.replace("(?i)обл\\.?", "").trim();
			address.setRegion(cleaned);
		}
		if(addressPart.toLowerCase().contains("м")){
			final String cleaned = addressPart.replace("(?i)м\\.?", "").trim();
			address.setCity(cleaned);
		}
		if(addressPart.toLowerCase().contains("с")){
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
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("вулиця")){
			final String cleaned = addressPart.replace("(?i)вулиця\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("проспект")){
			final String cleaned = addressPart.replace("(?i)проспект\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("провулок")){
			final String cleaned = addressPart.replace("(?i)провулок\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("бульвар")){
			final String cleaned = addressPart.replace("(?i)бульвар\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("шосе")){
			final String cleaned = addressPart.replace("(?i)шосе\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
		if(addressPart.toLowerCase().contains("мікрорайон")){
			final String cleaned = addressPart.replace("(?i)мікрорайон\\.?", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
	}

	private void setNum(final String[] addressParts, final Address address, final int index){
		final int next = index + 1;
		if(addressParts.length >= next && isNum(addressParts[next])){
			address.setHomeNumber(addressParts[next]);
		}
	}

	private boolean isNum(final String inputString){
		return inputString.matches(".*\\d+.*");
	}

	private boolean checkString(final String inputString){
		return inputString != null &&
			!inputString.equals("0") &&
			!inputString.isBlank() &&
			!inputString.equals("null") &&
			!inputString.equals("\"\"");
	}

	private boolean checkArrayLength(final int i){
		return lineArray.length >= i;
	}

	@SuppressWarnings("checkstyle:FinalParameters")
	private String getNormalizedPhoneNumber(String number) {
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

	public boolean isEmptyUser(){
		return user.getName() == null && user.getMiddleName() == null && user.getSurName() == null &&
			user.getRnokpp() == null;
	}
}
