package com.example.card.service;

import com.example.card.domain.CardInfo;
import com.example.card.response.CardResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CardService {
    List<CardResponse> getCardDetails(Long cardId);
    List<CardResponse> getUserCardDetails(Long cardId);
}
