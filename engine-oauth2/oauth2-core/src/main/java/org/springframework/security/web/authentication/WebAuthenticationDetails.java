/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package org.springframework.security.web.authentication;

import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * A holder of selected HTTP details related to a web authentication request.
 * <p>
 * 临时将 WebAuthenticationDetails 拷贝出来，用于解决自定义  WebAuthenticationDetails 无法反序列化问题。
 * <p>
 * 1. 主要原因是 WebAuthenticationDetails 开放的构造函数是以 HttpServletRequest 作为参数，难以对该参数进行构造
 * 2. 而以 remoteAddress, sessionId 作为参数的构造函数，又被设置为 private，不能使用。
 * <p>
 * 该问题已经在 spring security 5.7 版本解决，但是不知道什么时候发布，所以先临时改一下。
 *
 * @author Ben Alex
 * @author Luke Taylor
 * @see <a href="https://github.com/spring-projects/spring-security/issues/10564">参考资料</a>
 */
public class WebAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String remoteAddress;

    private final String sessionId;

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public WebAuthenticationDetails(HttpServletRequest request) {
        this.remoteAddress = request.getRemoteAddr();
        HttpSession session = request.getSession(false);
        this.sessionId = (session != null) ? session.getId() : null;
    }

    /**
     * Constructor to add Jackson2 serialize/deserialize support
     *
     * @param remoteAddress remote address of current request
     * @param sessionId     session id
     */
    public WebAuthenticationDetails(final String remoteAddress, final String sessionId) {
        this.remoteAddress = remoteAddress;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails other = (WebAuthenticationDetails) obj;
            if ((this.remoteAddress == null) && (other.getRemoteAddress() != null)) {
                return false;
            }
            if ((this.remoteAddress != null) && (other.getRemoteAddress() == null)) {
                return false;
            }
            if (this.remoteAddress != null) {
                if (!this.remoteAddress.equals(other.getRemoteAddress())) {
                    return false;
                }
            }
            if ((this.sessionId == null) && (other.getSessionId() != null)) {
                return false;
            }
            if ((this.sessionId != null) && (other.getSessionId() == null)) {
                return false;
            }
            if (this.sessionId != null) {
                if (!this.sessionId.equals(other.getSessionId())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Indicates the TCP/IP address the authentication request was received from.
     *
     * @return the address
     */
    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    /**
     * Indicates the <code>HttpSession</code> id the authentication request was received
     * from.
     *
     * @return the session ID
     */
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public int hashCode() {
        int code = 7654;
        if (this.remoteAddress != null) {
            code = code * (this.remoteAddress.hashCode() % 7);
        }
        if (this.sessionId != null) {
            code = code * (this.sessionId.hashCode() % 7);
        }
        return code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("RemoteIpAddress=").append(this.getRemoteAddress()).append(", ");
        sb.append("SessionId=").append(this.getSessionId()).append("]");
        return sb.toString();
    }

}
