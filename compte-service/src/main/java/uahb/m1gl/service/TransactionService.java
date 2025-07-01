package uahb.m1gl.service;

import org.springframework.stereotype.Service;
import uahb.m1gl.model.Transaction;
import uahb.m1gl.repository.TransactionRepository;

@Service
public class TransactionService implements ITransaction{
    private final TransactionService transactionService;

    public TransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionService.save(transaction);
    }

}
