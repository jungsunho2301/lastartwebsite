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

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 10~11자리 숫자여야 합니다.")
    private String phone;

    @Size(max = 500, message = "메시지는 최대 500자까지 입력할 수 있습니다.")
    private String message;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getArtnum() { return artnum; }
    public String getPhone() { return phone; }
    public String getMessage() { return message; }
}
