package kappa.elements;

import kappa.command.AddToDoCommand;
import kappa.command.NullCommand;
import kappa.command.ListCommand;
import kappa.command.HelpCommand;
import kappa.command.ExitCommand;
import kappa.command.DoneCommand;
import kappa.command.DeleteCommand;
import kappa.command.ClearCommand;
import kappa.command.AddEventCommand;
import kappa.command.Command;
import kappa.command.AddDeadlineCommand;
import kappa.command.FindCommand;

import kappa.exception.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            checkValidity("todo", splitByTagToDo[0], splitByTagToDo[0].split(" "));
            return new AddToDoCommand(splitByTagToDo[0].substring(5), parseTags(splitByTagToDo));
        case "deadline":
            String[] splitByTagDeadline = fullCommand.split("/t");
            checkValidity("deadline", splitByTagDeadline[0].trim(), splitByTagDeadline[0].split(" "));
            String[] deadlineSplitByTokens = splitByTagDeadline[0].trim().split(" /by ");
            String deadlineDesc = deadlineSplitByTokens[0].substring(9);
            String deadlineDate = deadlineSplitByTokens[deadlineSplitByTokens.length - 1];
            return getValidDeadlineCommand(deadlineDesc, deadlineDate, parseTags(splitByTagDeadline));
        case "event":
            String[] splitByTagEvent = fullCommand.split("/t");
            checkValidity("event", splitByTagEvent[0], splitByTagEvent[0].split(" "));
            String[] eventSplitByTokens = splitByTagEvent[0].split(" /at ");
            String eventDesc = eventSplitByTokens[0].substring(6);
            String eventDate = eventSplitByTokens[eventSplitByTokens.length - 1];
            return getValidEventCommand(eventDesc, eventDate, parseTags(splitByTagEvent));
        default:
            return new NullCommand(firstWord);
        }
    }

    /**
     * Parses tags to the correct format and returns a Tags object.
     *
     * @param command Command given by user input.
     * @return Tags object containing tags from command.
     */
    private static Tags parseTags(String[] command) throws InvalidTagException {
        if (command.length < 2) {
            return new Tags();
        } else {
            String[] splitByHash = command[1].trim().split("#");
            if (splitByHash.length == 0) {
                throw new InvalidTagException();
            }

            List<String> tagsList = new ArrayList<>(Stream.of(splitByHash)
                    .map(s -> s.trim())
                    .collect(Collectors.toList())
                    .subList(1, splitByHash.length));
            return new Tags(tagsList);
        }
    }

    /**
     * Retrieves a valid event command from the raw user input.
     *
     * @param eventDesc Event Description.
     * @param eventDate Date Description.
     * @param tags Tags.
     * @return Valid AddEventCommand.
     * @throws KappaException Throws if Event Command is not valid.
     */
    private static Command getValidEventCommand(String eventDesc, String eventDate, Tags tags) throws KappaException {
        if (isDate(eventDate)) {
            String[] eventSplitTokens = eventDate.split(" ");
            DateTime validEventDate = new DateTime(eventSplitTokens[0], eventSplitTokens[1]);
            return new AddEventCommand(eventDesc, validEventDate, tags);
        }
        return new AddEventCommand(eventDesc, eventDate, tags);
    }

    /**
     * Retrieves a valid deadline command from the raw user input.
     *
     * @param deadlineDesc Deadline Description.
     * @param deadlineDate Date Description.
     * @param tags Tags.
     * @return Valid AddDeadlineCommand.
     * @throws KappaException Throws if Deadline Command is not valid.
     */
    private static Command getValidDeadlineCommand(String deadlineDesc, String deadlineDate, Tags tags) throws KappaException {
        if (isDate(deadlineDate)) {
            String[] dateSplitTokens = deadlineDate.split(" ");
            DateTime validDeadlineDate = new DateTime(dateSplitTokens[0], dateSplitTokens[1]);
            return new AddDeadlineCommand(deadlineDesc, validDeadlineDate, tags);
        }
        return new AddDeadlineCommand(deadlineDesc, deadlineDate, tags);
    }

    /**
     * Checks if date is a proper date in the form DD/MM/YYYY HHMM.
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

    /**
     * Checks Done and Delete commands to see if the index is missing.
     *
     * @param tokens User input command split by spaces.
     * @throws MissingTaskException Throws if index is missing.
     */
    private static void checkDoneDelete(String[] tokens) throws MissingTaskException {
        if (tokens.length <= 1) {
            throw new MissingTaskException();
        }
    }

    /**
     * Checks ToDo Command to see if description is missing.
     *
     * @param tokens User input command split by spaces.
     * @throws MissingDescriptionException Throws if description is missing.
     */
    private static void checkToDo(String[] tokens) throws MissingDescriptionException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        }
    }

    /**
     * Checks Event Command to see if description or date is missing.
     *
     * @param input Raw user input.
     * @param tokens User input command split by spaces.
     * @throws MissingDescriptionException Throws if description is missing.
     * @throws MissingDateException Throws if date is missing.
     */
    private static void checkEvent(String input, String[] tokens) throws MissingDescriptionException, MissingDateException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        } else if (!input.contains(" /at ")) {
            throw new MissingDateException();
        }
    }

    /**
     * Checks Deadline Command to see if description or date is missing.
     *
     * @param input Raw user input.
     * @param tokens User input command split by spaces.
     * @throws MissingDescriptionException Throws if description is missing.
     * @throws MissingDateException Throws if date is missing.
     */
    private static void checkDeadline(String input, String[] tokens) throws MissingDescriptionException, MissingDateException {
        if (tokens.length <= 1) {
            throw new MissingDescriptionException();
        } else if (!input.contains(" /by ")) {
            throw new MissingDateException();
        }
    }
}
