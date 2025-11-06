package com.example.service;

import com.example.mappers.RetraitMapper;
import com.example.models.CompteCourant;
import com.example.models.Etat;
import com.example.models.HistoriqueRetrait;
import com.example.models.Retrait;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.EtatRepository;
import com.example.repositories.HistoriqueRetraitRepository;
import com.example.repositories.RetraitRepository;
import com.example.server_dtos.RetraitDto;
import com.example.remotes.RetraitServiceRemote;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class RetraitService implements RetraitServiceRemote {

    @EJB
    private RetraitRepository retraitRepository;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private HistoriqueRetraitRepository historiqueRetraitRepository;

    @Resource
    private EJBContext ejbContext;

      @EJB
    private EtatRepository etatRepository;
    @Override
    public List<RetraitDto> findByCompte(Integer idCompte) {
        List<Retrait> retraits = retraitRepository.findByCompte(idCompte);
        return retraits.stream()
                .map(RetraitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RetraitDto findById(Integer idRetrait) {
        Retrait retrait = retraitRepository.findById(idRetrait);
        if (retrait != null) {
            return RetraitMapper.toDto(retrait);
        }
        return null;
    }

    @Override
    public RetraitDto create(RetraitDto retraitDto) throws Exception {
        try {
            if (retraitDto == null)
                throw new Exception("Le retrait est obligatoire.");

            CompteCourant compteDebit = compteCourantRepository.findById(retraitDto.getIdCompteDebit());
            Retrait retrait = RetraitMapper.toEntity(retraitDto, compteDebit);
            compteDebit.controlRetrait(retrait.getMontant());
            retraitRepository.create(retrait);
            Etat etat = etatRepository.findByLibelle("en_attente");

            HistoriqueRetrait hist = retrait.generateHistorique(etat, null, "creation");
            historiqueRetraitRepository.create(hist);
            return RetraitMapper.toDto(retrait);

        } catch (Exception e) {
            e.printStackTrace();
            
            ejbContext.setRollbackOnly();
            
            throw e;
        }

    }
}
