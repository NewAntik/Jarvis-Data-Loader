package ua.jarvis.data.loader.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.facade.ConvertorFacade;
import ua.jarvis.data.loader.service.UserConverter;

import java.util.List;

@Component
public class ConvertorFacadeImpl implements ConvertorFacade {
	private static final Logger LOG = LoggerFactory.getLogger(ConvertorFacadeImpl.class);

	private final List<UserConverter> converters;

	private UserConverter converter;

	public ConvertorFacadeImpl(final List<UserConverter> converters) {
		this.converters = converters;
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("ConvertorFacadeImpl was called with {} lines", lines.size());
		final List<User> users;
		if(converter == null){
			for(final UserConverter conv : converters){
				final String firstLine = lines.get(0);
				final boolean isConverter = conv.getType().getValue().equals(firstLine);
				if(isConverter){
					converter = conv;
					lines.remove(lines.get(0));
				}
			}
		}

		if(converter != null){
			LOG.info("convert method in {} was called.", converter);
			users = converter.convert(lines);
		} else {
			throw new IllegalArgumentException("Convertor wasn't found.");
		}

		return users;
	}
}
