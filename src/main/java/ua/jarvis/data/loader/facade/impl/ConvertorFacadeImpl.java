package ua.jarvis.data.loader.facade.impl;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;
import ua.jarvis.data.loader.facade.ConvertorFacade;
import ua.jarvis.data.loader.service.UserConverter;

import java.util.EnumMap;
import java.util.List;

@Component
public class ConvertorFacadeImpl implements ConvertorFacade {
	private static final Logger LOG = LoggerFactory.getLogger(ConvertorFacadeImpl.class);
	private final List<UserConverter> converters;

	private final EnumMap<ConvertorType, UserConverter> executorRegistry = new EnumMap<>(ConvertorType.class);

	public ConvertorFacadeImpl(final List<UserConverter> converters) {
		this.converters = converters;
	}

	@PostConstruct
	protected void populateExecutorRegistry() {
		for (final UserConverter converter : converters) {
			final UserConverter alreadyRegistered = executorRegistry.put(converter.getType(), converter);

			if (alreadyRegistered != null) {
				throw new IllegalArgumentException(
					"Duplicate convertor found: {}" + alreadyRegistered.getType()
				);
			}
		}
	}

	@Override
	public List<User> convert(final List<String> lines) {
		LOG.info("ConvertorFacadeImpl was called with lines: {}", lines);
		UserConverter converter = null;
		final List<User> users;
		for(final UserConverter conv : converters){
			final boolean isConverter = conv.getType().getValue().equals(lines.get(0));
			if(isConverter){
				converter = conv;
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
