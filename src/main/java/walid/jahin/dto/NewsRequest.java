package walid.jahin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsRequest {

    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title can be up to 100 characters.")
    private String title;

    @NotBlank(message = "Content is required.")
    @Size(max = 2000, message = "Content can be up to 2,000 characters.")
    private String content;

    // Getter & Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
