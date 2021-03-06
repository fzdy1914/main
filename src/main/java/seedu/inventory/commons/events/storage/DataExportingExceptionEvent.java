package seedu.inventory.commons.events.storage;

import seedu.inventory.commons.events.BaseEvent;

/**
 * Indicates an exception during data exporting
 */
public class DataExportingExceptionEvent extends BaseEvent {

    public final Exception exception;

    public DataExportingExceptionEvent(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return exception.toString();
    }

}
