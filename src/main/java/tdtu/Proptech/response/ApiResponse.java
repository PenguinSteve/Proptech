package tdtu.Proptech.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;

//    public ApiResponse(String message, Object data){
//        this.message = message;
//        this.data = data;
//    }
}
