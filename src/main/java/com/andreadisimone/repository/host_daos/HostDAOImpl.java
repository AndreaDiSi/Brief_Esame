package com.andreadisimone.repository.host_daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Host;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostDAOImpl implements HostDAO{

    @Override
    public Host create(Host Host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Host> findAllHosts(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Host> findById(Integer idHost) {
        String sql = "SELECT * FROM host WHERE id_host = ?";
        try (Connection connection = DatabaseConnection.getConnection(); 
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idHost);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapResultSetToHost(rs));
                }
            }
        } catch(SQLException ex){
            log.error("Connection to DB failed");
            throw new RuntimeException("SQLException: ", ex);
        }
        log.debug("No host found with ID {}", idHost);
        return Optional.empty();
    }

    @Override
    public Optional<Host> update(Host Host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteAllHosts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(Integer idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    //=========UTILITY=============//
    public Host mapResultSetToHost(ResultSet rs)throws SQLException{
        Host host = new Host();
        host.setIdHost(rs.getInt("id_host"));
        host.setSuperHost(rs.getBoolean("is_superhost"));
        host.setEmail(rs.getString("email"));
        host.setName(rs.getString("host_name"));
        host.setSurname(rs.getString("surname"));
        host.setAddress(rs.getString("host_address"));
        return host;
    } 
}
