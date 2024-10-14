package ua.jarvis.data.loader.service;

import java.io.IOException;
import java.util.List;

public interface FileService {
	List<String> getLines() throws IOException;
}
