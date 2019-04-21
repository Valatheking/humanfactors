package dutyplanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import dutyplanner.commons.core.Config;
import dutyplanner.commons.core.LogsCenter;
import dutyplanner.commons.core.Version;
import dutyplanner.commons.exceptions.DataConversionException;
import dutyplanner.commons.util.ConfigUtil;
import dutyplanner.commons.util.StringUtil;
import dutyplanner.logic.Logic;
import dutyplanner.logic.LogicManager;
import dutyplanner.model.util.SampleDataUtil;
import dutyplanner.ui.Ui;
import dutyplanner.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;
import dutyplanner.model.Model;
import dutyplanner.model.ModelManager;
import dutyplanner.model.PersonnelDatabase;
import dutyplanner.model.ReadOnlyPersonnelDatabase;
import dutyplanner.model.ReadOnlyUserPrefs;
import dutyplanner.model.UserPrefs;
import dutyplanner.storage.JsonPersonnelDatabaseStorage;
import dutyplanner.storage.JsonUserPrefsStorage;
import dutyplanner.storage.PersonnelDatabaseStorage;
import dutyplanner.storage.Storage;
import dutyplanner.storage.StorageManager;
import dutyplanner.storage.UserPrefsStorage;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 2, 1, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing PersonnelDatabase ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        PersonnelDatabaseStorage personnelDatabaseStorage =
                new JsonPersonnelDatabaseStorage(userPrefs.getPersonnelDatabaseFilePath());
        storage = new StorageManager(personnelDatabaseStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyPersonnelDatabase> personnelDatabaseOptional;
        ReadOnlyPersonnelDatabase initialData;
        try {
            personnelDatabaseOptional = storage.readPersonnelDatabase();
            if (!personnelDatabaseOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample PersonnelDatabase");
            }
            initialData = personnelDatabaseOptional.orElseGet(SampleDataUtil::getSamplePersonnelDatabase);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be "
                    + "starting with an empty PersonnelDatabase and Calendar");
            initialData = new PersonnelDatabase();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be "
                    + "starting with an empty PersonnelDatabase and Calendar");
            initialData = new PersonnelDatabase();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. "
                    + "Will be starting with an empty PersonnelDatabase");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting PersonnelDatabase " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
