package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.Email;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;
import ua.jarvis.data.loader.service.UserConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BankDIAConverterImpl implements UserConverter {
	private static final Logger LOG = LoggerFactory.getLogger(BankDIAConverterImpl.class);

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
			final String[] lineArray = lines.get(i).split("\\|");
			final User user = new User();

			if(lineArray[1] != null){
				user.addEmail(new Email(lineArray[1]));
			}
		}

		return users;
	}
}
