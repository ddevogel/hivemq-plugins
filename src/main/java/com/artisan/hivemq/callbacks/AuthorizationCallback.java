/*
 * Copyright 2014 Dominik Obermaier <dominik.obermaier@dc-square.de>
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

package com.artisan.hivemq.callbacks;

import com.hivemq.spi.aop.cache.Cached;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.security.OnAuthorizationCallback;
import com.hivemq.spi.callback.security.authorization.AuthorizationBehaviour;
import com.hivemq.spi.security.ClientData;
import com.hivemq.spi.topic.MqttTopicPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Dominik Obermaier
 */
public class AuthorizationCallback implements OnAuthorizationCallback {

    private static Logger log = LoggerFactory.getLogger(AuthorizationCallback.class);


    @Inject
    public AuthorizationCallback() {
    }

    @Override
    @Cached(timeToLive = 300, timeUnit = TimeUnit.SECONDS)
    public List<MqttTopicPermission> getPermissionsForClient(ClientData clientData) {
        //clientData.getClientId() will be a JWT token
        //use it to get client's publish/subscribe permissions from db or cache
        return getPermissionsForToken(clientData.getClientId());
    }

    @Override
    public AuthorizationBehaviour getDefaultBehaviour() {
        return AuthorizationBehaviour.DENY;
    }

    @Override
    public int priority() {
        return CallbackPriority.HIGH;
    }

    private List<MqttTopicPermission> getPermissionsForToken(String jwtToken){
        List<MqttTopicPermission> result = new ArrayList<>();
        if(jwtToken.startsWith("lens_") || jwtToken.startsWith("mqttjs_")){
            result.add(new MqttTopicPermission(
                    "public/notifications/+",
                    MqttTopicPermission.TYPE.ALLOW,
                    MqttTopicPermission.ACTIVITY.PUBLISH));
        } else{
            result.add(new MqttTopicPermission(
                    "public/notifications/" + jwtToken,
                    MqttTopicPermission.TYPE.ALLOW,
                    MqttTopicPermission.ACTIVITY.SUBSCRIBE));
        }
        return result;
    }
}
