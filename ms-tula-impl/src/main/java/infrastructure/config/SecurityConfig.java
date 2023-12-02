package infrastructure.config;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

  @ConditionalOnProperty(name = "security.enabled", havingValue = "true")
  @Bean
  public SecurityFilterChain securedFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/actuator/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();

    return http.build();
  }

  @ConditionalOnProperty(name = "security.enabled", havingValue = "false", matchIfMissing = true)
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .httpBasic().disable()
      .cors().and().csrf().disable()
    .build();
  }

  @ConditionalOnProperty(name = "security.ssl.validate-peer-certificate", havingValue = "true",
      matchIfMissing = true)
  @Bean
  public RestTemplate restTemplateSsl() {
    return new RestTemplate();
  }

  @SneakyThrows
  @ConditionalOnProperty(name = "security.ssl.validate-peer-certificate", havingValue = "false")
  @Bean
  public RestTemplate restTemplateNoSsl() {
    SSLContext sslContext = SSLContexts.custom()
        .loadTrustMaterial((X509Certificate[] chain, String authType) -> true)
        .build();
    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
        .build();
    var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(requestFactory);
  }
}
