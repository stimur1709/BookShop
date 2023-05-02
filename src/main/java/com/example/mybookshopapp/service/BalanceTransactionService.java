package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BalanceTransactionDto;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.repository.BalanceTransactionRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BalanceTransactionService extends ModelServiceImpl<BalanceTransaction, BTQuery, BalanceTransactionDto,
        BalanceTransactionDto, BalanceTransactionRepository> {

    @Autowired
    protected BalanceTransactionService(BalanceTransactionRepository repository, UserProfileService userProfileService, ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, BalanceTransactionDto.class, BalanceTransactionDto.class, BalanceTransaction.class, userProfileService, modelMapper, request);
    }

    @Override
    public Page<BalanceTransactionDto> getContents(BTQuery q) {
        if (q.isAuth()) {
            return repository.findBalanceTransactions(userProfileService.getUserId(), q.getStatuses(), getPageRequest(q))
                    .map(m -> modelMapper.map(m, BalanceTransactionDto.class));
        } else {
            return super.getContents(q);
        }
    }
}
