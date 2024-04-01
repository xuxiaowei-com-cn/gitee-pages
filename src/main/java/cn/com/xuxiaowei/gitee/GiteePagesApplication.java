package cn.com.xuxiaowei.gitee;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

public class GiteePagesApplication {

	public static void main(String[] args) {

		String giteePagesUrl = System.getenv("GITEE_PAGES_URL");
		String giteePagesCookie = System.getenv("GITEE_PAGES_COOKIE");
		String giteePagesXCsrfToken = System.getenv("GITEE_PAGES_X_CSRF_TOKEN");
		String giteePagesBranch = System.getenv("GITEE_PAGES_BRANCH");
		String giteePagesForceHttps = System.getenv("GITEE_PAGES_FORCE_HTTPS");
		String giteePagesAutoUpdate = System.getenv("GITEE_PAGES_AUTO_UPDATE");

		String giteePagesBuildDirectory = System.getenv("GITEE_PAGES_BUILD_DIRECTORY");

		String giteePagesUserAgent = System.getenv("GITEE_PAGES_USER_AGENT");

		if (giteePagesUserAgent == null) {
			giteePagesUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36";
		}

		URI uri;
		try {
			uri = new URI(giteePagesUrl);
		}
		catch (Exception e) {
			System.out.println("URL 异常：" + giteePagesUrl);
			throw new RuntimeException(e);
		}

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(uri).path("/").path("pages");

		String referer = builder.toUriString();
		String rebuild = builder.path("/").path("rebuild").toUriString();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set(HttpHeaders.COOKIE, giteePagesCookie);
		httpHeaders.set(HttpHeaders.HOST, "gitee.com");
		httpHeaders.setOrigin("https://gitee.com");
		httpHeaders.set(HttpHeaders.REFERER, referer);
		httpHeaders.set(HttpHeaders.USER_AGENT, giteePagesUserAgent);
		httpHeaders.set("X-CSRF-Token", giteePagesXCsrfToken);
		httpHeaders.set("X-Requested-With", "XMLHttpRequest");

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put("branch", Collections.singletonList(giteePagesBranch));
		requestBody.put("build_directory", Collections.singletonList(giteePagesBuildDirectory));
		requestBody.put("force_https", Collections.singletonList(giteePagesForceHttps));
		requestBody.put("auto_update", Collections.singletonList(giteePagesAutoUpdate));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		String string = new RestTemplate().postForObject(rebuild, httpEntity, String.class);

		System.out.println(string);
	}

}
