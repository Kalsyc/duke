package slave.elements;

import slave.task.Task;
import slave.task.Deadline;
import slave.task.Event;
import slave.task.ToDo;

import slave.exception.DukeException;
import slave.exception.InOutWentWrongException;
import slave.exception.NoStorageFileDetectedException;
import slave.exception.UnableToReadFileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;

/**
 * Storage class that writes and loads data from local .txt file.
 */
public class Storage {

    private File file;

    /**
     * Constructor that initialises a File object.
     *
     * @param filePath FilePath of .txt file to write/load.
     */
    public Storage(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Clears storage of data.
     *
     * @throws NoStorageFileDetectedException When storage file not found.
     */
    void clearStorage() throws NoStorageFileDetectedException {
        try {
            PrintWriter writer = new PrintWriter(this.file);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new NoStorageFileDetectedException();
        }
    }

    /**
     * Loads tasks from .txt file.
     *
     * @return An ArrayList of tasks parsed from .txt file.
     * @throws DukeException Throws if file cannot be found or input went wrong.
     */
    public ArrayList<Task> load() throws DukeException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            List<String> list = Files.readAllLines(this.file.toPath());
            int index = 1;
            for (String line : list) {
                taskList.add(formatFileToTask(line, index));
                index++;
            }
        } catch (IOException e) {
            this.file = new File("./duke.txt");
            return taskList;
        }
        return taskList;
    }

    /**
     * Refreshes storage and updates list of tasks to the stored local .txt file.
     *
     * @param taskList Current task list.
     * @throws DukeException Throws if file cannot be found.
     */
    void refreshStorage(ArrayList<Task> taskList) throws DukeException {
        try {
            PrintWriter writer = new PrintWriter(this.file);
            writer.close();
            for (Task task : taskList) {
                addTask(task);
            }
        } catch (FileNotFoundException e) {
            throw new NoStorageFileDetectedException();
        }
    }

    /**
     * Encodes task and add it to .txt file.
     *
     * @param task Task to store in .txt file.
     * @throws DukeException Throws if input went wrong in writing file.
     */

    void addTask(Task task) throws DukeException {
        try {
            FileWriter fw = new FileWriter(this.file, true);
            switch (task.getType()) {
            case TODO:
                writeToDo(task, fw);
                break;
            case DEADLINE:
                writeDeadline(task, fw);
                break;
            case EVENT:
                writeEvent(task, fw);
                break;
            default:
            }
        } catch (IOException error) {
            throw new InOutWentWrongException();
        }
    }

    private void writeEvent(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "Event" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription() + " ~ "
                + task.getDate()
                + System.lineSeparator());
        fw.close();
    }

    private void writeDeadline(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "Deadline" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription() + " ~ "
                + task.getDate()
                + System.lineSeparator());
        fw.close();
    }

    private void writeToDo(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "ToDo" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription()
                + System.lineSeparator());
        fw.close();
    }

    /**
     * Parses the line from local storage .txt file
     * to a task to be added into current program's taskList.
     *
     * @param line Line to parse.
     * @param index Current line of file.
     * @return Task that has been parsed.
     * @throws DukeException Throws error in reading file.
     */

    private Task formatFileToTask(String line, int index) throws DukeException {
        String[] tokens = line.split(" ~ ");
        switch (tokens[1]) {
        case "ToDo":
            return formatToDo(tokens);
        case "Deadline":
            return formatDeadline(tokens);
        case "Event":
            return formatEvent(tokens);
        default:
        throw new UnableToReadFileException(index);
        }
    }

    private Task formatEvent(String[] tokens) {
        Event eventTask = new Event(tokens[3], Integer.parseInt(tokens[0]), tokens[4]);
        if (tokens[2].equals("Done")) {
            eventTask.setDone();
        }
        return eventTask;
    }

    private Task formatDeadline(String[] tokens) {
        Deadline deadlineTask = new Deadline(tokens[3], Integer.parseInt(tokens[0]), tokens[4]);
        if (tokens[2].equals("Done")) {
            deadlineTask.setDone();
        }
        return deadlineTask;
    }

    private Task formatToDo(String[] tokens) {
        ToDo toDoTask = new ToDo(tokens[3], Integer.parseInt(tokens[0]));
        if (tokens[2].equals("Done")) {
            toDoTask.setDone();
        }
        return toDoTask;
    }
}