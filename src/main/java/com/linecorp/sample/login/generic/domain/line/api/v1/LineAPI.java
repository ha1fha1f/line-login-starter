/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.linecorp.sample.login.generic.domain.line.api.v1;

import com.linecorp.sample.login.generic.domain.line.api.v1.response.AccessToken;
import com.linecorp.sample.login.generic.domain.line.api.v1.response.Profile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * <p>LINE v1 API interface</p>
 * https://developers.line.me/web-login/integrating-web-login
 */
public interface LineAPI {

    /**
     * <p>Retrieving access tokens</p>
     * https://developers.line.me/web-login/integrating-web-login#obtain_access_token<br/>
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("v1/oauth/accessToken")
    Call<AccessToken> accessToken(
            @Field("grant_type") String grant_type,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("redirect_uri") String redirect_uri,
            @Field("code") String code);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @GET("v1/profile")
    Call<Profile> profile(@Header("Authorization") String token);

}
