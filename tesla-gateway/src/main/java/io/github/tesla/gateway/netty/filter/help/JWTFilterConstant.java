/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.tesla.gateway.netty.filter.help;

public interface JWTFilterConstant {
    String KE_ID_NAME = "ke_id";
    String UUS_ID_NAME = "uus_id";
    String KE_ID_COOKIE_NAME = "cookie_" + KE_ID_NAME;
    String UUS_ID_COOKIE_NAME = "cookie_" + UUS_ID_NAME;

    String KE_ID_HEADER_NAME = "x-ke_id";
    String UUS_ID_HEADER_NAME = "x-uus_id";

    int EXPIRY_SECONDS = 600;
    String DOMAIN_COOKIE_BKJK = "*.bkjk.com";
}