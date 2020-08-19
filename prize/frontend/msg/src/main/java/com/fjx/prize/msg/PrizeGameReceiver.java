package com.fjx.prize.msg;

import com.fjx.prize.commons.db.entity.CardUserGame;
import com.fjx.prize.commons.config.RabbitKeys;
import com.fjx.prize.commons.db.mapper.CardUserGameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitKeys.QUEUE_PLAY)
public class PrizeGameReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PrizeGameReceiver.class);

    @Autowired
    private CardUserGameMapper cardUserGameMapper;

    @RabbitHandler
    public void processMessage3(CardUserGame message) {
        logger.info("用户参与活动的信息 : 活动={},用户={},时间={}", message.getGameid(), message.getUserid(),
                message.getCreatetime());
        cardUserGameMapper.insert(message);
    }

}
