package dutyplanner.ui;

import javafx.stage.Stage;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Starts the UI just for testing */
    void startTest(Stage primaryStage, NricUserPair nricUserPair);
}