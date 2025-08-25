package walid.jahin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BuyRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String id;

    @Pattern(regexp = "^[0-9-]+$", message = "Please enter numbers and '-' only.")
    private String phone;

    @Size(max = 500, message = "Message can be up to 500 characters.")
    private String message;

    // ✅ Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }

    // ✅ Setter 추가
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
