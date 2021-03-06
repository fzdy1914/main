package seedu.inventory.logic.commands.sale;

import static java.util.Objects.requireNonNull;

import seedu.inventory.logic.CommandHistory;
import seedu.inventory.logic.commands.Command;
import seedu.inventory.logic.commands.CommandResult;
import seedu.inventory.model.Model;

/**
 * Lists all sale orders in Inventory Manager to the user.
 */
public class ListSaleCommand extends Command {

    public static final String COMMAND_WORD = "list-sale";

    public static final String MESSAGE_SUCCESS = "Listed all sale orders.";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.listSales();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
