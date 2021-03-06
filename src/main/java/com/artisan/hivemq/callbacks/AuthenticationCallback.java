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
import com.hivemq.spi.callback.exception.AuthenticationException;
import com.hivemq.spi.callback.security.OnAuthenticationCallback;
import com.hivemq.spi.security.ClientCredentialsData;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Dominik Obermaier
 */
public class AuthenticationCallback implements OnAuthenticationCallback {

    private static Logger log = LoggerFactory.getLogger(AuthenticationCallback.class);


    /**
     * returns: {"auth": "valid"}
     */
    public static final String MOCK_HTTP_SERVICE = "http://www.mocky.io/v2/52ea1aa87cf0cd5e035407f0";
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;


    @Inject
    public AuthenticationCallback(final CloseableHttpClient httpClient,
                                  final ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    @Cached(timeToLive = 300, timeUnit = TimeUnit.SECONDS)
    public Boolean checkCredentials(final ClientCredentialsData clientCredentialsData) throws AuthenticationException {

        /*TODO: In a production ready plugin you should cache here if possible!!
        You can use hte @Cache Annotation for that or implement caching yourself */

        final HttpPost httpPost = new HttpPost(MOCK_HTTP_SERVICE);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                final HashMap result = objectMapper.readValue(EntityUtils.toString(entity), HashMap.class);
                if ("valid".equals(result.get("auth"))) {
                    return true;
                }
            }
        } catch (JsonParseException e) {
            log.error("Error while parsing the results from the webservice call", e);
        } catch (JsonMappingException e) {
            log.error("Error while parsing the results from the webservice call", e);
        } catch (ClientProtocolException e) {
            log.error("There was an error in the HTTP communication with the server", e);
        } catch (IOException e) {
            log.error("Could not get valid results from the webservice", e);
        } finally {
            closeGracefully(response);
        }
        //If we're getting here, we didn't get a valid response from the webservice
        return false;
    }

    private void closeGracefully(final CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                log.debug("Could not close ");
            }
        }
    }

    @Override
    public int priority() {
        return CallbackPriority.CRITICAL;
    }

}
