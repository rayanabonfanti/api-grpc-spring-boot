package api.grpc.spring.boot.orchestrator.domain.orm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageUserResponseDTO {
    private String status;
    private String details;
    private byte[] file;
}

