package kuit.server.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreResponse {
    private String store_name;
    private String store_address;
    private String store_number;
    private String status;
    private long store_id;

}
