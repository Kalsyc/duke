package kappa.elements;

import kappa.task.Task;
import kappa.task.Deadline;
import kappa.task.Event;
import kappa.task.ToDo;

import kappa.exception.KappaException;
import kappa.exception.InOutWentWrongException;
import kappa.exception.NoStorageFileDetectedException;
import kappa.exception.UnableToReadFileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;
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
     * @throws KappaException Throws if file cannot be found or input went wrong.
     */
    public ArrayList<Task> load() throws KappaException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            List<String> list = Files.readAllLines(this.file.toPath());
            int index = 1;
            for (String line : list) {
                taskList.add(formatFileToTask(line, index));
                index++;
            }
        } catch (IOException e) {
            this.file = new File("./data/store.txt");
            return taskList;
        }
        return taskList;
    }

    /**
     * Refreshes storage and updates list of tasks to the stored local .txt file.
     *
     * @param taskList Current task list.
     * @throws KappaException Throws if file cannot be found.
     */
    void refreshStorage(ArrayList<Task> taskList) throws KappaException {
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
     * @throws KappaException Throws if input went wrong in writing file.
     */
    void addTask(Task task) throws KappaException {
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

    /**
     * Writes event to file to store.
     *
     * @param task Task.
     * @param fw FileWriter object.
     * @throws IOException Throws if unable to write or close.
     */
    private void writeEvent(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "Event" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription() + " ~ "
                + task.getDate() + " ~ "
                + task.getTags()
                + System.lineSeparator());
        fw.close();
    }

    /**
     * Writes deadline to file to store.
     *
     * @param task Task.
     * @param fw FileWriter object.
     * @throws IOException Throws if unable to write or close.
     */
    private void writeDeadline(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "Deadline" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription() + " ~ "
                + task.getDate() + " ~ "
                + task.getTags()
                + System.lineSeparator());
        fw.close();
    }

    /**
     * Writes ToDo to file to store.
     *
     * @param task Task.
     * @param fw FileWriter object.
     * @throws IOException Throws if unable to write or close.
     */
    private void writeToDo(Task task, FileWriter fw) throws IOException {
        fw.write(task.getId() + " ~ "
                + "ToDo" + " ~ "
                + task.getStatusIcon() + " ~ "
                + task.getDescription() + " ~ "
                + task.getTags()
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
     * @throws KappaException Throws error in reading file.
     */
    private Task formatFileToTask(String line, int index) throws KappaException {
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

    /**
     * Formats Event properly from storage.
     *
     * @param tokens Storage data split by ~.
     * @return Event from storage.
     */
    private Task formatEvent(String[] tokens) {
        Tags tags;
        if (tokens.length == 6) {
            tags = formatTags(tokens[5]);
        } else {
            tags = new Tags();
        }
        Event eventTask = new Event(tokens[3], Integer.parseInt(tokens[0]), tokens[4], tags);
        if (tokens[2].equals("Done")) {
            eventTask.setDone();
        }
        return eventTask;
    }

    /**
     * Formats Deadline properly from storage.
     *
     * @param tokens Storage data split by ~.
     * @return Deadline from storage.
     */
    private Task formatDeadline(String[] tokens) {
        Tags tags;
        if (tokens.length == 6) {
            tags = formatTags(tokens[5]);
        } else {
            tags = new Tags();
        }
        Deadline deadlineTask = new Deadline(tokens[3], Integer.parseInt(tokens[0]), tokens[4], tags);
        if (tokens[2].equals("Done")) {
            deadlineTask.setDone();
        }
        return deadlineTask;
    }

    /**
     * Formats ToDo properly from storage.
     *
     * @param tokens Storage data split by ~.
     * @return ToDo from storage.
     */
    private Task formatToDo(String[] tokens) {
        Tags tags;
        if (tokens.length == 5) {
            tags = formatTags(tokens[4]);
        } else {
            tags = new Tags();
        }
        ToDo toDoTask = new ToDo(tokens[3], Integer.parseInt(tokens[0]), tags);
        if (tokens[2].equals("Done")) {
            toDoTask.setDone();
        }
        return toDoTask;
    }

    /**
     * Formats Tags properly from storage for a specific task.
     *
     * @param tagsString tags data from Storage.
     * @return Tags from storage associated with a specific task.
     */
    private Tags formatTags(String tagsString) {
        String[] splitByHash = tagsString.split("#");
        List<String> tagsList = new ArrayList<>(Arrays.asList(splitByHash).subList(1, splitByHash.length));
        return new Tags(tagsList);

    }
}