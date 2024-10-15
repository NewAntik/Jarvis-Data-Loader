package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.Address;
import ua.jarvis.data.loader.core.model.BirthCertificate;
import ua.jarvis.data.loader.core.model.Email;
import ua.jarvis.data.loader.core.model.Phone;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;
import ua.jarvis.data.loader.service.UserConverter;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankDIAConverterImpl implements UserConverter {
	private static final Logger LOG = LoggerFactory.getLogger(BankDIAConverterImpl.class);

	private String[] lineArray;

	private User user;

	@Override
	public ConvertorType getType() {
		return ConvertorType.DIA;
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("BankDIAConverterImpl.convert(lines) was called with lines: {}", lines);
		final List<User> users = new ArrayList<>();
		//todo continue converting
		for(int i = 3; i < lines.size();){
			lineArray = lines.get(i).split("\\|");
			user = new User();

			if(lineArray[1] != null){
				user.addEmail(new Email(lineArray[1]));
			}
			if(lineArray[2] != null){
				user.setName(lineArray[2]);
			}
			if(lineArray[3] != null){
				user.setSurName(lineArray[3]);
			}
			if(lineArray[4] != null){
				user.setMiddleName(lineArray[4]);
			}
			if(lineArray[5] != null && !lineArray[5].equals("0")){
				final String[] dateParts = lineArray[5].split("-");
				final BirthCertificate certificate = new BirthCertificate();
				certificate.setYear(dateParts[0]);
				certificate.setMonth(dateParts[1]);
				certificate.setDay(dateParts[3]);
				certificate.setUser(user);
				user.setBirthCertificate(certificate);
			}
			if(lineArray[6] != null){
				final Phone phone = new Phone();
				phone.setNumber(lineArray[6]);
				phone.setUser(user);
				user.addPhone(phone);
			}
			if(lineArray[7] != null){
				user.setRnokpp(lineArray[7]);
			}

			if(lineArray[8] != null){
				addAddress();
			}

		}

		return users;
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

//todo add logic wiht:
// "смт", "с."
// "інше", "проспект", "провулок", "бульвар", "шосе", "мікрорайон"
//
// в району може не бути приписки р-н
//м.Київ,  Голосіївський, вулиця Велика Китаївська , 92/98, корп. 1, кв. 310.
	private void setAddressData(final String[] addressParts, final Address address, final int index){
		final String addressPart = addressParts[index];
		if(addressPart.contains("обл.")){
			final String cleaned = addressPart.replace("обл.", "").trim();
			address.setRegion(cleaned);
		}
		if(addressPart.contains("м.")){
			final String cleaned = addressPart.replace("м.", "").trim();
			address.setCity(cleaned);
		}
		if(addressPart.contains("р-н")){
			final String cleaned = addressPart.replace("р-н", "").trim();
			address.setCity(cleaned);
		}

		if(addressPart.contains("вулиця")){
			final String cleaned = addressPart.replace("вулиця.", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}

		if(addressPart.contains("вулиця")){
			final String cleaned = addressPart.replace("вулиця.", "").trim();
			address.setStreet(cleaned);
			setNum(addressParts, address, index);
		}
	}

	private void setNum(final String[] addressParts, final Address address, final int index){
		if(addressParts.length >= (index+1)){
			address.setHomeNumber(addressParts[(index + 1)]);
		}
	}
}
