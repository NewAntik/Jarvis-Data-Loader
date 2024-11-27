package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.BirthCertificate;
import ua.jarvis.data.loader.core.model.Email;
import ua.jarvis.data.loader.core.model.FirstName;
import ua.jarvis.data.loader.core.model.MiddleName;
import ua.jarvis.data.loader.core.model.Passport;
import ua.jarvis.data.loader.core.model.Phone;
import ua.jarvis.data.loader.core.model.SurName;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.BooleanType;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BankDIAConverterImpl extends AbstractUserConverter {
	private static final Logger LOG = LoggerFactory.getLogger(BankDIAConverterImpl.class);

	private String[] lineArray;

	@Override
	public ConvertorType getType() {
		return ConvertorType.DIA;
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("convert was called with {} lines", lines.size());
		final List<User> users = new ArrayList<>();
		for (final String line : lines) {
			lineArray = line.split("\\|");
			final int arrLength = lineArray.length;
			final User user = new User();

			if (arrLength >= 2 && isValidString(lineArray[1])) {
				user.addEmail(new Email(lineArray[1]));
			}
			if (arrLength >= 3 && isValidString(lineArray[2])) {
				final FirstName firstName = new FirstName(lineArray[2]);
				firstName.setUser(user);
				user.getFirstNames().add(firstName);
			}
			if (arrLength >= 4 && isValidString(lineArray[3])) {
				final SurName surName = new SurName(lineArray[3]);
				surName.setUser(user);
				user.getSurNames().add(surName);
			}
			if (arrLength >= 5 && isValidString(lineArray[4])) {
				final MiddleName middleName = new MiddleName(lineArray[4]);
				middleName.setUser(user);
				user.getMiddleNames().add(middleName);
			}
			if (arrLength >= 6 && isValidString(lineArray[5])) {
				addBirthCertificate(user, lineArray[5]);
			}
			if (arrLength >= 7 && isValidString(lineArray[6])) {
				final Phone phone = new Phone();

				if (lineArray[6].length() > 10) {
					phone.setNumber(getNormalizedPhoneNumber(lineArray[6]));
				} else {
					phone.setNumber(lineArray[6]);
				}

				phone.setUser(user);
				user.addPhone(phone);
			}
			if (arrLength >= 8 && isValidString(lineArray[7]) && lineArray[7].length() == 10) {
				user.setRnokpp(lineArray[7]);
			}

			if (arrLength >= 9 && isValidString(lineArray[8])) {
				addAddress(user, lineArray[8]);
			}

			if (arrLength > 9) {
				addPassport(user, arrLength);
			}

			if (!isEmptyUser(user)) {
				user.setIndividualEntrepreneur(BooleanType.UNKNOWN);
				users.add(user);
			}
		}

		return users;
	}

	private void addBirthCertificate(final User user, final String dateLine){
		final String[] dateParts = dateLine.split("-");
		final BirthCertificate certificate = new BirthCertificate();

		if(dateParts.length == 1){
			certificate.setYear(dateParts[0]);
		}
		if(dateParts.length == 2){
			certificate.setMonth(dateParts[1]);
		}
		if(dateParts.length == 3){
			certificate.setDay(dateParts[2]);
		}

		certificate.setUser(user);
		user.setBirthCertificate(certificate);
	}

	private void addPassport(final User user, final int arrLength){
		final Passport passport = new Passport();
		passport.setUser(user);

		if(isValidString(lineArray[9])){
			passport.setPassportNumber(lineArray[9]);
		}
		if(arrLength >= 11 && isValidString(lineArray[10])){
			if(passport.getPassportNumber() != null){
				passport.setPassportNumber(passport.getPassportNumber() + lineArray[10]);
			}
			passport.setPassportNumber( lineArray[10]);
		}
		if(arrLength >= 12 && isValidString(lineArray[11])){
			passport.setIssueDate(LocalDate.parse(lineArray[11], DATE_YYY_MM_DD).atStartOfDay());
		}
		if(arrLength >= 13 && isValidString(lineArray[12])){
			passport.setAuthority(lineArray[12]);
		}
	}
}
