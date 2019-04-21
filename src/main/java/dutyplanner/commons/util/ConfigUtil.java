package dutyplanner.commons.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import dutyplanner.commons.exceptions.DataConversionException;
import dutyplanner.commons.core.Config;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    public static Optional<Config> readConfig(Path configFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(configFilePath, Config.class);
    }

    public static void saveConfig(Config config, Path configFilePath) throws IOException {
        JsonUtil.saveJsonFile(config, configFilePath);
    }

}
