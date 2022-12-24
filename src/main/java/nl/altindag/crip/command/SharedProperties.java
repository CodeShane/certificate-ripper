/*
 * Copyright 2021 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.altindag.crip.command;

import nl.altindag.ssl.util.CertificateUtils;
import picocli.CommandLine.Option;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import static nl.altindag.ssl.util.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public class SharedProperties {

    @Option(names = {"-u", "--url"}, description = "Url of the target server to extract the certificates", required = true)
    private String[] urls;

    @Option(names = {"--proxy-host"}, description = "Proxy host")
    private String proxyHost;

    @Option(names = {"--proxy-port"}, description = "Proxy port")
    private Integer proxyPort;

    @Option(names = {"--proxy-user"}, description = "User for authenticating the user for the given proxy")
    private String proxyUser;

    @Option(names = {"--proxy-password"}, interactive = true, description = "Password for authenticating the user for the given proxy")
    private String proxyPassword;

    public Map<String, List<X509Certificate>> getUrlsToCertificates() {
        if (isNotBlank(proxyHost) && proxyPort != null && isNotBlank(proxyUser) && isNotBlank(proxyPassword)) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            PasswordAuthentication passwordAuthentication = new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
            return CertificateUtils.getCertificatesFromExternalSources(proxy, passwordAuthentication, urls);
        }

        if (isNotBlank(proxyHost) && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            return CertificateUtils.getCertificatesFromExternalSources(proxy, urls);
        }

        return CertificateUtils.getCertificatesFromExternalSources(urls);
    }

}