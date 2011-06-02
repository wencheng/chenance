/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package cn.sh.fang.chenance.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ExchangeRateHandler {
	
	private static final Logger LOG = Logger.getLogger(ExchangeRateHandler.class);
	
	private static Double CNY_JPY;
	
    public final static void main(String[] args) throws Exception {
    	System.out.println(getRate());
    }
    
    /**
     * get cny -> jpy rate
     * 
     */
    public static double getRate() {
    	if (CNY_JPY != null) {
    		return CNY_JPY;
    	}

        HttpClient httpclient = new DefaultHttpClient();
        String res = null;
        try {
        	
            HttpGet httpget = new HttpGet("http://xurrency.com/api/cny/jpy/1");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
//            HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");
//            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            res = httpclient.execute(httpget, responseHandler);
            LOG.debug("Get exchange rate response: " + res);
            int i = res.indexOf("value\":");
            if ( i < 0 ) {
            	// wrong response format
            	CNY_JPY = 0.0;
            	return 0.0;
            }
            
            int j = res.indexOf(',', i);
            
            CNY_JPY = Double.valueOf( res.substring(i+7, j) );
        } catch (ClientProtocolException e) {
        	// impossible
        	CNY_JPY = 0.0;
		} catch (IOException e) {
			LOG.warn("Get exchange rate failed: " + res, e);
            CNY_JPY = 0.0;
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		
		return CNY_JPY;
    }

	public static void setRate(Double valueOf) {
		CNY_JPY = valueOf;
	}

}