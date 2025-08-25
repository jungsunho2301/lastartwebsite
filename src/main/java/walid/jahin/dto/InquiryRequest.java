package walid.jahin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class InquiryRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String artnum;

    @Pattern(regexp = "^[0-9-]+$", message = "Please enter numbers and '-' only.")
    private String phone;

    @Size(max = 500, message = "Message can be up to 500 characters.")
    private String message;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getArtnum() { return artnum; }
    public String getPhone() { return phone; }
    public String getMessage() { return message; }
}
