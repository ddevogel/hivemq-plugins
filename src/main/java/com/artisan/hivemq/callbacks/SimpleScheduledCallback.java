/*
 * Copyright 2015 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artisan.hivemq.callbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hivemq.spi.callback.schedule.ScheduledCallback;

/**
 * @author Christian Götz
 */
public class SimpleScheduledCallback implements ScheduledCallback {

    private static final Logger log = LoggerFactory.getLogger(SimpleScheduledCallback.class);

    @Override
    public void execute() {
        log.info("Scheduled Callback is doing maintenance!");
    }

    @Override
    public String cronExpression() {
        // Every 5 seconds
        return "0/5 * * * * ?";
    }
}
