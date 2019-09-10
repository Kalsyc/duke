package slave.elements;

import slave.exception.DukeException;
import slave.task.Deadline;
import slave.task.Event;
import slave.task.Task;
import slave.task.ToDo;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * User Interface that prints to user.
 */
public class Ui {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Prints welcome Message on application start-up.
     */
    public static String showWelcomeMessage() {
        String message = "Hello! I'm Slave! Your very own productivity application.\n"
                + "     What can I do for you? Type 'help' for commands!";
        String formattedMessage = Formatter.formatMessage(message);
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Displays goodbye message when exiting program.
     */
    public static String showByeMessage() {
        String message = "Bye! Thanks for using me! Will be right here when you need me.";
        String formattedMessage = Formatter.formatMessage(message);
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Displays help message when prompted.
     */
    public String showHelpMessage() {
        String message = "Commands:\n"
                + "     todo <task description> : To add task to list\n"
                + "     event <task description> /at <date> : To add event to list\n"
                + "     deadline <task description> /by <date> : To add deadline to list\n"
                + "     list : Retrieves all the tasks you have so far\n"
                + "     done <task index> : Checks task as done\n"
                + "     delete <task index> : Deletes task at a particular index\n"
                + "     clear : clear all tasks in the list and wipe all data\n"
                + "     bye : Exit program\n"
                + "     find <term> : Find tasks with the term in the description/name\n"
                + "     Note: Currently, Slave can only read date in the form 'DD/MM/YYYY HHMM'\n"
                + "     (E.g. 2/1/2020 1254 will be read as 2nd of January 2020 12.54pm)";
        String formattedMessage = Formatter.formatMessage(message);
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Prints error message to the user.
     *
     * @param e Error to be printed.
     */
    public String showErrorMessage(DukeException e) {
        String formattedMessage = Formatter.formatMessage(e.getMessage());
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Reads input from user.
     *
     * @return User input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints clear command message.
     */
    public String printClearCommand() {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Clearing List...")
                + Formatter.getLine();
        System.out.print(Formatter.indentLine(formattedMessage));
        return formattedMessage;
    }

    /**
     * Prints add deadline command.
     *
     * @param deadlineTask Deadline task to be added.
     * @param tasks Current task list.
     */
    public String printAddDeadlineCommand(Deadline deadlineTask, TaskList tasks) {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Got it. I've added this task:")
                + Formatter.indentLine("  " + deadlineTask)
                + Formatter.indentLine("Now you have " + tasks.getSize() + " tasks in the list.")
                + Formatter.getLine();
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Prints add event command.
     *
     * @param eventTask Event task to be added.
     * @param tasks Current task list.
     */
    public String printAddEventCommand(Event eventTask, TaskList tasks) {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Got it. I've added this task:")
                + Formatter.indentLine("  " + eventTask)
                + Formatter.indentLine("Now you have " + tasks.getSize() + " tasks in the list.")
                + Formatter.getLine();
        System.out.println(formattedMessage);
        return formattedMessage;
    }


    /**
     * Prints add to-do command.
     *
     * @param toDoTask To-do task to be added.
     * @param tasks Current task list.
     */
    public String printAddToDoCommand(ToDo toDoTask, TaskList tasks) {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Got it. I've added this task:")
                + Formatter.indentLine("  " + toDoTask)
                + Formatter.indentLine("Now you have " + tasks.getSize() + " tasks in the list.")
                + Formatter.getLine();
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Prints delete command.
     *
     * @param toRemove Task to be removed.
     * @param tasks Current task list.
     */
    public String printDeleteCommand(Task toRemove, TaskList tasks) {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Noted. I've removed this task:")
                + Formatter.indentLine("  " + toRemove)
                + Formatter.indentLine("Now you have " + tasks.getSize() + " tasks in the list.")
                + Formatter.getLine();
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Prints done command.
     *
     * @param task Task to be set as done.
     */
    public String printDoneCommand(Task task) {
        String formattedMessage = Formatter.getLine()
                + Formatter.indentLine("Nice! I've marked this task as done:")
                + Formatter.indentLine("  " + task)
                + Formatter.getLine();
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Prints a list of current tasks.
     *
     * @param tasks Current task list.
     */
    public String printListCommand(TaskList tasks) {
        StringBuilder formattedMessage = new StringBuilder(Formatter.getLine()
                + Formatter.indentLine("Here are the tasks in your list:"));
        for (Task task: tasks.getList()) {
            formattedMessage.append(Formatter.indentLine(task.getId() + "." + task.toString()));
        }
        formattedMessage.append(Formatter.getLine());
        System.out.println(formattedMessage);
        return formattedMessage.toString();
    }

    /**
     * Prints a list of matching results.
     *
     * @param findTasks List of searched entries.
     */
    public String printFindCommand(ArrayList<Task> findTasks) {
        StringBuilder formattedMessage = new StringBuilder(Formatter.getLine()
                + Formatter.indentLine("Here are the matching tasks in your list:"));
        int index = 1;
        for (Task task: findTasks) {
            formattedMessage.append(Formatter.indentLine(index + ") " + task.getId() + "." + task.toString()));
            index++;
        }
        formattedMessage.append(Formatter.getLine());
        System.out.println(formattedMessage);
        return formattedMessage.toString();
    }
}
