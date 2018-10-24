package uninove.br.loginwebservice;

import java.io.Serializable;

public class Contato implements Serializable {

    private int id;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;



    public Contato() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAtAt() {
        return updatedAt;
    }
}
