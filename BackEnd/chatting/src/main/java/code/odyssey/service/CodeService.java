package code.odyssey.service;

import code.odyssey.dto.CodeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

    private final TopicExchange topicExchange;
    private final RabbitTemplate rabbitTemplate;

    public void sendCode(CodeInfo codeInfo) {
        log.info(codeInfo.getCode());
        rabbitTemplate.convertAndSend(
                topicExchange.getName(),
                "ide."+codeInfo.getGuildProblemId(),
                codeInfo);
        // /topic/ide.{guildProblemId}를 구독한 클라이언트에게 메세지 전송

    }

}
