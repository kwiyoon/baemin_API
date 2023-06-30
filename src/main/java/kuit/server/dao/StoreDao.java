package kuit.server.dao;

import kuit.server.dto.store.GetStoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class StoreDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StoreDao(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<GetStoreResponse> getStores(String store_name, String status, Long last_id) {
        String sql = "select store_name, store_address, store_number, status, store_id from store " +
                "where store_name like :store_name and status=:status and store_id >= :last_id " +
                "order by store_id limit 20";

        Map<String, Object> param= Map.of(
                "store_name", "%"+store_name+"%",
                "status", status,
                "last_id", last_id
        );
        return jdbcTemplate.query(sql, param,
                (rs, rowNum)-> new GetStoreResponse(
                        rs.getString("store_name"),
                        rs.getString("store_address"),
                        rs.getString("store_number"),
                        rs.getString("status"),
                        rs.getLong("store_id")
                )
        );
    }
}
