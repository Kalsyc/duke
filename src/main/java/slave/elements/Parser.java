package slave.elements;

import slave.command.AddToDoCommand;
import slave.command.NullCommand;
import slave.command.ListCommand;
import slave.command.HelpCommand;
import slave.command.ExitCommand;
import slave.command.DoneCommand;
import slave.command.DeleteCommand;
import slave.command.ClearCommand;
import slave.command.AddEventCommand;
import slave.command.Command;
import slave.command.AddDeadlineCommand;
import slave.command.FindCommand;

import slave.exception.KappaException;
import slave.exception.MissingDateException;
import slave.exception.MissingDescriptionException;
import slave.exception.MissingTaskException;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser to take in input by user and returns the appropriate command.
 */
public class Parser {

    /**
     * Static method which parses input by user and returns the corresponding command object.
     *
     * @param fullCommand Input by user.
     * @return Corresponding command based on user input.
     * @throws KappaException For invalid input.
     */
    public static Command parse(String fullCommand) throws KappaException {
        String[] tokens = fullCommand.split(" ");
        String firstWord = tokens[0];
        switch (firstWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "help":
            return new HelpCommand();
        case "clear":
            return new ClearCommand();
        case "find":
            return new FindCommand(fullCommand.substring(5));
        case "done":
            checkValidity("done", fullCommand, tokens);
            return new DoneCommand(Integer.parseInt(tokens[1]));
        case "delete":
            checkValidity("delete", fullCommand, tokens);
            return new DeleteCommand(Integer.parseInt(tokens[1]));
        case "todo":
            String[] splitByTagToDo = fullCommand.split("/t");
            checkValidity("todo", splitByTagToDo[0], tokens);
            return new AddToDoCommand(splitByTagToDo[0].substring(5), parseTags(splitByTagToDo));
        case "deadline":
            String[] splitByTagDeadline = fullCommand.split("/t");
            checkValidity("deadline", splitByTagDeadline[0], tokens);
            String[] deadlineSplitByTokens = splitByTagDeadline[0].split(" /by ");
            String deadlineDesc = deadlineSplitByTokens[0].substring(9);
            String deadlineDate = deadlineSplitByTokens[deadlineSplitByTokens.length - 1];
            return getValidDeadlineCommand(deadlineDesc, deadlineDate, parseTags(splitByTagDeadline));
        case "event":
            String[] splitByTagEvent = fullCommand.split("/t");
            checkValidity("event", splitByTagEvent[0], tokens);
            String[] eventSplitByTokens = splitByTagEvent[0].split(" /at ");
            String eventDesc = eventSplitByTokens[0].substring(6);
            String eventDate = eventSplitByTokens[eventSplitByTokens.length - 1];
            return getValidEventCommand(eventDesc, eventDate, parseTags(splitByTagEvent));
        default:
            return new NullCommand(firstWord);
        }
    }

    private static Tags parseTags(String[] command) {
        if (command.length < 2) {
            return new Tags();
        } else {
            String[] splitByHash = command[1].trim().split("#");
            List<String> tagsList = new ArrayList<>();
            for (int i = 1; i < splitByHash.length; i++){
                tagsList.add(splitByHash[i]);
            }
            return new Tags(tagsList);
        }
    }

    private static Command getValidEventCommand(String eventDesc, String eventDate, Tags tags) throws KappaException {
        if (isDate(eventDate)) {
            String[] eventSplitTokens = eventDate.split(" ");
            DateTime validEventDate = new DateTime(eventSplitTokens[0], eventSplitTokens[1]);
            return new AddEventCommand(eventDesc, validEventDate, tags);
        }
        return new AddEventCommand(eventDesc, eventDate, tags);
    }

    private static Command getValidDeadlineCommand(String deadlineDesc, String deadlineDate, Tags tags) throws KappaException {
        if (isDate(deadlineDate)) {
            String[] dateSplitTokens = deadlineDate.split(" ");
            DateTime validDeadlineDate = new DateTime(dateSplitTokens[0], dateSplitTokens[1]);
            return new AddDeadlineCommand(deadlineDesc, validDeadlineDate, tags);
        }
        return new AddDeadlineCommand(deadlineDesc, deadlineDate, tags);
    }

    /**
     * Checks if it is a proper date in the form DD/MM/YYYY HHMM.
     *
     * @param dateDescription Date description.
     * @return True or false depending on the validity of the date description in that form.
     */
    private static boolean isDate(String dateDescription) {
        String[] dateSplit = dateDescription.split(" ");
        if (dateSplit.length != 2) {
            return false;
        } else {
            return dateSplit[0].contains("/")
                    && dateSplit[0].chars().filter(ch -> ch == '/').count() == 2;
        }
    }


    /**
     * Check validity of the deadline/event commands.
     *
     * @param check What command to check.
     * @param input Input from user.
     * @param tokens Input split by space.
     * @throws KappaException Throws exception if date or description is missing.
     */
    private static void checkValidity(String check, String input, String[] tokens) throws KappaException {
        switch (check) {
        case "deadline":
            checkDeadline(input, tokens);
            return;
        case "event":
            checkEvent(input, tokens);
            return;
        case "todo":
            checkToDo(tokens);
            return;
        case "done":
        case "delete":
            checkDoneDelete(tokens);
            return;
            default:
        }
    }

    private static void checkDoneDelete(String[] tokens) throws MissingTaskException {
        if (tokens.length <= 1) {
            throw new MissingTaskException();
        }
    }

    private static void checkToDo(String[] tokens) throws MissingDescriptionException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        }
    }

    private static void checkEvent(String input, String[] tokens) throws MissingDescriptionException, MissingDateException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        } else if (!input.contains(" /at ")) {
            throw new MissingDateException();
        }
    }

    private static void checkDeadline(String input, String[] tokens) throws MissingDescriptionException, MissingDateException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        } else if (!input.contains(" /by ")) {
            throw new MissingDateException();
        }
    }
}
