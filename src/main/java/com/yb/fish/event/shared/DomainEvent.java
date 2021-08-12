package com.yb.fish.event.shared;

import java.io.Serializable;
import java.util.Date;

/**
* DomainEvent领域事件
* @author bing
* @create 10/08/2021
* @version 1.0
**/
public interface DomainEvent extends Serializable {
    Date occurredTime();
}
