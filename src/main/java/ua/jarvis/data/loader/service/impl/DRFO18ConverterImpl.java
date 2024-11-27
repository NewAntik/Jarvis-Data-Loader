package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.BirthCertificate;
import ua.jarvis.data.loader.core.model.FirstName;
import ua.jarvis.data.loader.core.model.MiddleName;
import ua.jarvis.data.loader.core.model.SurName;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.BooleanType;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;

import java.util.ArrayList;
import java.util.List;

@Component
public class DRFO18ConverterImpl extends AbstractUserConverter {
	private static final Logger LOG = LoggerFactory.getLogger(DRFO18ConverterImpl.class);

	private String[] lineArray;

	@Override
	public ConvertorType getType() {
		return ConvertorType.DRFO18;
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("convert was called with {} lines", lines.size());
		final List<User> users = new ArrayList<>();
		for (final String line : lines) {
			lineArray = line.split("\\|");
			final User user = new User();
			final int arrLength = lineArray.length;

			if (arrLength >= 4 && isValidString(lineArray[3])) {
				final SurName surName = new SurName(lineArray[3]);
				surName.setUser(user);
				user.getSurNames().add(surName);
			}
			if (arrLength >= 5 && isValidString(lineArray[4])) {
				final FirstName firstName = new FirstName(lineArray[4]);
				firstName.setUser(user);
				user.getFirstNames().add(firstName);
			}
			if (arrLength >= 6 && isValidString(lineArray[5])) {
				final MiddleName middleName = new MiddleName(lineArray[5]);
				middleName.setUser(user);
				user.getMiddleNames().add(middleName);
			}
			if (arrLength >= 7 && isValidString(lineArray[6])) {
				addBirthCertificate(user, lineArray[6]);
			}
			if (arrLength >= 8 && isValidString(lineArray[7]) && lineArray[7].length() == 10) {
				user.setRnokpp(lineArray[7]);
			}
			if (arrLength >= 9 && isValidString(lineArray[8])) {
				final SurName surName = new SurName(lineArray[8]);
				surName.setUser(user);
				user.getSurNames().add(surName);
			}
			if (arrLength >= 10 && isValidString(lineArray[9])) {
				addAddress(user, lineArray[9]);
			}

			if (!isEmptyUser(user)) {
				user.setIndividualEntrepreneur(BooleanType.UNKNOWN);
				users.add(user);
			}
		}

		return users;
	}

	private void addBirthCertificate(final User user, final String dateLine){
		final String[] dateParts = dateLine.split("\\.");
		final BirthCertificate certificate = new BirthCertificate();

		if(dateParts.length >= 1){
			certificate.setDay(dateParts[0]);
		}
		if(dateParts.length >= 2){
			certificate.setMonth(dateParts[1]);
		}
		if(dateParts.length >= 3){
			certificate.setYear(dateParts[2]);
		}

		certificate.setUser(user);
		user.setBirthCertificate(certificate);
	}
}
