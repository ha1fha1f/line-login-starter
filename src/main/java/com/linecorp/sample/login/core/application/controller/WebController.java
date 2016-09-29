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
package com.linecorp.sample.login.core.application.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.linecorp.sample.login.generic.domain.line.LineConfig;
import com.linecorp.sample.login.generic.domain.line.api.v1.LineAPIService;
import com.linecorp.sample.login.generic.domain.line.api.v1.response.AccessToken;
import com.linecorp.sample.login.infra.utils.CommonUtils;

/**
 * <p>user web application pages</p>
 */
@Controller
public class WebController {

    private static final String LINE_WEB_LOGIN_STATE= "lineWebLoginState";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final Logger logger = Logger.getLogger(WebController.class);

    @Autowired
    private LineConfig lineConfig;

    @Autowired
    private LineAPIService lineAPIService;

    /**
     * <p>LINE Login Button Page
     * <p>Login Type is to log in on any desktop or mobile website
     * https://developers.line.me/web-login/overview#login_flow_type_web
     */
    @RequestMapping("/")
    public String login(HttpSession httpSession) {
        if (httpSession.getAttribute(ACCESS_TOKEN) == null) {
            return "user/login";
        } else {
            return "redirect:" + "profile";
        }
    }

    @RequestMapping("/profile")
    public String profile(HttpSession httpSession) {
        if (httpSession.getAttribute(ACCESS_TOKEN) == null) {
            return "user/login";
        } else {
            return "user/profile";
        }
    }

    /**
     * <p>Redirect to LINE Login Page</p>
     */
    @RequestMapping(value = "/gotoauthpage")
    public String goToAuthPage(HttpSession httpSession){
        final String state = CommonUtils.getToken();
        httpSession.setAttribute(LINE_WEB_LOGIN_STATE, state);
        final String url = lineConfig.getLineWebLoginUrl(state);
        return "redirect:" + url;
    }

    /**
     * <p>Redirect Page from LINE Platform</p>
     * <p>Login Type is to log in on any desktop or mobile website
     * https://developers.line.me/web-login/integrating-web-login#redirect_to_web_site
     */
    @RequestMapping("/auth")
    public ModelAndView auth(
            HttpSession httpSession,
            @RequestParam(value = "state") String state,
            @RequestParam(value = "code") String code) {

        if (!state.equals(httpSession.getAttribute(LINE_WEB_LOGIN_STATE))){
            throw new IllegalArgumentException();
        };
        httpSession.removeAttribute(LINE_WEB_LOGIN_STATE);
        if (logger.isDebugEnabled()) {
            logger.debug("code : " + code);
        }
        AccessToken token = lineAPIService.accessToken(code);
        if (logger.isDebugEnabled()) {
            logger.debug("mid : " + token.mid);
            logger.debug("access_token : " + token.access_token);
            logger.debug("refresh_token : " + token.refresh_token);
            logger.debug("expires_in : " + token.expires_in);
        }

        httpSession.setAttribute(ACCESS_TOKEN, token);

        ModelAndView mav = new ModelAndView("user/success");
        mav.addObject("token", token);

        return mav;
    }
}
