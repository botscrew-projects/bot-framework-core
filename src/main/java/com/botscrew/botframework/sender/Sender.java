/*
 * Copyright 2018 BotsCrew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Bot;

/**
 * Component responsible for sending messages to chat platforms
 * @param <B> defines communication channel with user(page from `Facebook Messenger`, etc)
 * @param <M> defines type for messages which we can send to the platform
 */
public interface Sender<B extends Bot, M extends Message> {

    /**
     * Sends message to specified channel(Bot)
     * @param bot channel(bot) where to send message
     * @param message {@link Message} implementation which describes message structure for
     *                               specific chat platform
     */
    void send(B bot, M message);
}
