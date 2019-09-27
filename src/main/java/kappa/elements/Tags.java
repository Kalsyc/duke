package kappa.elements;

import java.util.HashSet;
import java.util.List;

/**
 * An object that stores multiple tags.
 */
public class Tags {

    private HashSet<String> tagSet;

    public Tags(List<String> tagList) {
        this.tagSet = new HashSet<>();
        this.tagSet.addAll(tagList);
    }

    public Tags(){
        this.tagSet = new HashSet<>();
    }

    public String toString() {
        if (tagSet.size() == 0) {
            return "None";
        }
        StringBuilder formattedString = new StringBuilder();
        for (String tag: tagSet) {
            formattedString.append("#").append(tag).append(" ");
        }
        return formattedString.toString().trim();
    }
}
