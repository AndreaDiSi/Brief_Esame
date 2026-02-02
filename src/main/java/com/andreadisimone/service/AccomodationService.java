package com.andreadisimone.service;
import java.util.List;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.model.Accomodation;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAO;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAOImpl;
import com.andreadisimone.repository.host_daos.HostDAO;
import com.andreadisimone.repository.host_daos.HostDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationService {
    
    //dependency inversion per principi SOLID
    // passo l'interfaccia come attributo nello stato
    // inzializzo l'implementazione dell interfaccia assegnandolo all'interfaccia
    private final AccomodationDAO accomodationDao;
    private final HostDAO hostDao;

    public AccomodationService(){
        accomodationDao = new AccomodationDAOImpl();
        hostDao = new HostDAOImpl();
    }

    public AccomodationResponseDTO insertAccomodation(AccomodationRequestDTO accomodation){
        log.info("Trying to insert Accomodation - name: {}", accomodation.getAccomodationName());

        try{
            hostDao.findById(accomodation.getHostId());
            log.info("Trying to find Host from id");

            AccomodationRequestDTO accomodation2save = new AccomodationRequestDTO(accomodation.getAccomodationName(), accomodation.getNRooms(), accomodation.getAccomodationAddress(), accomodation.getNBedPlaces(), accomodation.getFloor(), accomodation.getPrice(), accomodation.getEndDate(), accomodation.getStartDate(), accomodation.getHostId());
            
            //manda request e riceve response
            return accomodationDao.create(accomodation2save);
            
        } catch (HostNotFoundException e) {
            log.error("Host with id {} not found", accomodation.getHostId());
            throw new HostNotFoundException("Host with id " + accomodation.getHostId() + " not found");
        }
    }

    public List<Accomodation> getAllAccomodation(){
        log.info("Trying to get all Accomodations");

        return accomodationDao.findAll();
    };

    public boolean deleteById(Integer idAccomodation){
        log.info("Trying to delete Accomodation with id: {}", idAccomodation);

        return accomodationDao.deleteById(idAccomodation);
    }

    public AccomodationResponseDTO update(Integer idAccomodation, AccomodationResponseDTO accomodation){
        log.info("Trying to update Accomodation with id: {}", idAccomodation);

        accomodation.setIdAccomodation(idAccomodation);

        return accomodationDao.update(accomodation, idAccomodation);
    }
}
