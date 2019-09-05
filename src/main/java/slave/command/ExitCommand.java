package slave.command;

import slave.elements.TaskList;
import slave.elements.Ui;

/**
 * A command which represents exiting the Slave program.
 */
public class ExitCommand extends Command {

    /**
     * Constructor for exit command.
     */
    public ExitCommand() {
        this.commandType = CommandType.EXIT;
    }

    /**
     * Executes by showing the goodbye message.
     *
     * @param tasks List containing current tasks.
     * @param ui User interface.
     */
    @Override
    public String execute(TaskList tasks, Ui ui) {
        return ui.showByeMessage();
    }

}

