package idv.tgp10103.ben;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserScripts implements Serializable {
    private String headline;
    private String description;


    public UserScripts() {
    }

    public UserScripts(String headline, String description) {
        this.headline = headline;
        this.description = description;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
