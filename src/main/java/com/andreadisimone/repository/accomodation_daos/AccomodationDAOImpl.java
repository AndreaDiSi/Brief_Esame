package com.andreadisimone.repository.accomodation_daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Host;
import com.andreadisimone.repository.host_daos.HostDAOImpl;
import com.andreadisimone.util.DatabaseConnection;
import com.andreadisimone.util.DateConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationDAOImpl implements AccomodationDAO {

    @Override
    public Accomodation create(Accomodation accomodation) {
        String sql = "INSERT INTO accomodation(accomodation_name, n_rooms, accomodation_address, id_host, n_bed_places, floor, starter_date, end_date, price) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_accomodation";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, accomodation.getName());
            ps.setInt(2, accomodation.getNRooms());
            ps.setString(3, accomodation.getAccomodationAddress());
            ps.setInt(4, accomodation.getHost().getIdHost());
            ps.setInt(5, accomodation.getNBedPlaces());
            ps.setInt(6, accomodation.getFloor());
            ps.setDate(7, Date.valueOf(accomodation.getStartDate()));
            ps.setDate(8, Date.valueOf(accomodation.getEndDate()));
            ps.setInt(9, accomodation.getPrice());
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                accomodation.setIdAccomodation((rs.getInt("id_accomodation")));
            }}
            
        } catch (SQLException exception) {
            log.error("Error during the creation of the accomodation: {}", accomodation.getName(), exception);
            throw new RuntimeException("SQLException: ", exception);
        }

        log.info("Accomodation created successfully - ID: {}, Email: {}", accomodation.getIdAccomodation(), accomodation.getAccomodationAddress());
        return accomodation;
    }

    @Override
    public List<Accomodation> findAll() {
        String sql = "SELECT * FROM accomodation";
        List<Accomodation> accomodationsList = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection(); 
        PreparedStatement ps = connection.prepareStatement(sql)){

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    accomodationsList.add(mapResultSetToAccomodtion(rs));
                }
            } catch(Exception ex){
                ex.printStackTrace();
                log.error("Accomodation findAll execute query gone wrong");
            }
        }catch(SQLException ex){
            log.error("Error during the connection to the db or during the creation of the find all Accomodations");
        }
        return accomodationsList;
    }

    @Override
    public List<Accomodation> findAllHostAccomodations(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Accomodation> findById(Integer idAccomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Accomodation> update(Accomodation accomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteAllHostAccomodations(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(Integer idAccomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    //=========UTILITY=============//

    private Accomodation mapResultSetToAccomodtion(ResultSet rs) throws SQLException{
        Accomodation accomodation = new Accomodation();
        accomodation.setIdAccomodation(rs.getInt("id_accomodation"));
        accomodation.setName(rs.getString("accomodation_name"));
        accomodation.setNRooms(rs.getInt("n_rooms"));
        accomodation.setAccomodationAddress(rs.getString("accomodation_address"));
        HostDAOImpl hostDAO = new HostDAOImpl();
        //lambda expression, funzione senza parametri che torna un oggetto
        accomodation.setHost(hostDAO.findById(rs.getInt("id_host")).orElseThrow(() -> new HostNotFoundException("Host not found")));
        accomodation.setNBedPlaces(rs.getInt("n_bed_places"));
        accomodation.setFloor(rs.getInt("floor"));
        accomodation.setStartDate(DateConverter.date2LocalDate(rs.getDate("starter_date")));
        accomodation.setEndDate(DateConverter.date2LocalDate(rs.getDate("end_date")));
        accomodation.setPrice(rs.getInt("price"));
        return accomodation;
    }
}
