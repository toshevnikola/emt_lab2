package com.example.emtlab2.service;

import com.example.emtlab2.model.Transaction;
import com.example.emtlab2.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}
