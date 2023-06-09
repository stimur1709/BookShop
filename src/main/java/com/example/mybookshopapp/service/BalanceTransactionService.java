package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dao.BooksTransactionsDao;
import com.example.mybookshopapp.dao.TransactionsIntervalDao;
import com.example.mybookshopapp.data.dao.BooksTransactionsCount;
import com.example.mybookshopapp.data.dao.TransactionsInterval;
import com.example.mybookshopapp.data.dto.BalanceTransactionDto;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.repository.BalanceTransactionRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Service
public class BalanceTransactionService extends ModelServiceImpl<BalanceTransaction, BTQuery, BalanceTransactionDto,
        BalanceTransactionDto, BalanceTransactionRepository> {

    private final TransactionsIntervalDao transactionsIntervalDao;
    private final BooksTransactionsDao booksTransactionsDao;

    @Autowired
    protected BalanceTransactionService(BalanceTransactionRepository repository, UserProfileService userProfileService, ModelMapper modelMapper, HttpServletRequest request, TransactionsIntervalDao transactionsIntervalDao, BooksTransactionsDao booksTransactionsDao) {
        super(repository, BalanceTransactionDto.class, BalanceTransactionDto.class, BalanceTransaction.class, userProfileService, modelMapper, request);
        this.transactionsIntervalDao = transactionsIntervalDao;
        this.booksTransactionsDao = booksTransactionsDao;
    }

    @Override
    public Page<BalanceTransactionDto> getPageContents(BTQuery q) {
        if (q.isAuth()) {
            return repository.findBalanceTransactions(userProfileService.getUserId(), q.getStatuses(), getPageRequest(q))
                    .map(m -> modelMapper.map(m, BalanceTransactionDto.class));
        } else {
            return super.getPageContents(q);
        }
    }

    public List<TransactionsInterval> getTransactionsInterval(BTQuery query) throws ParseException {
        return transactionsIntervalDao.getTransactionsInterval(query);
    }

    public List<BooksTransactionsCount> getBooksTransactionsCount(BTQuery query) throws ParseException {
        return booksTransactionsDao.getBooksTransactionsCount(query);
    }
}
