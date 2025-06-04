package com.crm.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class DealStateMachineConfig extends EnumStateMachineConfigurerAdapter<DealState, DealEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<DealState, DealEvent> states) throws Exception {
        states
            .withStates()
            .initial(DealState.PROPOSAL_SENT)
            .states(EnumSet.allOf(DealState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<DealState, DealEvent> transitions) throws Exception {
        transitions
            // From PROPOSAL_SENT
            .withExternal()
                .source(DealState.PROPOSAL_SENT)
                .target(DealState.NEGOTIATION)
                .event(DealEvent.START_NEGOTIATION)
                .and()
            .withExternal()
                .source(DealState.PROPOSAL_SENT)
                .target(DealState.DISCOVERY)
                .event(DealEvent.NEED_MORE_INFO)
                .and()
            
            // From DISCOVERY
            .withExternal()
                .source(DealState.DISCOVERY)
                .target(DealState.QUALIFIED)
                .event(DealEvent.QUALIFY_DEAL)
                .and()
            
            // From QUALIFIED
            .withExternal()
                .source(DealState.QUALIFIED)
                .target(DealState.NEGOTIATION)
                .event(DealEvent.START_NEGOTIATION)
                .and()
            
            // From NEGOTIATION
            .withExternal()
                .source(DealState.NEGOTIATION)
                .target(DealState.CLOSED_WON)
                .event(DealEvent.WIN_DEAL)
                .and()
            .withExternal()
                .source(DealState.NEGOTIATION)
                .target(DealState.CLOSED_LOST)
                .event(DealEvent.LOSE_DEAL)
                .and()
            
            // From any state to CLOSED_LOST
            .withExternal()
                .source(DealState.PROPOSAL_SENT)
                .target(DealState.CLOSED_LOST)
                .event(DealEvent.LOSE_DEAL)
                .and()
            .withExternal()
                .source(DealState.DISCOVERY)
                .target(DealState.CLOSED_LOST)
                .event(DealEvent.LOSE_DEAL)
                .and()
            .withExternal()
                .source(DealState.QUALIFIED)
                .target(DealState.CLOSED_LOST)
                .event(DealEvent.LOSE_DEAL);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<DealState, DealEvent> config) throws Exception {
        config
            .withConfiguration()
            .autoStartup(true)
            .listener(listener());
    }

    @Bean
    public StateMachineListener<DealState, DealEvent> listener() {
        return new StateMachineListenerAdapter<DealState, DealEvent>() {
            @Override
            public void stateChanged(State<DealState, DealEvent> from, State<DealState, DealEvent> to) {
                if (from != null) {
                    System.out.println("State change from " + from.getId() + " to " + to.getId());
                }
            }
        };
    }
} 