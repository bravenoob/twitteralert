package ch.bfh.bigdata.semarbeit.twitteralert.messaging;

public class Tweet {

    private String id;
    private String text;
    private String language;
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
            "id='" + id + '\'' +
            ", text='" + text + '\'' +
            ", language='" + language + '\'' +
            '}';
    }
}
