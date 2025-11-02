package com.example.remotes;

import java.util.List;

import com.example.change_dtos.DeviseDto;
import com.example.server_dtos.TransactionCourantDto;
import com.example.server_dtos.VirementDto;

import jakarta.ejb.Remote;

@Remote
public interface TransactionCourantServiceRemote {


    boolean updateAllOperations(DeviseDto deviseDto);
    VirementDto doVirement (VirementDto virementDto) throws Exception;
    
    List<TransactionCourantDto> getAllTransactionsByClientAndCourant(int idClient , int idCourant);
    
    TransactionCourantDto updateTransactionCourant(TransactionCourantDto transactionCourantDto );

    List<TransactionCourantDto> getAllTransactionsByClient(int idClient);

    List<TransactionCourantDto> getAllTransactions();

    TransactionCourantDto getTransactionById(Integer id);

    List<TransactionCourantDto> getTransactionsByCompte(Integer idCompte);

    TransactionCourantDto saveTransaction(TransactionCourantDto transaction);

    void deleteTransaction(TransactionCourantDto transaction);
}
