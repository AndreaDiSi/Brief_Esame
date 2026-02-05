package com.andreadisimone.repository.tenant_daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantRequestDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantDAOImpl implements TenantDAO {

    @Override
    public TenantResponseDTO create(TenantRequestDTO request) {
        String sql = """
            INSERT INTO tenant(email, tenant_name, surname, tenant_address) 
            VALUES(?, ?, ?, ?) 
            RETURNING id_tenant, email, tenant_name, surname, tenant_address
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.getEmail());
            ps.setString(2, request.getTenantName());
            ps.setString(3, request.getSurname());
            ps.setString(4, request.getTenantAddress());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Tenant created successfully - Email: {}", request.getEmail());
                    return mapResultSetToTenantResponseDTO(rs);
                }
            }

        } catch (SQLException exception) {
            log.error("Error during the creation of the tenant: {}", request.getEmail(), exception);
            throw new RuntimeException("SQLException: ", exception);
        }

        throw new RuntimeException("Failed to create tenant");
    }

    @Override
    public List<TenantResponseDTO> findAll() {
        String sql = "SELECT * FROM tenant";
        List<TenantResponseDTO> tenants = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tenants.add(mapResultSetToTenantResponseDTO(rs));
            }

        } catch (SQLException ex) {
            log.error("Error finding all tenants: {}", ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        log.info("Found {} tenants", tenants.size());
        return tenants;
    }

    @Override
    public Optional<TenantResponseDTO> findById(Integer idTenant) {
        String sql = "SELECT * FROM tenant WHERE id_tenant = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idTenant);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTenantResponseDTO(rs));
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding tenant with ID {}: {}", idTenant, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        return Optional.empty();
    }

    @Override
    public TenantResponseDTO update(Integer id, TenantRequestDTO request) {
        String sql = """
            UPDATE tenant 
            SET email = ?, 
                tenant_name = ?, 
                surname = ?,
                tenant_address = ?
            WHERE id_tenant = ? 
            RETURNING id_tenant, email, tenant_name, surname, tenant_address
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.getEmail());
            ps.setString(2, request.getTenantName());
            ps.setString(3, request.getSurname());
            ps.setString(4, request.getTenantAddress());
            ps.setInt(5, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Tenant updated successfully - ID: {}", id);
                    return mapResultSetToTenantResponseDTO(rs);
                }
            }

        } catch (SQLException ex) {
            log.error("Error updating tenant with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        throw new RuntimeException("Tenant not found with ID: " + id);
    }

    @Override
    public boolean deleteById(Integer idTenant) {
        String sql = "DELETE FROM tenant WHERE id_tenant = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idTenant);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Tenant deleted successfully - ID: {}", idTenant);
                return true;
            }

        } catch (SQLException ex) {
            log.error("Error deleting tenant with ID {}: {}", idTenant, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        return false;
    }

    @Override
    public List<TenantResponseDTO> getTopFiveTenants() {

        String sql = """
        SELECT 
            t.*,
            COUNT(r.id_reservation) AS n_reservations
        FROM tenant t
        JOIN reservation r ON t.id_tenant = r.id_tenant
        GROUP BY t.id_tenant
        ORDER BY n_reservations DESC
        LIMIT 5
        """;

        List<TenantResponseDTO> tenants = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                TenantResponseDTO tenant = new TenantResponseDTO();

                tenant.setIdTenant(rs.getInt("id_tenant"));
                tenant.setTenantName(rs.getString("tenant_name"));
                tenant.setSurname(rs.getString("surname"));
                tenant.setEmail(rs.getString("email"));
                tenant.setTenantAddress(rs.getString("tenant_address"));
                tenant.setNReservations(rs.getInt("n_reservations"));

                tenants.add(tenant);
            }

        } catch (SQLException ex) {
            log.error("Error finding top tenants: {}", ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        log.info("Found Top Five tenants: {}", tenants.size());
        return tenants;
    }

    @Override
    public Optional<ReservationResponseDTO> getLastReservation(Integer idTenant) {
        String sql = """
            SELECT r.id_reservation, r.reservation_start_date, r.reservation_end_date, r.id_accomodation, r.id_tenant
            FROM reservation r
            WHERE r.id_tenant = ?
            ORDER BY r.reservation_end_date DESC
            LIMIT 1
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idTenant);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Last reservation retrieved successfully for tenant ID: {}", idTenant);
                    return Optional.of(mapResultSetToReservationResponseDTO(rs));
                }
            }

        } catch (SQLException ex) {
            log.error("Error retrieving last reservation for tenant ID {}: {}", idTenant, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        return Optional.empty();
    }

    // ========= UTILITY ============= //
    private TenantResponseDTO mapResultSetToTenantResponseDTO(ResultSet rs) throws SQLException {
        return new TenantResponseDTO(
                rs.getInt("id_tenant"),
                rs.getString("tenant_name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("tenant_address")
        );
    }

    private ReservationResponseDTO mapResultSetToReservationResponseDTO(ResultSet rs) throws SQLException {
        return new ReservationResponseDTO(
                rs.getInt("id_reservation"),
                rs.getDate("reservation_start_date").toLocalDate(),
                rs.getDate("reservation_end_date").toLocalDate(),
                rs.getInt("id_accomodation"),
                rs.getInt("id_tenant")
        );
    }
}
