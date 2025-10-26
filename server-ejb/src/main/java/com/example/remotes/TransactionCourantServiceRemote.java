package com.example.remotes;

import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.dto.TransactionCourantDto;
import java.util.List;
import jakarta.ejb.Remote;

@Remote
public interface TransactionCourantServiceRemote {

    List<TransactionCourantDto> getAllTransactionsByClientAndCourant(int idClient , int idCourant);
    TransactionCourantDto updateTransactionCourant(TransactionCourantDto transactionCourantDto );

    List<TransactionCourantDto> getAllTransactionsByClient(int idClient);

    List<TransactionCourantDto> getAllTransactions();

    TransactionCourant getTransactionById(Integer id);

    List<TransactionCourant> getTransactionsByCompte(Integer idCompte);

    TransactionCourant saveTransaction(TransactionCourant transaction);

    void deleteTransaction(TransactionCourant transaction);
}
