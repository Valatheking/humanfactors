package dutyplanner.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import dutyplanner.commons.core.LogsCenter;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;


/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());


    @FXML
    private TextArea textDisplay;

    public BrowserPanel() {
        super(FXML);
        String initText = requestsToStringForDisplay();
        setPanelText(initText);
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void setPanelText(String text) {
        requireNonNull(text);
        textDisplay.setText(text);
    }

    /**
     * Returns a string representing the list of requests to be displayed.
     */
    public String requestsToStringForDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("List of duties");
        int counter = 0;
        return sb.toString();
    }

    /**
     * Refreshes the text displayed to reflect current request list.
     */
    public void refreshRequestListDisplay() {
        setPanelText(requestsToStringForDisplay());
    }
}
