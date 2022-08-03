/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.access.justauth.processor;

import cn.herodotus.engine.access.justauth.properties.JustAuthProperties;
import cn.herodotus.engine.access.justauth.stamp.JustAuthStateStampManager;
import cn.hutool.core.util.EnumUtil;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;

/**
 * <p>Description: JustAuth请求的生成器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/22 11:23
 */
public class JustAuthProcessor {

    private JustAuthProperties justAuthProperties;
    private JustAuthStateStampManager justAuthStateStampManager;

    public void setJustAuthProperties(JustAuthProperties justAuthProperties) {
        this.justAuthProperties = justAuthProperties;
    }

    public void setJustAuthStateRedisCache(JustAuthStateStampManager justAuthStateStampManager) {
        this.justAuthStateStampManager = justAuthStateStampManager;
    }

    private JustAuthStateStampManager getJustAuthStateRedisCache() {
        return justAuthStateStampManager;
    }

    /**
     * 返回带state参数的授权url，授权回调时会带上这个state
     *
     * @param source 第三方登录的类别 {@link AuthDefaultSource}
     * @return 返回授权地址
     */
    public String getAuthorizeUrl(String source) {
        AuthRequest authRequest = this.getAuthRequest(source);
        return authRequest.authorize(AuthStateUtils.createState());
    }

    /**
     * 获取默认的 Request
     *
     * @param source {@link AuthSource}
     * @return {@link AuthRequest}
     */
    public AuthRequest getAuthRequest(String source) {

        AuthDefaultSource authDefaultSource;

        try {
            authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, source.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 无自定义匹配
            return null;
        }

        AuthConfig authConfig = justAuthProperties.getConfigs().get(authDefaultSource.name());
        // 找不到对应关系，直接返回空
        if (authConfig == null) {
            return null;
        }

        switch (authDefaultSource) {
            case GITHUB:
                return new AuthGithubRequest(authConfig, this.getJustAuthStateRedisCache());
            case WEIBO:
                return new AuthWeiboRequest(authConfig, this.getJustAuthStateRedisCache());
            case GITEE:
                return new AuthGiteeRequest(authConfig, this.getJustAuthStateRedisCache());
            case DINGTALK:
                return new AuthDingTalkRequest(authConfig, this.getJustAuthStateRedisCache());
            case BAIDU:
                return new AuthBaiduRequest(authConfig, this.getJustAuthStateRedisCache());
            case CSDN:
                return new AuthCsdnRequest(authConfig, this.getJustAuthStateRedisCache());
            case CODING:
                return new AuthCodingRequest(authConfig, this.getJustAuthStateRedisCache());
            case OSCHINA:
                return new AuthOschinaRequest(authConfig, this.getJustAuthStateRedisCache());
            case ALIPAY:
                return new AuthAlipayRequest(authConfig, this.getJustAuthStateRedisCache());
            case QQ:
                return new AuthQqRequest(authConfig, this.getJustAuthStateRedisCache());
            case WECHAT_MP:
                return new AuthWeChatMpRequest(authConfig, this.getJustAuthStateRedisCache());
            case WECHAT_OPEN:
                return new AuthWeChatOpenRequest(authConfig, this.getJustAuthStateRedisCache());
            case WECHAT_ENTERPRISE:
                return new AuthWeChatEnterpriseQrcodeRequest(authConfig, this.getJustAuthStateRedisCache());
            case WECHAT_ENTERPRISE_WEB:
                return new AuthWeChatEnterpriseWebRequest(authConfig, this.getJustAuthStateRedisCache());
            case TAOBAO:
                return new AuthTaobaoRequest(authConfig, this.getJustAuthStateRedisCache());
            case GOOGLE:
                return new AuthGoogleRequest(authConfig, this.getJustAuthStateRedisCache());
            case FACEBOOK:
                return new AuthFacebookRequest(authConfig, this.getJustAuthStateRedisCache());
            case DOUYIN:
                return new AuthDouyinRequest(authConfig, this.getJustAuthStateRedisCache());
            case LINKEDIN:
                return new AuthLinkedinRequest(authConfig, this.getJustAuthStateRedisCache());
            case MICROSOFT:
                return new AuthMicrosoftRequest(authConfig, this.getJustAuthStateRedisCache());
            case MI:
                return new AuthMiRequest(authConfig, this.getJustAuthStateRedisCache());
            case TOUTIAO:
                return new AuthToutiaoRequest(authConfig, this.getJustAuthStateRedisCache());
            case TEAMBITION:
                return new AuthTeambitionRequest(authConfig, this.getJustAuthStateRedisCache());
            case RENREN:
                return new AuthRenrenRequest(authConfig, this.getJustAuthStateRedisCache());
            case PINTEREST:
                return new AuthPinterestRequest(authConfig, this.getJustAuthStateRedisCache());
            case STACK_OVERFLOW:
                return new AuthStackOverflowRequest(authConfig, this.getJustAuthStateRedisCache());
            case HUAWEI:
                return new AuthHuaweiRequest(authConfig, this.getJustAuthStateRedisCache());
            case GITLAB:
                return new AuthGitlabRequest(authConfig, this.getJustAuthStateRedisCache());
            case KUJIALE:
                return new AuthKujialeRequest(authConfig, this.getJustAuthStateRedisCache());
            case ELEME:
                return new AuthElemeRequest(authConfig, this.getJustAuthStateRedisCache());
            case MEITUAN:
                return new AuthMeituanRequest(authConfig, this.getJustAuthStateRedisCache());
            case TWITTER:
                return new AuthTwitterRequest(authConfig, this.getJustAuthStateRedisCache());
            case FEISHU:
                return new AuthFeishuRequest(authConfig, this.getJustAuthStateRedisCache());
            case JD:
                return new AuthJdRequest(authConfig, this.getJustAuthStateRedisCache());
            case ALIYUN:
                return new AuthAliyunRequest(authConfig, this.getJustAuthStateRedisCache());
            case XMLY:
                return new AuthXmlyRequest(authConfig, this.getJustAuthStateRedisCache());
            default:
                return null;
        }
    }
}
