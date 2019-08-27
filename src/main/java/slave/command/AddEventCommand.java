package slave.command;

import slave.elements.Date;
import slave.elements.Ui;
import slave.elements.TaskList;
import slave.exception.DukeException;
import slave.task.Event;

public class AddEventCommand extends Command{

    String task;
    String date;
    Event eventTask;

    public AddEventCommand(String task, String date){
        this.commandType = CommandType.ADDEVENT;
        this.task = task;
        this.date = date;
    }

    public AddEventCommand(String task, Date date) throws DukeException {
        this.commandType = CommandType.ADDEVENT;
        this.task = task;
        this.date = date.convertToString();
    }

    @Override
    public void execute(TaskList taskList, Ui ui) throws DukeException {
        this.eventTask = new Event(this.task, taskList.getSize() + 1, this.date);
        taskList.addToList(this.eventTask);
        ui.printAddEventCommand(eventTask, taskList);
    }
}
