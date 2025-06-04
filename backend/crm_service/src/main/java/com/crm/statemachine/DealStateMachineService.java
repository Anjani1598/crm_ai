package com.crm.statemachine;

import com.crm.statemachine.entity.DealStateEntity;
import com.crm.statemachine.entity.DealStateTransitionHistory;
import com.crm.statemachine.exception.DealStateException;
import com.crm.statemachine.repository.DealStateRepository;
import com.crm.statemachine.repository.DealStateTransitionHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DealStateMachineService {
    private final StateMachineFactory<DealState, DealEvent> stateMachineFactory;
    private final DealStateRepository dealStateRepository;
    private final DealStateTransitionHistoryRepository historyRepository;
    private static final String DEAL_ID_HEADER = "deal_id";

    public DealStateMachineService(
            StateMachineFactory<DealState, DealEvent> stateMachineFactory,
            DealStateRepository dealStateRepository,
            DealStateTransitionHistoryRepository historyRepository) {
        this.stateMachineFactory = stateMachineFactory;
        this.dealStateRepository = dealStateRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public boolean sendEvent(Long dealId, DealEvent event) {
        StateMachine<DealState, DealEvent> sm = build(dealId);
        
        // Get current state from database
        DealStateEntity currentState = dealStateRepository.findById(dealId)
                .orElseThrow(() -> new DealStateException("Deal not found with id: " + dealId));
        
        // Validate if the transition is allowed
        if (!isValidTransition(currentState.getState(), event)) {
            throw new DealStateException(
                String.format("Invalid transition from %s with event %s", 
                    currentState.getState(), event));
        }

        Message<DealEvent> message = MessageBuilder.withPayload(event)
                .setHeader(DEAL_ID_HEADER, dealId)
                .build();
        
        try {
            sm.sendEvent(Mono.just(message))
                .doOnComplete(() -> {
                    if (sm.hasStateMachineError()) {
                        throw new DealStateException("State machine error occurred");
                    }
                })
                .blockLast();
            
            // If we reach here, the event was accepted
            DealState newState = sm.getState().getId();
            if (!newState.equals(currentState.getState())) {
                saveState(dealId, newState, event.name());
                log.info("currentState {}",currentState.getState());
                log.info("newState {}",newState);

                // Record the transition in history only if state actually changed
                historyRepository.save(new DealStateTransitionHistory(
                    dealId, currentState.getState(), newState, event));
                return true;
            } else {
                // No state change, do not record transition
                return false;
            }
        } catch (Exception e) {
            throw new DealStateException("Failed to process state transition: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public DealState getState(Long dealId) {
        DealStateEntity stateEntity = dealStateRepository.findById(dealId)
                .orElseThrow(() -> new DealStateException("Deal not found with id: " + dealId));
        return stateEntity.getState();
    }

    @Transactional(readOnly = true)
    public List<DealStateTransitionHistory> getTransitionHistory(Long dealId) {
        return historyRepository.findByDealIdOrderByTransitionTimeDesc(dealId);
    }

    private StateMachine<DealState, DealEvent> build(Long dealId) {
        StateMachine<DealState, DealEvent> sm = stateMachineFactory.getStateMachine(dealId.toString());
        sm.stop();

        DealStateEntity dealStateEntity = new DealStateEntity();
        dealStateEntity.setDealId(dealId);
        dealStateEntity.setState(DealState.PROPOSAL_SENT);
        dealStateEntity.setLastEvent("INITIAL");
        
        DealStateEntity stateEntity = dealStateRepository.findById(dealId)
                .orElse(dealStateEntity);
        
        // If this is a new deal, record the initial state
        if (stateEntity == dealStateEntity) {
            recordInitialState(dealId, DealState.PROPOSAL_SENT);
        }
        
        sm.getStateMachineAccessor()
                .doWithAllRegions(accessor -> {
                    accessor.resetStateMachine(new DefaultStateMachineContext<>(
                            stateEntity.getState(), null, null, null));
                });
        
        sm.start();
        return sm;
    }

    private void recordInitialState(Long dealId, DealState initialState) {
        historyRepository.save(new DealStateTransitionHistory(
            dealId,
            initialState,  // fromState is same as toState for initial state
            initialState,
            null  // No event for initial state
        ));
    }

    private void saveState(Long dealId, DealState state, String event) {
        DealStateEntity stateEntity = new DealStateEntity();
        stateEntity.setLastEvent(event);
        stateEntity.setState(state);
        stateEntity.setDealId(dealId);
        dealStateRepository.save(stateEntity);
    }

    private boolean isValidTransition(DealState currentState, DealEvent event) {
        return switch (currentState) {
            case PROPOSAL_SENT -> event == DealEvent.START_NEGOTIATION || 
                                event == DealEvent.NEED_MORE_INFO || 
                                event == DealEvent.LOSE_DEAL;
            case DISCOVERY -> event == DealEvent.QUALIFY_DEAL || 
                            event == DealEvent.LOSE_DEAL;
            case QUALIFIED -> event == DealEvent.START_NEGOTIATION || 
                            event == DealEvent.LOSE_DEAL;
            case NEGOTIATION -> event == DealEvent.WIN_DEAL || 
                              event == DealEvent.LOSE_DEAL;
            case CLOSED_WON, CLOSED_LOST -> false;
        };
    }
} 